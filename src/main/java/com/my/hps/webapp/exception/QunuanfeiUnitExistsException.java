package com.my.hps.webapp.exception;

public class QunuanfeiUnitExistsException  extends RuntimeException {
	private static final long serialVersionUID = 5291369415056763567L;
	public QunuanfeiUnitExistsException() {
		super();
	}

	public QunuanfeiUnitExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QunuanfeiUnitExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public QunuanfeiUnitExistsException(String message) {
		super(message);
	}

	public QunuanfeiUnitExistsException(Throwable cause) {
		super(cause);
	}
}