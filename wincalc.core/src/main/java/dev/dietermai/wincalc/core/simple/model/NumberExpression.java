package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

public record NumberExpression(BigDecimal value) implements Expression {
	public static final NumberExpression ZERO = of(BigDecimal.ZERO);
	
	public static NumberExpression of(BigDecimal value) {
		return new NumberExpression(value);
	}
	
	public static NumberExpression of(String value) {
		return of(new BigDecimal(value));
	}
	
}
