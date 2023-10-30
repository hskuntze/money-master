package br.com.kuntzedev.moneymaster.repositories.tokens;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.tokens.PasswordRecoveryToken;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long>{

	Optional<PasswordRecoveryToken> findByToken(String token);
	
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_recovery_token trk "
			+ "WHERE trk.token = :token")
	int deleteRecoveryTokenByTokenNumber(String token);
}