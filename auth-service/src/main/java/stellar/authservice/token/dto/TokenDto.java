package stellar.authservice.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stellar.authservice.user.dto.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    public Long id;
    public String token;
    public boolean revoked;
    public boolean expired;
    public UserDto user;
}
