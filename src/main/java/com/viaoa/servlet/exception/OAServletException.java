package com.viaoa.servlet.exception;

public class OAServletException extends Exception {
	protected int httpStatusCode;

	public OAServletException(String msg, int httpStatus) {
		this(msg, httpStatus, null);
	}

	public OAServletException(String msg, int httpStatus, Exception e) {
		super(msg, e);
		this.httpStatusCode = httpStatus;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}
}
