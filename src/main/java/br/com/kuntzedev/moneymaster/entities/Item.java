package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.kuntzedev.moneymaster.enums.SourcePlatform;

@Entity
@Table(name = "tb_item")
public class Item implements Serializable {
	private static final long serialVersionUID = 8096854116134620745L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;
	@Column(columnDefinition = "TEXT")
	private String link;
	@Column(columnDefinition = "TEXT")
	private String image;
	private Float variation;
	private SourcePlatform sourcePlatform;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wishlist_id")
	private Wishlist wishlist;
	
	@OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
	private ItemHistory itemHistory;
	
	public Item() {
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

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}

	public ItemHistory getItemHistory() {
		return itemHistory;
	}

	public void setItemHistory(ItemHistory itemHistory) {
		this.itemHistory = itemHistory;
	}

	public String getSourcePlatformName() {
		return sourcePlatform.getDesc();
	}

	@JsonIgnore
	public SourcePlatform getSourcePlatform() {
		return sourcePlatform;
	}

	public void setSourcePlatform(SourcePlatform sourcePlatform) {
		this.sourcePlatform = sourcePlatform;
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
		Item other = (Item) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", link=" + link + ", image=" + image
				+ ", variation=" + variation + ", sourcePlatform=" + sourcePlatform + "]";
	}
}