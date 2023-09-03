package stellar.authservice.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;
}
