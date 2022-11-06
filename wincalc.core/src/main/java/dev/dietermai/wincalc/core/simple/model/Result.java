package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

/**
 * A Result is represents the result of an resolved expression. If the
 * ResultType is OK, the the value is non null. If the result type is not OK,
 * then the value can be ignored and my be null.
 */
public record Result(BigDecimal value, ResultType type) {
	public static final Result ZERO = of(BigDecimal.ZERO);

	public static Result of(BigDecimal value) {
		return new Result(value, ResultType.OK);
	}

	public static Result of(ResultType type) {
		return new Result(null, type);
	}

	public boolean error() {
		return type.error();
	}

	public boolean isOK() {
		return type.ok();
	}
}
