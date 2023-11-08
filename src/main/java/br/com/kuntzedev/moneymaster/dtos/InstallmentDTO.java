package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.kuntzedev.moneymaster.entities.Installment;

public class InstallmentDTO implements Serializable {
	private static final long serialVersionUID = -8112874367083241182L;

	private Long id;
	private BigDecimal price;
	private LocalDate dateOfCharge;
	
	public InstallmentDTO() {
	}
	
	public InstallmentDTO(Installment entity) {
		this.id = entity.getId();
		this.price = entity.getPrice();
		this.dateOfCharge = entity.getDateOfCharge();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "InstallmentDTO [id=" + id + ", price=" + price + ", dateOfCharge=" + dateOfCharge + "]";
	}
}