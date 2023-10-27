package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.com.kuntzedev.moneymaster.entities.User;

public class UserBasicDTO implements Serializable {
	private static final long serialVersionUID = 5734701072206258380L;

	private Long id;
	private String email;
	private String name;
	private Long idNumber;
	private Set<RoleDTO> roles = new HashSet<>();
	private boolean enabled;
	
	public UserBasicDTO() {
	}
	
	public UserBasicDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.idNumber = user.getIdNumber();
		this.enabled = user.isEnabled();
		user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "BasicUserDTO [id=" + id + ", email=" + email + ", name=" + name + ", idNumber=" + idNumber
				+ ", enabled=" + enabled + "]";
	}
}