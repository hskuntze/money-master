package br.com.kuntzedev.moneymaster.services.scraping.exceptions;

public class ScrapingConnectionException extends RuntimeException {
	private static final long serialVersionUID = 4467148790221083499L;
	
	private Integer status;

	public ScrapingConnectionException() {
	}
	
	public ScrapingConnectionException(String msg) {
		super(msg);
	}
	
	public ScrapingConnectionException(String msg, Integer status) {
		super(msg);
		this.status = status;
	}
	
	public ScrapingConnectionException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ScrapingConnectionException(String msg, Throwable cause, Integer status) {
		super(msg, cause);
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}