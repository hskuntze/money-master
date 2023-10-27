package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.kuntzedev.moneymaster.entities.Vault;

public class VaultDTO implements Serializable {
	private static final long serialVersionUID = -4472915620785600370L;
	
	private Long id;
	private BigDecimal savings;
	private BigDecimal onWallet;
	private BigDecimal allowedToSpend;
	
	public VaultDTO() {
	}
	
	public VaultDTO(Vault vault) {
		this.id = vault.getId();
		this.savings = vault.getSavings();
		this.onWallet = vault.getOnWallet();
		this.allowedToSpend = vault.getAllowedToSpend();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getSavings() {
		return savings;
	}

	public void setSavings(BigDecimal savings) {
		this.savings = savings;
	}

	public BigDecimal getOnWallet() {
		return onWallet;
	}

	public void setOnWallet(BigDecimal onWallet) {
		this.onWallet = onWallet;
	}

	public BigDecimal getAllowedToSpend() {
		return allowedToSpend;
	}

	public void setAllowedToSpend(BigDecimal allowedToSpend) {
		this.allowedToSpend = allowedToSpend;
	}

	@Override
	public String toString() {
		return "VaultDTO [id=" + id + ", savings=" + savings + ", onWallet=" + onWallet + ", allowedToSpend="
				+ allowedToSpend + "]";
	}
}