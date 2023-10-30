package br.com.kuntzedev.moneymaster.entities.tokens;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.kuntzedev.moneymaster.entities.User;

@Entity
@Table(name = "tb_recovery_token")
public class PasswordRecoveryToken extends AbstractToken {

	public PasswordRecoveryToken() {
	}
	
	public PasswordRecoveryToken(Long id, String token, User user, Date expiryDate) {
		this.setId(id);
		this.setToken(token);
		this.setUser(user);
		this.setExpiryDate(expiryDate);
	}
	
	public PasswordRecoveryToken(User user, String token) {
		this.setToken(token);
		this.setUser(user);
		this.setExpiryDate(calculateExpiryDate(getExpiration()));
	}
}