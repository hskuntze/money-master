package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_wishlist")
public class Wishlist implements Serializable {
	private static final long serialVersionUID = -4380458842747153051L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private BigDecimal totalValue;
	private LocalDate created;
	private boolean enabled;
	private LocalDate toBuyAt;
	private boolean installment;
	private Integer totalInstallments;
	private BigDecimal installmentsValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
	private List<Item> items = new ArrayList<>();
	
	@OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
	private List<Installment> installments = new ArrayList<>();
	
	public Wishlist() {
	}
	
	public Wishlist(String title, String description, LocalDate toBuyAt, boolean installment, Integer totalInstallments) {
		this.title = title;
		this.description = description;
		this.toBuyAt = toBuyAt;
		this.installment = installment;
		this.totalInstallments = totalInstallments;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTotalValue() {
		//return totalValue;
		return calculateTotalValue();
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDate getToBuyAt() {
		return toBuyAt;
	}

	public void setToBuyAt(LocalDate toBuyAt) {
		this.toBuyAt = toBuyAt;
	}

	public boolean isInstallment() {
		return installment;
	}

	public void setInstallment(boolean installment) {
		this.installment = installment;
	}

	public Integer getTotalInstallments() {
		return totalInstallments;
	}

	public void setTotalInstallments(Integer totalInstallments) {
		this.totalInstallments = totalInstallments;
	}

	public BigDecimal getInstallmentsValue() {
		return installmentsValue;
	}

	public void setInstallmentsValue(BigDecimal installmentsValue) {
		this.installmentsValue = installmentsValue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Item> getItems() {
		return items;
	}

	public List<Installment> getInstallments() {
		return installments;
	}
	
	public BigDecimal calculateTotalValue() {
		BigDecimal total = BigDecimal.ZERO;
		
		for(Item item : this.items) {
			total = total.add(item.getPrice());
		}
		
		return total;
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
		Wishlist other = (Wishlist) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Wishlist [id=" + id + ", title=" + title + ", totalValue=" + totalValue + ", installment=" + installment
				+ ", totalInstallments=" + totalInstallments + ", installmentsValue=" + installmentsValue + "]";
	}
}