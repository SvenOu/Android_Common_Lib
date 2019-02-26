package com.tts.android.common.exception;

public class NullContextException extends RuntimeException {

	private static final long serialVersionUID = 1637298742131918707L;

	public NullContextException() {

	}

	public NullContextException(String detailMessage) {
		super(detailMessage);
	}

	public NullContextException(Throwable throwable) {
		super(throwable);
	}

	public NullContextException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
