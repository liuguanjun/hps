package com.my.hps.webapp.exception;

public class PaymentDateNotExistsException extends RuntimeException {
	public PaymentDateNotExistsException() {
		super();
	}

	public PaymentDateNotExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PaymentDateNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentDateNotExistsException(String message) {
		super(message);
	}

	public PaymentDateNotExistsException(Throwable cause) {
		super(cause);
	}
}
