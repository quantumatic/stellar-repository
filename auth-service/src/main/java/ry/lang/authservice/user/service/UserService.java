package ry.lang.authservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ry.lang.authservice.user.dto.UserInternalDto;
import ry.lang.authservice.user.exception.UserNotFoundException;
import ry.lang.authservice.user.model.User;
import ry.lang.authservice.user.repository.UserRepository;
import ry.lang.authservice.user.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserInternalDto findUserByEmail(String email) {
        return userMapper.mapToDto(findEntityByEmail(email));
    }

    private User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email"));
    }

    public UserInternalDto save(UserInternalDto user) {
        User saved = userRepository.save(userMapper.mapToEntity(user));
        return userMapper.mapToDto(saved);
    }
}
