package stellar.authservice.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stellar.authservice.token.dto.TokenDto;
import stellar.authservice.token.model.Token;
import stellar.authservice.token.repository.TokenRepository;
import stellar.authservice.token.utils.mapper.TokenMapper;

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
