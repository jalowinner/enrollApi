package com.dingren.enrolldemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class noAccessException extends RuntimeException {
	public noAccessException(String message) {
		super(message);
	}
}