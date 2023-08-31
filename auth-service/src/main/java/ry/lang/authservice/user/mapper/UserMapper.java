package ry.lang.authservice.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;
import ry.lang.authservice.user.dto.UserInternalDto;
import ry.lang.authservice.user.model.User;
import ry.lang.authservice.user.projection.HasEmailHasPassword;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public UserDetails mapToUserDetails(HasEmailHasPassword user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public abstract UserInternalDto mapToDto(User user);

    public abstract User mapToEntity(UserInternalDto userInternalDto);

    public UserDetails mapToUserDetails(UserInternalDto savedUser){
        return new org.springframework.security.core.userdetails.User(savedUser.getEmail(), savedUser.getPassword(), new ArrayList<>());
    }
}