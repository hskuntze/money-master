package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public class ScrapingItemDTO implements Serializable {
	private static final long serialVersionUID = 3862791569776635445L;
	
	private BigDecimal price;
	private String name;
	private String link;
	private String image;

	public ScrapingItemDTO() {
	}

	public ScrapingItemDTO(BigDecimal price, String name, String link, String image) {
		this.price = price;
		this.name = name;
		this.link = link;
		this.image = image;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(String price) {
		double auxValue = Double.valueOf(price);
		this.price = BigDecimal.valueOf(auxValue);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "ScrapingItemDTO [price=" + price + ", name=" + name + ", link=" + link + ", image=" + image + "]";
	}
}