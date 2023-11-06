package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.kuntzedev.moneymaster.entities.Item;
import br.com.kuntzedev.moneymaster.entities.ItemHistory;

public class ItemDTO implements Serializable {
	private static final long serialVersionUID = 8096854116134620745L;

	private Long id;
	private String name;
	private BigDecimal price;
	private String link;
	private String image;
	private Float variation;
	
	private ItemHistory itemHistory;
	
	public ItemDTO() {
	}
	
	public ItemDTO(Item item) {
		this.id = item.getId();
		this.name = item.getName();
		this.price = item.getPrice();
		this.link = item.getLink();
		this.image = item.getImage();
		this.variation = item.getVariation();
		
		this.itemHistory = item.getItemHistory();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Float getVariation() {
		return variation;
	}

	public void setVariation(Float variation) {
		this.variation = variation;
	}

	public ItemHistory getItemHistory() {
		return itemHistory;
	}

	public void setItemHistory(ItemHistory itemHistory) {
		this.itemHistory = itemHistory;
	}

	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", name=" + name + ", price=" + price + ", link=" + link + ", image=" + image
				+ ", variation=" + variation + ", itemHistory=" + itemHistory + "]";
	}
}