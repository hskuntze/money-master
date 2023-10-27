package br.com.kuntzedev.moneymaster.repositories.tokens;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.tokens.PasswordRecoveryToken;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long>{

	Optional<PasswordRecoveryToken> findByToken(String token);
}