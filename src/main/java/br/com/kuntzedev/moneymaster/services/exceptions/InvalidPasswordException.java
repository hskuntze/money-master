package br.com.kuntzedev.moneymaster.services.exceptions;

public class InvalidPasswordException extends Exception {
	private static final long serialVersionUID = -142866188209110692L;
	
	public InvalidPasswordException() {
	}
	
	public InvalidPasswordException(String msg) {
		super(msg);
	}
	
	public InvalidPasswordException(String msg, Throwable cause) {
		super(msg, cause);
	}
}