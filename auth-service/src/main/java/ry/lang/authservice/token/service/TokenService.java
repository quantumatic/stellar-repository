package ry.lang.authservice.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ry.lang.authservice.token.dto.TokenDto;
import ry.lang.authservice.token.model.Token;
import ry.lang.authservice.token.repository.TokenRepository;
import ry.lang.authservice.token.utils.mapper.TokenMapper;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    public Optional<TokenDto> findByToken(String jwt) {
        return tokenRepository.findByToken(jwt).map(tokenMapper::toDto);
    }

    public TokenDto save(TokenDto tokenDto) {
        Token saved = tokenRepository.save(tokenMapper.toEntity(tokenDto));
        return tokenMapper.toDto(saved);
    }

    public Collection<TokenDto> findAllValidTokenByUser(Long id) {
        return tokenRepository.findAllValidTokenByUser(id).stream()
                .map(tokenMapper::toDto)
                .toList();
    }

    public void saveAll(Collection<TokenDto> validUserTokens) {
        tokenRepository.saveAll(validUserTokens.stream()
                .map(tokenMapper::toEntity)
                .toList());
    }
}
