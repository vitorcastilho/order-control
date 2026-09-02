package com.ordercontrol.infrastructure.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	@DisplayName("ResourceNotFoundException vira 404 preservando as duas mensagens")
	void resourceNotFound() {
		ResponseEntity<ResponseMessage> response = handler
				.handleResourceNotFoundException(new ResourceNotFoundException("dev", "cliente"));

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(404, response.getBody().getStatus());
		assertEquals("dev", response.getBody().getDeveloperMessage());
		assertEquals("cliente", response.getBody().getClientMessage());
	}

	@Test
	@DisplayName("ValidationException vira 400 preservando as duas mensagens")
	void validation() {
		ResponseEntity<ResponseMessage> response = handler
				.handleValidationException(new ValidationException("dev", "cliente"));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(400, response.getBody().getStatus());
		assertEquals("dev", response.getBody().getDeveloperMessage());
		assertEquals("cliente", response.getBody().getClientMessage());
	}

	@Test
	@DisplayName("exceção não mapeada vira 500 com mensagem genérica ao cliente")
	void generic() {
		ResponseEntity<ResponseMessage> response = handler
				.handleGenericException(new IllegalStateException("falha inesperada"));

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(500, response.getBody().getStatus());
		assertEquals("falha inesperada", response.getBody().getDeveloperMessage());
		assertEquals("Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.",
				response.getBody().getClientMessage());
	}

	@Test
	@DisplayName("ResponseMessage expõe os valores atribuídos pelos setters")
	void responseMessageAccessors() {
		ResponseMessage message = new ResponseMessage(200, "d", "c");
		message.setStatus(201);
		message.setDeveloperMessage("dev2");
		message.setClientMessage("cli2");

		assertEquals(201, message.getStatus());
		assertEquals("dev2", message.getDeveloperMessage());
		assertEquals("cli2", message.getClientMessage());
	}

	@Test
	@DisplayName("as exceções de domínio carregam mensagem de desenvolvedor e de cliente")
	void exceptionsCarryBothMessages() {
		ResourceNotFoundException notFound = new ResourceNotFoundException("dev", "cli");
		ValidationException validation = new ValidationException("dev", "cli");

		assertEquals("dev", notFound.getMessage());
		assertEquals("dev", notFound.getDeveloperMessage());
		assertEquals("cli", notFound.getClientMessage());
		assertEquals("dev", validation.getMessage());
		assertEquals("dev", validation.getDeveloperMessage());
		assertEquals("cli", validation.getClientMessage());
	}
}
