package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

public record BinaryExpression(BigDecimal left, BigDecimal right, BiOperator operator) implements Expression
{
	public static BinaryExpression of(BigDecimal left, BiOperator operator) {
		return new BinaryExpression(left, null, operator);
	}

	public static Expression of(BiOperator operator, BigDecimal left, BigDecimal right) {
		return new BinaryExpression(left, right, operator);
	}
	
	public BinaryExpression withRight(BigDecimal right) {
		return new BinaryExpression(left(), right, operator());
	}
	
	public BinaryExpression withOperator(BiOperator operator) {
		return new BinaryExpression(left(), right(), operator);
	}
}
