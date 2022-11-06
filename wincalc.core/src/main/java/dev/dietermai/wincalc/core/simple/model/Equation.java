package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

/**
 * Represents an Equation. An Equation is an expression with a resulting value
 * or a resulting Error. If the error is {@link ResultType#OK}, the value is valid.
 * Otherwise, the value can be ignored and may be null.
 */
public record Equation(Expression expression, BigDecimal value, ResultType error) {

	public static Equation of(Expression expression, Result result) {
		return new Equation(expression, result.value(), result.type());
	}

	public static Equation of(BigDecimal value) {
		return new Equation(NumberExpression.of(value), value, ResultType.OK);
	}
}
