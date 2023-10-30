package br.com.kuntzedev.moneymaster.dtos.tokens;

import java.io.Serializable;

public class TokenPasswordDTO implements Serializable{
	private static final long serialVersionUID = -199483668652026959L;
	
	private String token;
	private String password;
	
	public TokenPasswordDTO() {
	}

	public TokenPasswordDTO(String token, String password) {
		this.token = token;
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}