package br.com.kuntzedev.moneymaster.services.exceptions;

public class EmailException extends RuntimeException {
	private static final long serialVersionUID = -2726588630322727390L;
	
	public EmailException() {
	}

	public EmailException(String msg) {
		super(msg);
	}
	
	public EmailException(String msg, Throwable cause) {
		super(msg, cause);
	}
}