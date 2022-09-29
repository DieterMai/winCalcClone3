package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

public record Equation(Expression expression, BigDecimal value) {
	public static Equation of(Expression expression, BigDecimal value) {
		return new Equation(expression, value);
	}
}
