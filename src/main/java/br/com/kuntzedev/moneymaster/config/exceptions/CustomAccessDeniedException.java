package br.com.kuntzedev.moneymaster.config.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {	
	private static final long serialVersionUID = 603378848408825285L;

	public CustomAccessDeniedException(String msg) {
		super(msg);
	}

	public CustomAccessDeniedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}