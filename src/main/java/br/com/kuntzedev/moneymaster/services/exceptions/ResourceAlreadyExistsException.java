package br.com.kuntzedev.moneymaster.services.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 4993997104412407918L;

	public ResourceAlreadyExistsException() {
	}
	
	public ResourceAlreadyExistsException(String msg) {
		super(msg);
	}
	
	public ResourceAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}