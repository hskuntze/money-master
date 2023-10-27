package br.com.kuntzedev.moneymaster.services.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 6943916297103357565L;
	
	public UserAlreadyExistsException() {
	}

	public UserAlreadyExistsException(String msg) {
		super(msg);
	}
	
	public UserAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}