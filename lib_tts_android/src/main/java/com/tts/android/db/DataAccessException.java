package com.tts.android.db;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {

	public DataAccessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DataAccessException(String detailMessage) {
		super(detailMessage);
	}

}
