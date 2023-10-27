package br.com.kuntzedev.moneymaster.services.exceptions;

public class UnprocessableRequestException extends RuntimeException {
	private static final long serialVersionUID = -6713721995825191259L;
	
	public UnprocessableRequestException() {
	}

	public UnprocessableRequestException(String msg) {
		super(msg);
	}
	
	public UnprocessableRequestException(String msg, Throwable cause) {
		super(msg, cause);
	}
}