package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

public enum BiOperator {
	plus, minus;

	private BiOperator() {
	}

	public BinaryExpression of(BigDecimal left) {
		return switch (this) {
		case plus -> PlusExpression.of(left);
		case minus -> MinusExpression.of(left);
		};
	}

	public BinaryExpression of(BigDecimal left, BigDecimal right) {
		return switch (this) {
		case plus -> PlusExpression.of(left, right);
		case minus -> MinusExpression.of(left, right);
		};
	}
}
