package br.com.kuntzedev.moneymaster.services.scraping.exceptions;

public class ScrapingPriceException extends RuntimeException {
	private static final long serialVersionUID = -1140012934079301324L;

	public ScrapingPriceException() {
	}
	
	public ScrapingPriceException(String msg) {
		super(msg);
	}
	
	public ScrapingPriceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}