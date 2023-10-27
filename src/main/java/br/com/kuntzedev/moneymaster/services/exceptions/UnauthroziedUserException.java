package br.com.kuntzedev.moneymaster.services.exceptions;

public class UnauthroziedUserException extends RuntimeException {
	private static final long serialVersionUID = -3337695497310478569L;
	
	public UnauthroziedUserException() {
	}

	public UnauthroziedUserException(String msg) {
		super(msg);
	}
	
	public UnauthroziedUserException(String msg, Throwable cause) {
		super(msg, cause);
	}
}