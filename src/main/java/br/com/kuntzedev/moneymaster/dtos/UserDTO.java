package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import br.com.kuntzedev.moneymaster.entities.User;
import br.com.kuntzedev.moneymaster.enums.GenderType;
import br.com.kuntzedev.moneymaster.services.validators.VerifyUserExistence;

@VerifyUserExistence
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 5734701072206258380L;

	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private Long idNumber;
	private Integer idType;
	private LocalDate birth;
	private boolean enabled;
	private GenderType gender;
	private AddressDTO address;
	private Set<RoleDTO> roles = new HashSet<>();
	private VaultDTO vault;
	
	public UserDTO() {
	}
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.idNumber = user.getIdNumber();
		this.idType = user.getIdType();
		this.enabled = user.isEnabled();
		this.gender = user.getGender();
		this.address = new AddressDTO(user.getAddress());
		this.vault = new VaultDTO(user.getVault());
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

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public VaultDTO getVault() {
		return vault;
	}

	public void setVault(VaultDTO vault) {
		this.vault = vault;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", idNumber=" + idNumber + ", idType=" + idType + ", birth=" + birth + ", enabled=" + enabled
				+ ", gender=" + gender + ", roles=" + roles + "]";
	}
}