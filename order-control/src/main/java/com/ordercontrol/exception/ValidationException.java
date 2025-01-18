package com.ordercontrol.exception;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String developerMessage;
	private final String clientMessage;

	public ValidationException(String developerMessage, String clientMessage) {
		super(developerMessage);
		this.developerMessage = developerMessage;
		this.clientMessage = clientMessage;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public String getClientMessage() {
		return clientMessage;
	}
}
