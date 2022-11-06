package dev.dietermai.wincalc.core.simple.model;

import java.math.BigDecimal;

/**
 * Represents a binary expression. A binary expression is an expression that has
 * a binary operator and two nested expressions around it.
 */
public record BinaryExpression(Expression left, BiOperator operator, Expression right) implements Expression {

	private static final Expression DEFAULT_RIGHT_EXPRESSION = null;

	/* ------------------------------------ */
	/* Factory method with all three fields */
	/* ------------------------------------ */
	public static BinaryExpression of(BigDecimal left, BiOperator operator, BigDecimal right) {
		return new BinaryExpression(NumberExpression.of(left), operator, NumberExpression.of(right));
	}

	public static BinaryExpression of(BigDecimal left, BiOperator operator, Expression right) {
		return new BinaryExpression(NumberExpression.of(left), operator, right);
	}

	public static BinaryExpression of(String left, BiOperator operator, String right) {
		return new BinaryExpression(NumberExpression.of(left), operator, NumberExpression.of(right));
	}

	public static BinaryExpression of(String left, BiOperator operator, Expression right) {
		return new BinaryExpression(NumberExpression.of(left), operator, right);
	}

	/* ------------------------------ */
	/* Factory method with two fields */
	/* ------------------------------ */
	public static BinaryExpression of(BigDecimal left, BiOperator operator) {
		return new BinaryExpression(NumberExpression.of(left), operator, DEFAULT_RIGHT_EXPRESSION);
	}

	public static BinaryExpression of(String left, BiOperator operator) {
		return new BinaryExpression(NumberExpression.of(left), operator, DEFAULT_RIGHT_EXPRESSION);
	}

	/* ----------------- */
	/* Withers one field */
	/* ----------------- */
	public BinaryExpression withRight(Expression right) {
		return new BinaryExpression(left(), operator(), right);
	}

	public BinaryExpression withRight(BigDecimal right) {
		return new BinaryExpression(left(), operator(), NumberExpression.of(right));
	}

	public BinaryExpression withRight(String right) {
		return new BinaryExpression(left(), operator(), NumberExpression.of(right));
	}

	public BinaryExpression withLeft(Expression left) {
		return new BinaryExpression(left, operator(), right());
	}

	public BinaryExpression withLeft(BigDecimal left) {
		return new BinaryExpression(NumberExpression.of(left), operator(), right());
	}

	public BinaryExpression withLeft(String left) {
		return new BinaryExpression(NumberExpression.of(left), operator(), right());
	}

	public BinaryExpression with(BiOperator operator) {
		return new BinaryExpression(left(), operator, right());
	}

	/**
	 * Returns if the expression is complete, meaning left and right terms are not
	 * null
	 * 
	 * @return true if complete, false otherwise.
	 */
	public boolean isComplete() {
		return left != null && right != null;
	}
}
