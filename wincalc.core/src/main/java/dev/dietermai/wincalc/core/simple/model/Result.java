package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

public record Result(BigDecimal value, ErrorType type) {
	public static final Result ZERO = of(BigDecimal.ZERO);
	
	public static Result of(BigDecimal value) {
		return new Result(value, ErrorType.NONE);
	}
	
	public static Result of(ErrorType type) {
		return new Result(null, type);
	}
	
	public boolean error() {
		return type.isError();
	}
	
	public boolean isSuccess() {
		return type.none();
	}
}
