package ry.lang.authservice.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ry.lang.authservice.token.service.TokenService;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (!hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        tokenService.findByToken(jwt).ifPresent(storedToken -> {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenService.save(storedToken);
            SecurityContextHolder.clearContext();
        });
    }
}
