package com.ordercontrol.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("NumberUtils")
class NumberUtilsTest {

	@Nested
	@DisplayName("isValidId / isInvalidId")
	class IdValidation {

		@ParameterizedTest
		@ValueSource(longs = { 1L, -5L, Long.MAX_VALUE })
		@DisplayName("considera válido qualquer id não nulo e diferente de zero")
		void acceptsNonZeroIds(long id) {
			assertTrue(NumberUtils.isValidId(id));
			assertFalse(NumberUtils.isInvalidId(id));
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(longs = { 0L })
		@DisplayName("considera inválido id nulo ou zero")
		void rejectsNullAndZero(Long id) {
			assertFalse(NumberUtils.isValidId(id));
			assertTrue(NumberUtils.isInvalidId(id));
		}
	}

	@Nested
	@DisplayName("isPositiveNumber")
	class PositiveNumber {

		@Test
		@DisplayName("aceita valores maiores que zero em tipos diferentes")
		void acceptsPositive() {
			assertTrue(NumberUtils.isPositiveNumber(1));
			assertTrue(NumberUtils.isPositiveNumber(0.01));
			assertTrue(NumberUtils.isPositiveNumber(new BigDecimal("0.5")));
		}

		@Test
		@DisplayName("rejeita zero, negativo e nulo")
		void rejectsZeroNegativeAndNull() {
			assertFalse(NumberUtils.isPositiveNumber(0));
			assertFalse(NumberUtils.isPositiveNumber(-1));
			assertFalse(NumberUtils.isPositiveNumber(null));
		}
	}

	@Nested
	@DisplayName("isWithinRange")
	class WithinRange {

		@Test
		@DisplayName("inclui os dois extremos do intervalo")
		void includesBoundaries() {
			assertTrue(NumberUtils.isWithinRange(1, 1, 10));
			assertTrue(NumberUtils.isWithinRange(10, 1, 10));
			assertTrue(NumberUtils.isWithinRange(5, 1, 10));
		}

		@Test
		@DisplayName("rejeita valores fora do intervalo")
		void rejectsOutside() {
			assertFalse(NumberUtils.isWithinRange(0, 1, 10));
			assertFalse(NumberUtils.isWithinRange(11, 1, 10));
		}

		@Test
		@DisplayName("retorna falso quando qualquer argumento é nulo")
		void rejectsNullArguments() {
			assertFalse(NumberUtils.isWithinRange(null, 1, 10));
			assertFalse(NumberUtils.isWithinRange(5, null, 10));
			assertFalse(NumberUtils.isWithinRange(5, 1, null));
		}
	}

	@Nested
	@DisplayName("conversões")
	class Conversions {

		@Test
		@DisplayName("convertToLong preserva o valor e propaga nulo")
		void convertToLong() {
			assertEquals(7L, NumberUtils.convertToLong(7));
			assertNull(NumberUtils.convertToLong(null));
		}

		@Test
		@DisplayName("convertToBigDecimal preserva o valor e propaga nulo")
		void convertToBigDecimal() {
			assertEquals(BigDecimal.valueOf(2.5), NumberUtils.convertToBigDecimal(2.5));
			assertNull(NumberUtils.convertToBigDecimal(null));
		}
	}

	@Test
	@DisplayName("a classe utilitária pode ser instanciada sem efeito colateral")
	void constructorIsAccessible() {
		new NumberUtils();
	}
}
