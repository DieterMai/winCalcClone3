package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public record SimpleEquation(SimpleExpression expression, BigDecimal value) {
	public static SimpleEquation of(SimpleExpression expression, BigDecimal value) {
		return new SimpleEquation(expression, value);
	}
}
