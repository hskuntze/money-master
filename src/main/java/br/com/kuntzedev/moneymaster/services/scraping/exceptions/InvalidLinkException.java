package br.com.kuntzedev.moneymaster.services.scraping.exceptions;

public class InvalidLinkException extends RuntimeException {
	private static final long serialVersionUID = -6579535450840137369L;

	public InvalidLinkException() {
	}
	
	public InvalidLinkException(String msg) {
		super(msg);
	}
	
	public InvalidLinkException(String msg, Throwable cause) {
		super(msg, cause);
	}
}