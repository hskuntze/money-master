package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_variable_expense")
public class VariableExpense implements Serializable {
	private static final long serialVersionUID = -6261046871447471677L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private BigDecimal price;
	private LocalDate dateOfCharge;
	
	@ManyToOne
	@JoinColumn(name = "month_id")
	private TotalExpenseByMonth totalExpenseByMonth;

	public VariableExpense() {
	}
	
	public VariableExpense(String title, BigDecimal price, LocalDate dateOfCharge) {
		this.title = title;
		this.price = price;
		this.dateOfCharge = dateOfCharge;
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

	public void setTotalExpenseByMonth(TotalExpenseByMonth totalExpenseByMonth) {
		this.totalExpenseByMonth = totalExpenseByMonth;
	}
}