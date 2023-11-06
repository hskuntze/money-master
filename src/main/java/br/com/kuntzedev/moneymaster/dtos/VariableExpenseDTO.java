package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VariableExpenseDTO implements Serializable {
	private static final long serialVersionUID = -6261046871447471677L;
	
	private Long id;
	private String title;
	private BigDecimal price;
	private LocalDate dateOfCharge;
	
	public VariableExpenseDTO() {
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

	public LocalDate getDateOfCharge() {
		return dateOfCharge;
	}

	public void setDateOfCharge(LocalDate dateOfCharge) {
		this.dateOfCharge = dateOfCharge;
	}

	@Override
	public String toString() {
		return "VariableExpenseDTO [id=" + id + ", title=" + title + ", price=" + price + ", dateOfCharge="
				+ dateOfCharge + "]";
	}
}