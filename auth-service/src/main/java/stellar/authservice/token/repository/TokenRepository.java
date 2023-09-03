package stellar.authservice.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import stellar.authservice.token.model.Token;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String jwt);

    @Query("SELECT t FROM Token t WHERE t.user.id = :id AND (t.expired = false OR t.revoked = false)")
    Collection<Token> findAllValidTokenByUser(Long id);
}
