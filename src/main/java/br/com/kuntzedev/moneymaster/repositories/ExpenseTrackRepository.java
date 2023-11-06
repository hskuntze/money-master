package br.com.kuntzedev.moneymaster.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.ExpenseTrack;

@Repository
public interface ExpenseTrackRepository extends JpaRepository<ExpenseTrack, Long>{

	@Query("SELECT COUNT(et) FROM ExpenseTrack et "
			+ "WHERE et.user.id = :userId")
	int verifyIfExpenseTrackExists(Long userId);
	
	@Query("SELECT et FROM ExpenseTrack et "
			+ "WHERE et.user.id = :userId")
	Optional<ExpenseTrack> findByUserId(Long userId);
}