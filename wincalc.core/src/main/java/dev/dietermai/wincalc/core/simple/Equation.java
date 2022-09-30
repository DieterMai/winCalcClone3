package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

public record Equation(Expression expression, BigDecimal value, ResolveType type) {
	
	public static Equation of(Expression expression, BigDecimal value) {
		return new Equation(expression, value, ResolveType.SUCCESS);
	}
	
	public static Equation of(Expression expression, ResolveType type) {
		return new Equation(expression, null, type);
	}
}
