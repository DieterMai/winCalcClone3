package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.Equation;

public record MinusExpression(BigDecimal left, BigDecimal right) implements BinaryExpression {
	public static MinusExpression of(BigDecimal left, BigDecimal right) {
		return new MinusExpression(left, right);
	}

	public static MinusExpression of(BigDecimal left) {
		return new MinusExpression(left, null);
	}

	@Override
	public BinaryExpression withRight(BigDecimal localRight) {
		return of(left, localRight);
	}

	@Override
	public Equation resolve() {
		if (right == null) {
			return of(left, left).resolve();
		}
		return Equation.of(this, left.subtract(right));
	}
}
