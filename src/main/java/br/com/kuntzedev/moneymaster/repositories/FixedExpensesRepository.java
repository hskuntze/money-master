package br.com.kuntzedev.moneymaster.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzedev.moneymaster.entities.FixedExpense;

@Repository
public interface FixedExpensesRepository extends JpaRepository<FixedExpense, Long> {

	@Query("SELECT fe FROM FixedExpense fe " + "WHERE LOWER(fe.title) LIKE LOWER(:title)")
	Optional<List<FixedExpense>> findManyByTitle(String title);

	@Query("SELECT fe FROM FixedExpense fe " + "WHERE LOWER(fe.title) LIKE LOWER(:title)")
	Optional<FixedExpense> findByTitle(String title);

	@Query(nativeQuery = true, value = "SELECT DISTINCT tfe.id, tfe.begin_of_expense, tfe.day_of_charge, tfe.end_of_expense, tfe.price, tfe.title FROM tb_fixed_expense tfe "
			+ "LEFT JOIN tb_fixed_expenses_by_month tfebm ON tfe.id = tfebm.fixed_expense_id "
			+ "LEFT JOIN tb_total_expense_by_month ttebm ON tfebm.tbem_id = ttebm.id "
			+ "LEFT JOIN tb_expense_track tet ON ttebm.expense_track_id = tet.id "
			+ "LEFT JOIN tb_user tu ON tet.user_id = tu.id " + "WHERE tu.id = :userId AND tfebm.tbem_id = :tebmId")
	Page<FixedExpense> findAllByUserId(Long userId, Long tebmId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT DISTINCT tfe.id, tfe.begin_of_expense, tfe.day_of_charge, tfe.end_of_expense, tfe.title, tfe.price "
			+ "FROM tb_fixed_expense AS tfe "
			+ "LEFT JOIN tb_fixed_expenses_by_month AS tfebm ON tfe.id = tfebm.fixed_expense_id "
			+ "LEFT JOIN tb_total_expense_by_month AS tebm ON tfebm.tbem_id = tebm.id "
			+ "LEFT JOIN tb_expense_track AS tet ON tebm.expense_track_id = tet.id "
			+ "WHERE tfe.end_of_expense >= CURRENT_DATE "
			+ "AND tet.user_id = :userId", countQuery = "SELECT DISTINCT tfe.id, tfe.begin_of_expense, tfe.day_of_charge, tfe.end_of_expense, tfe.title, tfe.price "
					+ "FROM tb_fixed_expense AS tfe "
					+ "LEFT JOIN tb_fixed_expenses_by_month AS tfebm ON tfe.id = tfebm.fixed_expense_id "
					+ "LEFT JOIN tb_total_expense_by_month AS tebm ON tfebm.tbem_id = tebm.id "
					+ "LEFT JOIN tb_expense_track AS tet ON tebm.expense_track_id = tet.id "
					+ "WHERE tfe.end_of_expense >= CURRENT_DATE "
					+ "AND tet.user_id = :userId")
	Page<FixedExpense> findAllByUserIdWithValidDate(Long userId, Pageable pageable);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM tb_fixed_expenses_by_month tb WHERE tb.fixed_expense_id = :id")
	int deleteFixedExpenseInAuxTable(Long id);
}