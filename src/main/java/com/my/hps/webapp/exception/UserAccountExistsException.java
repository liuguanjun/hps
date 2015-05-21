package com.my.hps.webapp.exception;

public class UserAccountExistsException extends RuntimeException {

	private static final long serialVersionUID = 5291369415056763567L;

	public UserAccountExistsException() {
		super();
	}

	public UserAccountExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserAccountExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountExistsException(String message) {
		super(message);
	}

	public UserAccountExistsException(Throwable cause) {
		super(cause);
	}
	
	

}
