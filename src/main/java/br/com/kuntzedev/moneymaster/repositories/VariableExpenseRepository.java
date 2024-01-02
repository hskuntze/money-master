package br.com.kuntzedev.moneymaster.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByDateDTO;
import br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByTitleDTO;
import br.com.kuntzedev.moneymaster.entities.VariableExpense;

@Repository
public interface VariableExpenseRepository extends JpaRepository<VariableExpense, Long>{
	@Query("SELECT new br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByTitleDTO(obj.title, SUM(obj.price)) "
			+ " FROM VariableExpense obj "
			+ " WHERE obj.totalExpenseByMonth.id = :monthId "
			+ " GROUP BY obj.title")
	List<VariableExpenseSumByTitleDTO> sumByTitle(Long monthId);
	
	@Query("SELECT new br.com.kuntzedev.moneymaster.dtos.VariableExpenseSumByDateDTO(obj.dateOfCharge, SUM(obj.price)) "
			+ " FROM VariableExpense obj "
			+ " WHERE obj.totalExpenseByMonth.id = :monthId "
			+ " GROUP BY obj.dateOfCharge")
	List<VariableExpenseSumByDateDTO> sumByDateOfCharge(Long monthId);
}