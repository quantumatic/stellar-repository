package stellar.authservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stellar.authservice.user.dto.UserDto;
import stellar.authservice.user.exception.UserNotFoundException;
import stellar.authservice.user.mapper.UserMapper;
import stellar.authservice.user.model.User;
import stellar.authservice.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findUserByEmail(String email) {
        return userMapper.mapToDto(findEntityByEmail(email));
    }

    private User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email"));
    }

    public UserDto save(UserDto user) {
        User saved = userRepository.save(userMapper.mapToEntity(user));
        return userMapper.mapToDto(saved);
    }
}
