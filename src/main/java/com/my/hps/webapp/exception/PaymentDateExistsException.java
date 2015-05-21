package com.my.hps.webapp.exception;

public class PaymentDateExistsException extends RuntimeException {
	public PaymentDateExistsException() {
		super();
	}

	public PaymentDateExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PaymentDateExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentDateExistsException(String message) {
		super(message);
	}

	public PaymentDateExistsException(Throwable cause) {
		super(cause);
	}
}
