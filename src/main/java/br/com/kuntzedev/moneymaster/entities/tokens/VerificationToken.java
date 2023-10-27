package br.com.kuntzedev.moneymaster.entities.tokens;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.kuntzedev.moneymaster.entities.User;

@Entity
@Table(name = "tb_verification_token")
public class VerificationToken extends AbstractToken {

	public VerificationToken() {
	}
	
	public VerificationToken(User user, String token) {
		this.setToken(token);
		this.setUser(user);
		this.setExpiryDate(this.calculateExpiryDate(getExpiration()));
	}
}