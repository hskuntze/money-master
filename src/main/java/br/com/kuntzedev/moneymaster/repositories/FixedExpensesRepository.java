package br.com.kuntzedev.moneymaster.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.FixedExpense;

@Repository
public interface FixedExpensesRepository extends JpaRepository<FixedExpense, Long>{

	@Query("SELECT fe FROM FixedExpense fe "
			+ "WHERE LOWER(fe.title) LIKE LOWER(CONCAT('%',:title,'%'))")
	Optional<FixedExpense> findByTitle(String title);	
}