package br.com.kuntzedev.moneymaster.dtos;

import br.com.kuntzedev.moneymaster.entities.Vault;

public class VaultSavingsResponseDTO extends VaultDTO {
	private static final long serialVersionUID = 7849297566774101184L;
	
	private String message;

	public VaultSavingsResponseDTO() {
	}
	
	public VaultSavingsResponseDTO(String message, Vault vault) {
		super(vault);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "VaultSavingsResponseDTO [message=" + message + "]";
	}
}