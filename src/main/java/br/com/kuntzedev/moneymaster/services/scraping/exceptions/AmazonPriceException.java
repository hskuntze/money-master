package br.com.kuntzedev.moneymaster.services.scraping.exceptions;

public class AmazonPriceException extends RuntimeException {
	private static final long serialVersionUID = -1140012934079301324L;

	public AmazonPriceException() {
	}
	
	public AmazonPriceException(String msg) {
		super(msg);
	}
	
	public AmazonPriceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}