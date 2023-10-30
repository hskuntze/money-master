package br.com.kuntzedev.moneymaster.repositories.tokens;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.tokens.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{

	Optional<VerificationToken> findByToken(String token);
	
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_verification_token tvk "
			+ "WHERE tvk.token = :token")
	int deleteVerificationTokenByTokenNumber(String token);
}