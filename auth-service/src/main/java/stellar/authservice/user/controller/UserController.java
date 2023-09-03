package stellar.authservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stellar.authservice.user.dto.UserDto;
import stellar.authservice.user.service.UserService;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto getUser(Principal principal) {
        log.info("Request to get user: {}", principal.getName());
        UserDto userByEmail = userService.findUserByEmail(principal.getName());
        log.info("User found: {}", userByEmail);
        return userByEmail;
    }
}
