package com.ordercontrol.exception;

public class ResponseMessage {

	private int status;
	private String developerMessage;
	private String clientMessage;

	public ResponseMessage(int status, String developerMessage, String clientMessage) {
		this.status = status;
		this.developerMessage = developerMessage;
		this.clientMessage = clientMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getClientMessage() {
		return clientMessage;
	}

	public void setClientMessage(String clientMessage) {
		this.clientMessage = clientMessage;
	}
}
