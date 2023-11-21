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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_item_price")
public class ItemPrice implements Serializable {
	private static final long serialVersionUID = -1792126556263107496L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private LocalDate date;
	private BigDecimal price;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_history_id")
	private ItemHistory itemHistory;
	
	public ItemPrice() {
	}

	public ItemPrice(LocalDate date, BigDecimal price) {
		this.date = date;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@JsonIgnore
	public ItemHistory getItemHistory() {
		return itemHistory;
	}

	public void setItemHistory(ItemHistory itemHistory) {
		this.itemHistory = itemHistory;
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
		ItemPrice other = (ItemPrice) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ItemPrice [id=" + id + ", date=" + date + ", price=" + price + "]";
	}
}