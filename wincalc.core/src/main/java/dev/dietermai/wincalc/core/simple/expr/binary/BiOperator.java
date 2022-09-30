package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

public enum BiOperator {
	plus, minus, multiply, divide;

	private BiOperator() {
	}

	public BinaryExpression of(BigDecimal left) {
		return switch (this) {
		case plus -> PlusExpression.of(left);
		case minus -> MinusExpression.of(left);
		case multiply -> MultiplyExpression.of(left);
		case divide -> DivideExpression.of(left);
		};
	}

	public BinaryExpression of(BigDecimal left, BigDecimal right) {
		return switch (this) {
		case plus -> PlusExpression.of(left, right);
		case minus -> MinusExpression.of(left, right);
		case multiply -> MultiplyExpression.of(left,right);
		case divide -> DivideExpression.of(left, right);
		};
	}
}
