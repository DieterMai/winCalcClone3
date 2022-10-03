package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

public record BinaryExpression(BigDecimal left, BiOperator operator, BigDecimal right) implements Expression {
	public static BinaryExpression of(BigDecimal left, BiOperator operator) {
		return new BinaryExpression(left, operator, null);
	}

	public static Expression of(BigDecimal left, BiOperator operator, BigDecimal right) {
		return new BinaryExpression(left, operator, right);
	}

	public BinaryExpression withRight(BigDecimal right) {
		return new BinaryExpression(left(), operator(), right);
	}

	public BinaryExpression withOperator(BiOperator operator) {
		return new BinaryExpression(left(), operator, right());
	}
}
