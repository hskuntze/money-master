package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TotalExpenseByMonthBasicDTO implements Serializable {
	private static final long serialVersionUID = -1425767308174548680L;
	
	private LocalDate date;
	private BigDecimal totalExpended;
	
	public TotalExpenseByMonthBasicDTO() {
	}
	
	public TotalExpenseByMonthBasicDTO(LocalDate date, BigDecimal totalExpended) {
		this.date = date;
		this.totalExpended = totalExpended;
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
}