package com.sjsu.masterproject.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found in database!")
public class RecordNotFoundException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = -8640082899027484243L;

	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
