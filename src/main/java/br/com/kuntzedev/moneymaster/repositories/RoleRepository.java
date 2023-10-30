package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_user_role tur "
			+ "WHERE tur.user_id = :userId")
	int deleteUserFromUserRoleTable(Long userId);
}