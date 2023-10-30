package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Vault;

@Repository
public interface VaultRepository extends JpaRepository<Vault, Long>{
	
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_vault tv "
			+ "WHERE tv.user_id = :userId")
	int deleteFromVaultTable(Long userId);
}