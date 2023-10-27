package br.com.kuntzedev.moneymaster.services.exceptions;

public class UserAlreadyEnabledException extends RuntimeException {
	private static final long serialVersionUID = -3831839594942749092L;
	
	public UserAlreadyEnabledException() {
	}

	public UserAlreadyEnabledException(String msg) {
		super(msg);
	}
	
	public UserAlreadyEnabledException(String msg, Throwable cause) {
		super(msg, cause);
	}
}