package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.kuntzedev.moneymaster.entities.FixedExpense;

public class FixedExpenseDTO implements Serializable {
	private static final long serialVersionUID = -6261046871447471677L;
	
	private Long id;
	private String title;
	private BigDecimal price;
	private int dayOfCharge;
	
	public FixedExpenseDTO() {
	}
	
	public FixedExpenseDTO(FixedExpense entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.price = entity.getPrice();
		this.dayOfCharge = entity.getDayOfCharge();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getDayOfCharge() {
		return dayOfCharge;
	}

	public void setDayOfCharge(int dateOfCharge) {
		this.dayOfCharge = dateOfCharge;
	}

	@Override
	public String toString() {
		return "VariableExpenseDTO [id=" + id + ", title=" + title + ", price=" + price + ", dateOfCharge="
				+ dayOfCharge + "]";
	}
}