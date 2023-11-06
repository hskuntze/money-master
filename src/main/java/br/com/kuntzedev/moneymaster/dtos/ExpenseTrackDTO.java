package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.kuntzedev.moneymaster.entities.ExpenseTrack;

public class ExpenseTrackDTO implements Serializable {
	private static final long serialVersionUID = -6179938791289338803L;

	private Long id;
	private BigDecimal monthlyIncome;
	private BigDecimal anualIncome;
	private BigDecimal extraIncome;
	private Integer dayOfSalaryPayment;
	private Float fluctuationByMonth;
	private List<TotalExpenseByMonthDTO> totalExpenseByMonths = new ArrayList<>();
	
	public ExpenseTrackDTO() {
	}
	
	public ExpenseTrackDTO(ExpenseTrack entity) {
		this.id = entity.getId();
		this.monthlyIncome = entity.getMonthlyIncome();
		this.anualIncome = entity.getAnualIncome();
		this.extraIncome = entity.getExtraIncome();
		this.dayOfSalaryPayment = entity.getDayOfSalaryPayment();
		this.fluctuationByMonth = entity.getFluctuationByMonth();
		
		this.totalExpenseByMonths.clear();
		entity.getTotalExpenseByMonths().forEach(tebm -> this.totalExpenseByMonths.add(new TotalExpenseByMonthDTO(tebm)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public BigDecimal getAnualIncome() {
		return anualIncome;
	}

	public void setAnualIncome(BigDecimal anualIncome) {
		this.anualIncome = anualIncome;
	}

	public BigDecimal getExtraIncome() {
		return extraIncome;
	}

	public void setExtraIncome(BigDecimal extraIncome) {
		this.extraIncome = extraIncome;
	}

	public Integer getDayOfSalaryPayment() {
		return dayOfSalaryPayment;
	}

	public void setDayOfSalaryPayment(Integer dayOfSalaryPayment) {
		this.dayOfSalaryPayment = dayOfSalaryPayment;
	}

	public Float getFluctuationByMonth() {
		return fluctuationByMonth;
	}

	public void setFluctuationByMonth(Float fluctuationByMonth) {
		this.fluctuationByMonth = fluctuationByMonth;
	}

	public List<TotalExpenseByMonthDTO> getTotalExpenseByMonths() {
		return totalExpenseByMonths;
	}

	public void setTotalExpenseByMonths(List<TotalExpenseByMonthDTO> totalExpenseByMonths) {
		this.totalExpenseByMonths = totalExpenseByMonths;
	}
}