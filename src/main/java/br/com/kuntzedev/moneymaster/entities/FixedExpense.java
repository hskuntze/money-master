package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_fixed_expense")
public class FixedExpense implements Serializable {
	private static final long serialVersionUID = 1235091813641078895L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private BigDecimal price;
	private int dayOfCharge;

	private LocalDate beginOfExpense;
	private LocalDate endOfExpense;

	public FixedExpense() {
	}

	public FixedExpense(String title, BigDecimal price, int dayOfCharge) {
		this.title = title;
		this.price = price;
		this.dayOfCharge = dayOfCharge;
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

	public void setDayOfCharge(int dayOfCharge) {
		this.dayOfCharge = dayOfCharge;
	}

	public LocalDate getBeginOfExpense() {
		return beginOfExpense;
	}

	public void setBeginOfExpense(LocalDate beginOfExpense) {
		this.beginOfExpense = beginOfExpense;
	}

	public LocalDate getEndOfExpense() {
		return endOfExpense;
	}

	public void setEndOfExpense(LocalDate endOfExpense) {
		this.endOfExpense = endOfExpense;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FixedExpense other = (FixedExpense) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "FixedExpense [title=" + title + ", price=" + price + ", dayOfCharge=" + dayOfCharge
				+ ", beginOfExpense=" + beginOfExpense + ", endOfExpense=" + endOfExpense + "]";
	}
}