package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

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
	
	public boolean isSuccess() {
		return type.ok();
	}
}
