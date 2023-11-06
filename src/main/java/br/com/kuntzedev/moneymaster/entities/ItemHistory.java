package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_item_history")
public class ItemHistory implements Serializable {
	private static final long serialVersionUID = -6545922534243876352L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Float fluctuation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@OneToMany(mappedBy = "itemHistory", fetch = FetchType.EAGER)
	private List<ItemPrice> itemPrices = new ArrayList<>();

	public ItemHistory() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getFluctuation() {
		return calculateFluctuation();
	}

	public void setFluctuation(Float fluctuation) {
		this.fluctuation = fluctuation;
	}
	
	@JsonIgnore
	public Item getItem() {
		return item;
	}

	public List<ItemPrice> getItemPrices() {
		return itemPrices;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public Float calculateFluctuation() {
		float percentage = 0.0f;
		int size = itemPrices.size();
		
		if(!(itemPrices.size() <= 1)) {
			BigDecimal previous = itemPrices.get(size - 2).getPrice();
			BigDecimal current = itemPrices.get(size - 1).getPrice();
			
			BigDecimal difference = current.subtract(previous);
			percentage = difference.divide(previous, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).floatValue();
		}
		
		return percentage;
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
		ItemHistory other = (ItemHistory) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ItemHistory [id=" + id + ", fluctuation=" + fluctuation + ", itemPrices=" + itemPrices + "]";
	}
}