package com.ordercontrol.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseMessage> handleResourceNotFoundException(ResourceNotFoundException exception) {
		ResponseMessage response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), exception.getDeveloperMessage(),
				exception.getClientMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ResponseMessage> handleNoResourceFoundException(NoResourceFoundException exception) {
		ResponseMessage response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage(),
				"Recurso não encontrado. Favor verificar a URL informada.");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseMessage> handleGenericException(Exception exception) {
		ResponseMessage response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(),
				"Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResponseMessage> handleValidationException(ValidationException exception) {
		ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), exception.getDeveloperMessage(),
				exception.getClientMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
