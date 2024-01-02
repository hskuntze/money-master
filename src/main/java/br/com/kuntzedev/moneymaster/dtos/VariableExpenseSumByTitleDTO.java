package br.com.kuntzedev.moneymaster.dtos;

import java.math.BigDecimal;

public class VariableExpenseSumByTitleDTO {
	
	private String title;
	private BigDecimal sum;
	
	public VariableExpenseSumByTitleDTO() {
	}
	
	public VariableExpenseSumByTitleDTO(String title, BigDecimal price) {
		this.title = title;
		this.sum = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
}