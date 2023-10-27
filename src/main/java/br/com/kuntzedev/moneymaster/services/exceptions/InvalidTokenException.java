package br.com.kuntzedev.moneymaster.services.exceptions;

public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = -2917101552165682558L;

	public InvalidTokenException() {
	}
	
	public InvalidTokenException(String msg) {
		super(msg);
	}
	
	public InvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}
}