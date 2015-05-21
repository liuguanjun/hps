package com.my.hps.webapp.exception;

public class WeixiufeiUnitExistsException extends RuntimeException {
	private static final long serialVersionUID = 5291369415056763567L;
	public WeixiufeiUnitExistsException() {
		super();
	}

	public WeixiufeiUnitExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WeixiufeiUnitExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeixiufeiUnitExistsException(String message) {
		super(message);
	}

	public WeixiufeiUnitExistsException(Throwable cause) {
		super(cause);
	}
}