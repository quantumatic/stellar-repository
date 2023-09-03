package stellar.authservice.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserDto {
    private final Long id;
    private final String username;
    private final String email;
    private final String firstname;
    private final String lastname;
    @JsonIgnore
    private final String password;
}
