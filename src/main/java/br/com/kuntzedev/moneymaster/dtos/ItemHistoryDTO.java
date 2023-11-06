package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.kuntzedev.moneymaster.entities.ItemHistory;
import br.com.kuntzedev.moneymaster.entities.ItemPrice;

public class ItemHistoryDTO implements Serializable {
	private static final long serialVersionUID = -6545922534243876352L;
	
	private Long id;
	private Float fluctuation;
	private List<ItemPrice> itemPrices = new ArrayList<>();
	
	public ItemHistoryDTO() {
	}
	
	public ItemHistoryDTO(ItemHistory entity) {
		this.id = entity.getId();
		this.fluctuation = entity.getFluctuation();
		this.itemPrices = entity.getItemPrices();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getFluctuation() {
		return fluctuation;
	}

	public void setFluctuation(Float fluctuation) {
		this.fluctuation = fluctuation;
	}
	
	public List<ItemPrice> getItemPrices() {
		return itemPrices;
	}

	public void setItemPrices(List<ItemPrice> itemPrices) {
		this.itemPrices = itemPrices;
	}

	@Override
	public String toString() {
		return "ItemHistoryDTO [id=" + id + ", fluctuation=" + fluctuation + ", itemPrices=" + itemPrices + "]";
	}
}