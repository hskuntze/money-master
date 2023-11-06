package br.com.kuntzedev.moneymaster.entities.exceptions;

public class InvalidValueException extends RuntimeException {
	private static final long serialVersionUID = -6767096286851667756L;

	public InvalidValueException() {
	}
	
	public InvalidValueException(String msg) {
		super(msg);
	}
	
	public InvalidValueException(String msg, Throwable cause) {
		super(msg, cause);
	}
}