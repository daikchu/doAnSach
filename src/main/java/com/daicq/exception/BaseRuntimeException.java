package com.daicq.exception;

/**
 * Api base runtime exception
 * 
 * @author Luong To Thanh
 *
 */
public abstract class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BaseRuntimeException(String message) {
		super(message);
	}

	public abstract Integer getCode();

}
