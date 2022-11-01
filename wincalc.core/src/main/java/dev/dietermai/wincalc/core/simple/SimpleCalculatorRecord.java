package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;

public record SimpleCalculatorRecord(String input, Expression expression, Equation equation, ResolveType lastResolve) {

	private static final Expression DEFAULT_EXPRESSION = IdleExpression.of();
	private static final String DEFAULT_INPUT = "";
	private static final ResolveType DEFAULT_STATE = ResolveType.SUCCESS;

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(input, expression, equation, lastResolve);
	}

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(input, expression, equation, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, expression, equation, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(Equation equation) {
		return new SimpleCalculatorRecord("", DEFAULT_EXPRESSION, equation, equation.type());
	}

	public static SimpleCalculatorRecord of(Equation equation, ResolveType type) {
		return new SimpleCalculatorRecord("", DEFAULT_EXPRESSION, equation, type);
	}

	public static SimpleCalculatorRecord initial() {
		return of(DEFAULT_INPUT, DEFAULT_EXPRESSION, null);
	}

	public static SimpleCalculatorRecord of(Expression newExpression, ResolveType type) {
		return of(DEFAULT_INPUT, newExpression, null, type);
	}
	
	public static SimpleCalculatorRecord of(Expression newExpression) {
		return of(DEFAULT_INPUT, newExpression, null, DEFAULT_STATE);
	}

	public SimpleCalculatorRecord with(Expression expression) {
		return of(input(), expression, equation());
	}

	public SimpleCalculatorRecord with(Expression expression, ResolveType lastResolve) {
		return of(input(), expression, equation(), lastResolve);
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
