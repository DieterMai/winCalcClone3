package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.Equation;

public record PlusExpression(BigDecimal left, BigDecimal right) implements BinaryExpression{
	public static PlusExpression of(BigDecimal left, BigDecimal right) {
		return new PlusExpression(left, right);
	}
	
	public static PlusExpression of(BigDecimal left) {
		return new PlusExpression(left, null);
	}

	@Override
	public BinaryExpression withRight(BigDecimal localRight) {
		return of(left, localRight);
	}
	
	
	@Override
	public Equation resolve() {
		if(right == null) {
			return of(left, left).resolve();
		}
		return Equation.of(this, left.add(right));
	}




}
