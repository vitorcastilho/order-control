package com.ordercontrol.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TextUtils")
class TextUtilsTest {

	@Nested
	@DisplayName("isEmpty / isNotEmpty")
	class Emptiness {

		@ParameterizedTest
		@NullSource
		@ValueSource(strings = { "", "   ", "\t", "\n" })
		@DisplayName("trata nulo e texto só com espaço em branco como vazio")
		void blankIsEmpty(String value) {
			assertTrue(TextUtils.isEmpty(value));
			assertFalse(TextUtils.isNotEmpty(value));
		}

		@ParameterizedTest
		@ValueSource(strings = { "a", " a ", "texto" })
		@DisplayName("trata texto com conteúdo como não vazio")
		void contentIsNotEmpty(String value) {
			assertFalse(TextUtils.isEmpty(value));
			assertTrue(TextUtils.isNotEmpty(value));
		}
	}

	@Nested
	@DisplayName("isValidEmail")
	class Email {

		@ParameterizedTest
		@ValueSource(strings = { "a@b.com", "vitor.castilho@empresa.com.br", "nome+tag@dominio.io" })
		@DisplayName("aceita endereços com usuário e domínio")
		void accepts(String email) {
			assertTrue(TextUtils.isValidEmail(email));
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(strings = { "", "   ", "sem-arroba.com", "@dominio.com" })
		@DisplayName("rejeita vazio e endereços sem usuário ou sem arroba")
		void rejects(String email) {
			assertFalse(TextUtils.isValidEmail(email));
		}
	}

	@Nested
	@DisplayName("convertToInteger")
	class ToInteger {

		@Test
		@DisplayName("converte texto numérico")
		void converts() {
			assertEquals(42, TextUtils.convertToInteger("42"));
			assertEquals(-7, TextUtils.convertToInteger("-7"));
		}

		@Test
		@DisplayName("retorna nulo para entrada vazia")
		void returnsNullForBlank() {
			assertNull(TextUtils.convertToInteger(null));
			assertNull(TextUtils.convertToInteger("   "));
		}

		@Test
		@DisplayName("lança IllegalArgumentException para texto não numérico")
		void throwsForInvalid() {
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					() -> TextUtils.convertToInteger("abc"));
			assertTrue(ex.getMessage().contains("abc"));
		}
	}

	@Nested
	@DisplayName("convertToBigDecimal")
	class ToBigDecimal {

		@Test
		@DisplayName("converte texto decimal")
		void converts() {
			assertEquals(new BigDecimal("10.50"), TextUtils.convertToBigDecimal("10.50"));
		}

		@Test
		@DisplayName("retorna nulo para entrada vazia")
		void returnsNullForBlank() {
			assertNull(TextUtils.convertToBigDecimal(null));
			assertNull(TextUtils.convertToBigDecimal(""));
		}

		@Test
		@DisplayName("lança IllegalArgumentException para texto não numérico")
		void throwsForInvalid() {
			assertThrows(IllegalArgumentException.class, () -> TextUtils.convertToBigDecimal("dez"));
		}
	}

	@Test
	@DisplayName("a classe utilitária pode ser instanciada sem efeito colateral")
	void constructorIsAccessible() {
		new TextUtils();
	}
}
