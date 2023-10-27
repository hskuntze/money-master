package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
