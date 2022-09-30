package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.Equation;

public record MultiplyExpression(BigDecimal left, BigDecimal right) implements BinaryExpression {
	public static MultiplyExpression of(BigDecimal left, BigDecimal right) {
		return new MultiplyExpression(left, right);
	}

	public static MultiplyExpression of(BigDecimal left) {
		return new MultiplyExpression(left, null);
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
		return Equation.of(this, left.multiply(right));
	}
}
