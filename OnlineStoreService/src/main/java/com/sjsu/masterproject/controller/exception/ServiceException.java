package com.sjsu.masterproject.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author ruchira If the HTTP request gets exception as NOT FOUND
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Exception in OnlineStoreService!")
public class ServiceException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1362251344289146233L;

	public ServiceException(String msg) {
		super(msg);
	}
}
