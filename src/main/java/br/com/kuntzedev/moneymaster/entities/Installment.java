package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_installment")
public class Installment implements Serializable {
	private static final long serialVersionUID = -8112874367083241182L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal price;
	private LocalDate dateOfCharge;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wishlist_id")
	private Wishlist wishlist;
	
	public Installment() {
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
	
	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
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
		Installment other = (Installment) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Installment [id=" + id + ", price=" + price + ", dateOfCharge=" + dateOfCharge + "]";
	}
}