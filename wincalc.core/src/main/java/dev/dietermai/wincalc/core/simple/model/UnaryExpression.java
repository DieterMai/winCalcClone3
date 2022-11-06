package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

/**
 * An unary expression is an operator that is only applied to one expression.
 */
public final record UnaryExpression(UnaryOperator operator, Expression nested) implements Expression {
	public static UnaryExpression of(UnaryOperator operator, BigDecimal value) {
		return new UnaryExpression(operator, NumberExpression.of(value));
	}

	public static UnaryExpression of(UnaryOperator operator, String value) {
		return new UnaryExpression(operator, NumberExpression.of(value));
	}

	public static UnaryExpression of(UnaryOperator operator, Expression nested) {
		return new UnaryExpression(operator, nested);
	}
}
