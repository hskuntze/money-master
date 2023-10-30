package br.com.kuntzedev.moneymaster.services.exceptions;

public class DeletionProcedureException extends RuntimeException {
	private static final long serialVersionUID = 1898148461540508129L;

	public DeletionProcedureException() {
	}
	
	public DeletionProcedureException(String msg) {
		super(msg);
	}
	
	public DeletionProcedureException(String msg, Throwable cause) {
		super(msg, cause);
	}
}