package ry.lang.authservice.token.utils.mapper;

import org.mapstruct.Mapper;
import ry.lang.authservice.token.dto.TokenDto;
import ry.lang.authservice.token.model.Token;

@Mapper(componentModel = "spring")
public abstract class TokenMapper {

    public abstract TokenDto toDto(Token token);

    public abstract Token toEntity(TokenDto tokenDto);
}
