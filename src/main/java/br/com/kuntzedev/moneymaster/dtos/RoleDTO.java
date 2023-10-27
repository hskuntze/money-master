package br.com.kuntzedev.moneymaster.dtos;

import java.io.Serializable;

import br.com.kuntzedev.moneymaster.entities.Role;

public class RoleDTO implements Serializable {
	private static final long serialVersionUID = -3471858915503309355L;
	
	private Long id;
	private String authority;
	
	public RoleDTO() {
	}
	
	public RoleDTO(Role role) {
		this.id = role.getId();
		this.authority = role.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "RoleDTO [id=" + id + ", authority=" + authority + "]";
	}
}