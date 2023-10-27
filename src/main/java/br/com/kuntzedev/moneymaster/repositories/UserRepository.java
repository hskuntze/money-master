package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	@Query(nativeQuery = true, value = "SELECT id, email, name, id_number, enabled FROM tb_user "
			+ " INNER JOIN tb_rocovery_token ON id = tb_recovery_token.user_id "
			+ " WHERE tb_recovery_token.token = :token")
	User findByRecoveryToken(String token);
}