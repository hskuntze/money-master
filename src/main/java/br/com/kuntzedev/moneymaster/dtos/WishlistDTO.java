package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.kuntzedev.moneymaster.entities.Wishlist;

public class WishlistDTO implements Serializable {
	private static final long serialVersionUID = -4380458842747153051L;

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
	
	private UserBasicDTO user;
	private List<ItemDTO> items = new ArrayList<>();
	private List<InstallmentDTO> installments = new ArrayList<>();
	
	public WishlistDTO() {
	}
	
	public WishlistDTO(Wishlist entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.description = entity.getDescription();
		this.totalValue = entity.getTotalValue();
		this.created = entity.getCreated();
		this.enabled = entity.isEnabled();
		this.toBuyAt = entity.getToBuyAt();
		this.installment = entity.isInstallment();
		this.totalInstallments = entity.getTotalInstallments();
		this.installmentsValue = entity.getInstallmentsValue();
		
		this.user = new UserBasicDTO(entity.getUser());
		
		this.items.clear();
		entity.getItems().forEach(item -> this.items.add(new ItemDTO(item)));
		
		this.installments.clear();
		entity.getInstallments().forEach(installment -> this.installments.add(new InstallmentDTO(installment)));
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
		return totalValue;
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
	
	public List<ItemDTO> getItems() {
		return items;
	}

	public List<InstallmentDTO> getInstallments() {
		return installments;
	}

	@JsonIgnore
	public UserBasicDTO getUser() {
		return user;
	}

	public void setUser(UserBasicDTO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "WishlistDTO [id=" + id + ", title=" + title + ", description=" + description + ", totalValue="
				+ totalValue + ", installment=" + installment + ", totalInstallments=" + totalInstallments
				+ ", installmentsValue=" + installmentsValue + "]";
	}
}