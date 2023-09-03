package stellar.authservice.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stellar.authservice.auth.dto.AuthenticationRequest;
import stellar.authservice.auth.dto.AuthenticationResponse;
import stellar.authservice.auth.dto.RegisterRequest;
import stellar.authservice.token.dto.TokenDto;
import stellar.authservice.token.service.TokenService;
import stellar.authservice.user.dto.UserDto;
import stellar.authservice.user.mapper.UserMapper;
import stellar.authservice.user.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        UserDto savedUser = saveNewUser(request);
        UserDetails userDetails = userMapper.mapToUserDetails(savedUser);
        return generateAndSaveTokens(userDetails, savedUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticateUser(request.getEmail(), request.getPassword());
        UserDto user = userService.findUserByEmail(request.getEmail());
        UserDetails userDetails = userMapper.mapToUserDetails(user);
        revokeAllUserTokens(user);
        return generateAndSaveTokens(userDetails, user);
    }

    private UserDto saveNewUser(RegisterRequest request) {
        UserDto user = createUserFromRequest(request);
        return userService.save(user);
    }

    private UserDto createUserFromRequest(RegisterRequest request) {
        return UserDto.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private AuthenticationResponse generateAndSaveTokens(UserDetails userDetails, UserDto user) {
        Tokens tokens = generateTokens(userDetails);
        saveUserToken(user, tokens.jwtToken());
        return AuthenticationResponse.builder()
                .accessToken(tokens.jwtToken())
                .refreshToken(tokens.refreshToken())
                .build();
    }

    private Tokens generateTokens(UserDetails userDetails) {
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        return new Tokens(jwtToken, refreshToken);
    }

    private void saveUserToken(UserDto user, String jwtToken) {
        var token = TokenDto.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    private void revokeAllUserTokens(UserDto user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<String> refreshTokenOpt = extractRefreshTokenFromHeader(request);

        if (refreshTokenOpt.isEmpty()) {
            return;
        }

        String refreshToken = refreshTokenOpt.get();
        Optional<UserDto> userOpt = findUserByRefreshToken(refreshToken);

        if (userOpt.isEmpty()) {
            return;
        }

        UserDto user = userOpt.get();
        UserDetails userDetails = userMapper.mapToUserDetails(user);

        if (!isTokenValid(refreshToken, userDetails)) {
            return;
        }

        AuthenticationResponse authResponse = regenerateTokens(userDetails, user, refreshToken);
        writeResponse(response, authResponse);
    }

    private Optional<String> extractRefreshTokenFromHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Optional.ofNullable(authHeader)
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7));
    }

    private Optional<UserDto> findUserByRefreshToken(String refreshToken) {
        String userEmail = jwtService.extractEmail(refreshToken);
        return Optional.ofNullable(userEmail).map(userService::findUserByEmail);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        return jwtService.isTokenValid(token, userDetails);
    }

    private AuthenticationResponse regenerateTokens(UserDetails userDetails, UserDto user, String refreshToken) {
        String newAccessToken = jwtService.generateToken(userDetails);
        revokeAllUserTokens(user);
        saveUserToken(user, newAccessToken);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void writeResponse(HttpServletResponse response, AuthenticationResponse authResponse) throws IOException {
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    private record Tokens(String jwtToken, String refreshToken) {
    }
}
