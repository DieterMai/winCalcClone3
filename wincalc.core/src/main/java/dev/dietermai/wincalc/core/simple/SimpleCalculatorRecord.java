package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;

public record SimpleCalculatorRecord(Expression expression, Equation equation) {
	private static final Expression INITIAL_EXPRESSION = IdleExpression.of();
	
	public static SimpleCalculatorRecord of(Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(expression, equation);
	}
	
	public static SimpleCalculatorRecord initial() {
		return of(INITIAL_EXPRESSION, null);
	}
	
	public SimpleCalculatorRecord withExpression(Expression expression) {
		return of(expression, equation());
	}
	
	public SimpleCalculatorRecord withEquation(Equation equation) {
		return of(expression(), equation);
	}
}
