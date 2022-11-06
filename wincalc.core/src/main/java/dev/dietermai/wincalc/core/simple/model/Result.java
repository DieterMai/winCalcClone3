package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

public record Result(BigDecimal value, Error type) {
	public static final Result ZERO = of(BigDecimal.ZERO);
	
	public static Result of(BigDecimal value) {
		return new Result(value, Error.NONE);
	}
	
	public static Result of(Error type) {
		return new Result(null, type);
	}
	
	public boolean error() {
		return type.isError();
	}
	
	public boolean isSuccess() {
		return type.none();
	}
}
