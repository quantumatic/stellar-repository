package stellar.authservice.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;
import stellar.authservice.user.dto.UserDto;
import stellar.authservice.user.model.User;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserDto mapToDto(User user);

    public abstract User mapToEntity(UserDto userDto);

    public UserDetails mapToUserDetails(UserDto savedUser) {
        return new org.springframework.security.core.userdetails.User(savedUser.getEmail(), savedUser.getPassword(), new ArrayList<>());
    }

    public UserDetails mapToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}