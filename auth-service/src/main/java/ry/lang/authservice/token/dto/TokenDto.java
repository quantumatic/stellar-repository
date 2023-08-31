package ry.lang.authservice.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ry.lang.authservice.user.dto.UserInternalDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    public Long id;
    public String token;
    public boolean revoked;
    public boolean expired;
    public UserInternalDto user;
}
