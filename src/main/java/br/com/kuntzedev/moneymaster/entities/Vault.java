package br.com.kuntzedev.moneymaster.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_vault")
public class Vault implements Serializable {
	private static final long serialVersionUID = -4472915620785600370L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal savings;
	private BigDecimal onWallet;
	private BigDecimal allowedToSpend;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public Vault() {
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
	
	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal increaseWalletValue(BigDecimal value) {
		BigDecimal newValue = this.onWallet.add(value);
		this.setOnWallet(newValue);
		return this.getOnWallet();
	}
	
	/**
	 * Apenas "onWallet" poderá conter valores negativos.
	 */
	public BigDecimal reduceWalletValue(BigDecimal value) {
		BigDecimal newValue = this.onWallet.subtract(value);
		this.setOnWallet(newValue);
		return this.getOnWallet();
	}
	
	public BigDecimal increaseSavingsValue(BigDecimal value) {
		BigDecimal newValue = this.savings.add(value);
		this.setSavings(newValue);
		return this.getSavings();
	}
	
	public BigDecimal reduceSavingsValue(BigDecimal value) {
		BigDecimal newValue = this.savings.subtract(value);
		
		if(newValue.signum() <= 0) {
			newValue = BigDecimal.ZERO;
		}
		
		this.setSavings(newValue);
		return this.getSavings();
	}
	
	public BigDecimal increaseAllowedToSpendValue(BigDecimal value) {
		BigDecimal newValue = this.allowedToSpend.add(value);
		this.setAllowedToSpend(newValue);
		return this.getAllowedToSpend();
	}
	
	public BigDecimal reduceAllowedToSpendValue(BigDecimal value) {
		BigDecimal newValue = this.allowedToSpend.subtract(value);
		
		if(newValue.signum() <= 0) {
			newValue = BigDecimal.ZERO;
		}
		
		this.setAllowedToSpend(newValue);
		return this.getAllowedToSpend();
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
		Vault other = (Vault) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Vault [id=" + id + ", savings=" + savings + ", onWallet=" + onWallet + ", allowedToSpend="
				+ allowedToSpend + "]";
	}
}