package com.daicq.exception;

/**
 * Api Error Type Enum
 *
 * @author Luong To Thanh
 */
public enum ErrorType {
	USER_NOT_FOUND(404, "not found"), USER_ALREADY_EXISTS(400, "already exists");

	private final int code;
	private final String message;

	private ErrorType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
