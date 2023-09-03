package stellar.authservice.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stellar.authservice.auth.dto.AuthenticationRequest;
import stellar.authservice.auth.dto.AuthenticationResponse;
import stellar.authservice.auth.dto.RegisterRequest;
import stellar.authservice.auth.service.AuthenticationService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Request to register user: {}", request.getEmail());
        AuthenticationResponse register = authService.register(request);
        log.info("User registered successfully: {}", register);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        log.info("Request to authenticate user: {}", request.getEmail());
        AuthenticationResponse authenticate = authService.authenticate(request);
        log.info("User authenticated successfully: {}", authenticate);
        return ResponseEntity.ok(authenticate);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Request to refresh token");
        authService.refreshToken(request, response);
        log.info("Token refreshed successfully");
    }
}
