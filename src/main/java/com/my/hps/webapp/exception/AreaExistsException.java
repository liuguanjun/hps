package com.my.hps.webapp.exception;

public class AreaExistsException  extends RuntimeException {

	private static final long serialVersionUID = 5291369415056763567L;

	public AreaExistsException() {
		super();
	}

	public AreaExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AreaExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AreaExistsException(String message) {
		super(message);
	}

	public AreaExistsException(Throwable cause) {
		super(cause);
	}
	
	

}
