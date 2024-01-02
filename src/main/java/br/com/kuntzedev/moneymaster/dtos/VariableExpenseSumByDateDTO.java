package br.com.kuntzedev.moneymaster.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VariableExpenseSumByDateDTO {

	public LocalDate dateOfCharge;
	public BigDecimal sum;
	
	public VariableExpenseSumByDateDTO() {
	}
	
	public VariableExpenseSumByDateDTO(LocalDate dateOfCharge, BigDecimal sum) {
		this.dateOfCharge = dateOfCharge;
		this.sum = sum;
	}

	public LocalDate getDateOfCharge() {
		return dateOfCharge;
	}

	public void setDateOfCharge(LocalDate dateOfCharge) {
		this.dateOfCharge = dateOfCharge;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
}