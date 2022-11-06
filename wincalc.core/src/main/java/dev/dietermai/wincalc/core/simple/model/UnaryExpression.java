package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

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

//	public static UnaryExpression of(BigDecimal value) {
//		return new UnaryExpression(UnaryOperator.identity, NumberExpression.of(value));
//	}
//
//	public static UnaryExpression of(String value) {
//		return new UnaryExpression(UnaryOperator.identity, NumberExpression.of(value));
//	}
}
