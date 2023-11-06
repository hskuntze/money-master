package br.com.kuntzedev.moneymaster.services.scraping.exceptions;

public class AmazonConnectionException extends RuntimeException {
	private static final long serialVersionUID = 4467148790221083499L;
	
	private Integer status;

	public AmazonConnectionException() {
	}
	
	public AmazonConnectionException(String msg) {
		super(msg);
	}
	
	public AmazonConnectionException(String msg, Integer status) {
		super(msg);
		this.status = status;
	}
	
	public AmazonConnectionException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public AmazonConnectionException(String msg, Throwable cause, Integer status) {
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