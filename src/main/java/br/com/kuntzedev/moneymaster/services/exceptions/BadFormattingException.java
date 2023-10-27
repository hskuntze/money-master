package br.com.kuntzedev.moneymaster.services.exceptions;

public class BadFormattingException extends RuntimeException {
	private static final long serialVersionUID = 1323672888116808870L;
	
	public BadFormattingException() {
	}

	public BadFormattingException(String msg) {
		super(msg);
	}
	
	public BadFormattingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}