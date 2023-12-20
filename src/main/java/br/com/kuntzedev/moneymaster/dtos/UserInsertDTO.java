package br.com.kuntzedev.moneymaster.dtos;

import br.com.kuntzedev.moneymaster.entities.User;

public class UserInsertDTO extends UserSimpleRegisterDTO {
	private static final long serialVersionUID = 5734701072206258380L;

	private String password;
	
	public UserInsertDTO() {
	}
	
	public UserInsertDTO(User user) {
		super(user);
		
		this.password = user.getPassword();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "This class can't be stringfied for security purpouses.";
	}
}