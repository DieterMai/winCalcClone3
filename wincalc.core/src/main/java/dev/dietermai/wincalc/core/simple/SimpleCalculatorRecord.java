package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;

public record SimpleCalculatorRecord(String input, Expression expression, Equation equation, ResolveType lastResolve) {

	private static final Expression DEFAULT_EXPRESSION = IdleExpression.of();
	private static final String DEFAULT_INPUT = "";
	private static final Equation DEFAULT_EQUATION = null;
	private static final ResolveType DEFAULT_STATE = ResolveType.SUCCESS;

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(input, expression, equation, lastResolve);
	}

	public static SimpleCalculatorRecord of(String input, Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(input, expression, equation, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(String input, Expression expression, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(input, expression, DEFAULT_EQUATION, lastResolve);
	}

	public static SimpleCalculatorRecord of(String input, Equation equation, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(input, DEFAULT_EXPRESSION, equation, lastResolve);
	}

	public static SimpleCalculatorRecord of(Expression expression, Equation equation, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, expression, equation, lastResolve);
	}

	public static SimpleCalculatorRecord of(String input, Expression expression) {
		return new SimpleCalculatorRecord(input, expression, DEFAULT_EQUATION, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(String input, Equation equation) {
		return new SimpleCalculatorRecord(input, DEFAULT_EXPRESSION, equation, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(Expression expression, Equation equation) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, expression, equation, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(String input, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(input, DEFAULT_EXPRESSION, DEFAULT_EQUATION, lastResolve);
	}

	public static SimpleCalculatorRecord of(Expression expression, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, expression, DEFAULT_EQUATION, lastResolve);
	}

	public static SimpleCalculatorRecord of(Equation equation, ResolveType lastResolve) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, DEFAULT_EXPRESSION, equation, lastResolve);
	}

	public static SimpleCalculatorRecord of(String input) {
		return new SimpleCalculatorRecord(input, DEFAULT_EXPRESSION, DEFAULT_EQUATION, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(Expression newExpression) {
		return of(DEFAULT_INPUT, newExpression, DEFAULT_EQUATION, DEFAULT_STATE);
	}

	public static SimpleCalculatorRecord of(Equation equation) {
		return new SimpleCalculatorRecord(DEFAULT_INPUT, DEFAULT_EXPRESSION, equation, equation.type());
	}

	public static SimpleCalculatorRecord of() {
		return of(DEFAULT_INPUT, DEFAULT_EXPRESSION, DEFAULT_EQUATION, DEFAULT_STATE);
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
