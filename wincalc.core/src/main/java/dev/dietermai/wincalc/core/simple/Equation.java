package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;

public record Equation(Expression expression, BigDecimal value, ResolveType type) {
	public static Equation of(Expression expression, Result result) {
		return new Equation(expression, result.value(), result.type());
	}
	
	public static Equation of(BigDecimal value) {
		return new Equation(NumberExpression.of(value), value, ResolveType.SUCCESS);
	}
	
}
