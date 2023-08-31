package ry.lang.authservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ry.lang.authservice.user.dto.UserInternalDto;
import ry.lang.authservice.user.service.UserService;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserInternalDto getUser(Principal principal) {
        log.info("Request to get user: {}", principal.getName());
        UserInternalDto userByEmail = userService.findUserByEmail(principal.getName());
        log.info("User found: {}", userByEmail);
        return userByEmail;
    }
}
