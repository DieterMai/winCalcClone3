package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;

public record SimpleCalculatorRecord(String input, Expression expression, Equation equation) {

	private static final Expression DEFAULT_EXPRESSION = IdleExpression.of();
	private static final String DEFAULT_INPUT = "";

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(input, expression, equation);
	}
	
	public static SimpleCalculatorRecord of(Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, expression, equation);
	}
	
	public static SimpleCalculatorRecord of(Equation equation) {
		return new SimpleCalculatorRecord("", DEFAULT_EXPRESSION, equation);
	}

	public static SimpleCalculatorRecord initial() {
		return of(DEFAULT_INPUT, DEFAULT_EXPRESSION, null);
	}

	public SimpleCalculatorRecord with(Expression expression) {
		return of(input(), expression, equation());
	}

	public SimpleCalculatorRecord with(Equation equation) {
		return of(input(), expression(), equation);
	}

	public SimpleCalculatorRecord with(String input) {
		return of(input, expression(), equation());
	}
	
	public SimpleCalculatorRecord with(String input, Expression expression, Equation equation) {
		return of(input, expression, equation);
	}
}
