package br.com.kuntzedev.moneymaster.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.VariableExpense;

@Repository
public interface VariableExpenseRepository extends JpaRepository<VariableExpense, Long>{
}