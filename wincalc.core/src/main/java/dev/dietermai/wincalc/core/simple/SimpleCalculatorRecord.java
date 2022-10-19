package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;

public record SimpleCalculatorRecord(String input, Expression expression, Equation equation) {

	private static final Expression INITIAL_EXPRESSION = IdleExpression.of();

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(input, expression, equation);
	}
	
	public static SimpleCalculatorRecord of(Equation equation) {
		return new SimpleCalculatorRecord("", INITIAL_EXPRESSION, equation);
	}

	public static SimpleCalculatorRecord initial() {
		return of("", INITIAL_EXPRESSION, null);
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
}
