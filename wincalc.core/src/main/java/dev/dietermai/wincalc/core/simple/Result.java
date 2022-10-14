package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public record Result(BigDecimal value, ResolveType type) {
	public static Result of(BigDecimal value) {
		return new Result(value, ResolveType.SUCCESS);
	}
	
	public static Result of(ResolveType type) {
		return new Result(null, type);
	}
	
	public boolean error() {
		return type.isError();
	}
	
	public boolean isSuccess() {
		return type.isSuccess();
	}
}
