package com.my.hps.webapp.exception;

public class LouzuoExistsException extends RuntimeException {
	private static final long serialVersionUID = 5291369415056763567L;

	public LouzuoExistsException() {
		super();
	}

	public LouzuoExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LouzuoExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LouzuoExistsException(String message) {
		super(message);
	}

	public LouzuoExistsException(Throwable cause) {
		super(cause);
	}
}
