package com.ordercontrol.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class TextUtils {

	public static boolean isNotEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static boolean isValidEmail(String email) {
		if (isNotEmpty(email)) {
			String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(email).matches();
		}
		return false;
	}

	public static Integer convertToInteger(String str) {
		try {
			if (isNotEmpty(str)) {
				return Integer.parseInt(str);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid integer value: " + str, e);
		}
		return null;
	}

	public static BigDecimal convertToBigDecimal(String str) {
		try {
			if (isNotEmpty(str)) {
				return new BigDecimal(str);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid BigDecimal value: " + str, e);
		}
		return null;
	}
}
