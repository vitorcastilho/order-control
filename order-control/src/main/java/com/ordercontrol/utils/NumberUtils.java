package com.ordercontrol.utils;

import java.math.BigDecimal;

public class NumberUtils {

	public static boolean isValidId(Long id) {
		return id != null && id != 0L;
	}

	public static boolean isInvalidId(Long id) {
		return id == null || id == 0L;
	}

	public static boolean isPositiveNumber(Number number) {
		return number != null && number.doubleValue() > 0;
	}

	public static boolean isWithinRange(Number number, Number min, Number max) {
		if (number != null && min != null && max != null) {
			double numValue = number.doubleValue();
			return numValue >= min.doubleValue() && numValue <= max.doubleValue();
		}
		return false;
	}

	public static Long convertToLong(Integer number) {
		return (number != null) ? number.longValue() : null;
	}

	public static BigDecimal convertToBigDecimal(Double number) {
		return (number != null) ? BigDecimal.valueOf(number) : null;
	}
}
