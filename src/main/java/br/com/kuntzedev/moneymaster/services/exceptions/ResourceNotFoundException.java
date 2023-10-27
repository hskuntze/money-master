package br.com.kuntzedev.moneymaster.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6016576005960652003L;
	
	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
	
	public ResourceNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}