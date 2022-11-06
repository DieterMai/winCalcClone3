package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

public record Equation(Expression expression, BigDecimal value, ErrorType type) {
	public static Equation of(Expression expression, Result result) {
		return new Equation(expression, result.value(), result.type());
	}
	
	public static Equation of(BigDecimal value) {
		return new Equation(NumberExpression.of(value), value, ErrorType.NONE);
	}
	
}
