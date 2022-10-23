package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;

public record BinaryExpression(Expression left, BiOperator operator, Expression right) implements Expression {
	public static BinaryExpression of(BigDecimal left, BiOperator operator) {
		return new BinaryExpression(NumberExpression.of(left), operator, null);
	}
	
	public static BinaryExpression of(String left, BiOperator operator) {
		return new BinaryExpression(NumberExpression.of(left), operator, null);
	}

	public static BinaryExpression of(BigDecimal left, BiOperator operator, BigDecimal right) {
		return new BinaryExpression(NumberExpression.of(left), operator, NumberExpression.of(right));
	}
	
	public static BinaryExpression of(Expression left, BiOperator operator, BigDecimal right) {
		return new BinaryExpression(left, operator, NumberExpression.of(right));
	}
	
	public static BinaryExpression of(BigDecimal left, BiOperator operator, Expression right) {
		return new BinaryExpression(NumberExpression.of(left), operator, right);
	}

	public BinaryExpression withRight(Expression right) {
		return new BinaryExpression(left(), operator(), right);
	}
	
	public BinaryExpression withRight(BigDecimal right) {
		return new BinaryExpression(left(), operator(), NumberExpression.of(right));
	}
	
	public BinaryExpression withRight(String right) {
		return new BinaryExpression(left(), operator(), NumberExpression.of(right));
	}
	
	public BinaryExpression withLeft(BigDecimal left) {
		return new BinaryExpression(NumberExpression.of(left), operator(), right());
	}
	
	public BinaryExpression withLeft(Expression left) {
		return new BinaryExpression(left, operator(), right());
	}

	public BinaryExpression with(BiOperator operator) {
		return new BinaryExpression(left(), operator, right());
	}
	
	public boolean isComplete() {
		return left != null && right != null;
	}
}
