package dev.dietermai.wincalc.core.simple.expr;

import java.math.BigDecimal;

public final record UnaryExpression(UnaryOperator operator, BigDecimal value) implements Expression{
	public static UnaryExpression of(UnaryOperator operator, BigDecimal value) {
		return new UnaryExpression(operator, value);
	}
	
	public static UnaryExpression of(BigDecimal value) {
		return new UnaryExpression(UnaryOperator.identity, value);
	}
	
	public static UnaryExpression of(String value) {
		return new UnaryExpression(UnaryOperator.identity, new BigDecimal(value));
	}
}
