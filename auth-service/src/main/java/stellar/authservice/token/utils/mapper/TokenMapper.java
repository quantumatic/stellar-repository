package stellar.authservice.token.utils.mapper;

import org.mapstruct.Mapper;
import stellar.authservice.token.dto.TokenDto;
import stellar.authservice.token.model.Token;

@Mapper(componentModel = "spring")
public abstract class TokenMapper {

    public abstract TokenDto toDto(Token token);

    public abstract Token toEntity(TokenDto tokenDto);
}
