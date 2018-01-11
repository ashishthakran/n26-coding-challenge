package com.n26.coding.challenge.exceptions;

public class ExpiredTimestampException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public ExpiredTimestampException(String message) {
        super(message);
    }

    public static void check(boolean condition, String message, Object... args) {
        if (!condition) {
            throw new ExpiredTimestampException(String.format(message, args));
        }
    }
}
