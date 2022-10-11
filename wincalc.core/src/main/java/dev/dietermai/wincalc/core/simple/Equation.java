package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

// TODO merge value and type to Result
public record Equation(Expression expression, BigDecimal value, ResolveType type) {
	
	// TODO re-add this method for convenience 
//	public static Equation of(Expression expression, BigDecimal value) {
//		return new Equation(expression, value, ResolveType.SUCCESS);
//	}
	
	// TODO re-add this method for convenience 
//	public static Equation of(Expression expression, ResolveType type) {
//		return new Equation(expression, null, type);
//	}
	
	public static Equation of(Expression expression, Result result) {
		return new Equation(expression, result.value(), result.type());
	}
}
