package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.kuntzedev.moneymaster.entities.FixedExpense;
import br.com.kuntzedev.moneymaster.entities.TotalExpenseByMonth;
import br.com.kuntzedev.moneymaster.entities.VariableExpense;

public class TotalExpenseByMonthDTO implements Serializable {
	private static final long serialVersionUID = -3141747452651754505L;

	private Long id;
	private LocalDate date;
	private BigDecimal totalExpended;
	private BigDecimal remainingAmount;
	private List<VariableExpense> variableExpenses = new ArrayList<VariableExpense>();
	private List<FixedExpense> fixedExpenses  = new ArrayList<FixedExpense>();
	
	public TotalExpenseByMonthDTO() {
	}
	
	public TotalExpenseByMonthDTO(TotalExpenseByMonth entity) {
		this.id = entity.getId();
		this.date = entity.getDate();
		this.totalExpended = entity.getTotalExpended();
		this.remainingAmount = entity.getRemainingAmount();
		
		//Isso é uma forma de forçar o lazy load a funcionar!
		entity.getFixedExpenses().forEach(fe -> this.fixedExpenses.add(fe));
		entity.getVariableExpenses().forEach(ve -> this.variableExpenses.add(ve));
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public BigDecimal getTotalExpended() {
		return totalExpended;
	}
	
	public void setTotalExpended(BigDecimal totalExpended) {
		this.totalExpended = totalExpended;
	}
	
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public List<VariableExpense> getVariableExpenses() {
		return variableExpenses;
	}

	public List<FixedExpense> getFixedExpenses() {
		return fixedExpenses;
	}

	@Override
	public String toString() {
		return "TotalExpenseByMonthDTO [id=" + id + ", date=" + date + ", totalExpended=" + totalExpended
				+ ", remainingAmount=" + remainingAmount + "]";
	}
}