package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.com.kuntzedev.moneymaster.entities.User;

public class UserSimpleRegisterDTO implements Serializable {
	private static final long serialVersionUID = -4680619636248455326L;
	
	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private Long idNumber;
	private Integer idType;
	private boolean enabled;
	private boolean registrationCompleted;
	private Set<RoleDTO> roles = new HashSet<>();
	private VaultDTO vault;
	
	public UserSimpleRegisterDTO() {
	}
	
	public UserSimpleRegisterDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.idNumber = user.getIdNumber();
		this.idType = user.getIdType();
		this.enabled = user.isEnabled();
		this.vault = new VaultDTO(user.getVault());
		
		this.roles.clear();
		user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}

	public Integer getIdType() {
		return idType;
	}

	public void setIdType(Integer idType) {
		this.idType = idType;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public VaultDTO getVault() {
		return vault;
	}

	public void setVault(VaultDTO vault) {
		this.vault = vault;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public boolean isRegistrationCompleted() {
		return registrationCompleted;
	}

	public void setRegistrationCompleted(boolean registrationCompleted) {
		this.registrationCompleted = registrationCompleted;
	}

	@Override
	public String toString() {
		return "UserSimpleRegisterDTO [id=" + id + ", name=" + name + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", idNumber=" + idNumber + ", idType=" + idType + ", enabled=" + enabled + ", roles="
				+ roles + ", vault=" + vault + "]";
	}
}
