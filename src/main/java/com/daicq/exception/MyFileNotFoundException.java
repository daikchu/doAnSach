package com.daicq.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * File not found exception
 * 
 * @author Luong To Thanh
 *
 */
@ResponseStatus
public class MyFileNotFoundException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyFileNotFoundException(String message) {
	        super(message);
	    }

	    public MyFileNotFoundException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
