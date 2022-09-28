package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public record PlusExpression(BigDecimal left, BigDecimal right) implements SimpleExpression{
	public static PlusExpression of(BigDecimal left, BigDecimal right) {
		return new PlusExpression(left, right);
	}
	
	public static PlusExpression of(BigDecimal left) {
		return new PlusExpression(left, BigDecimal.ZERO);
	}


	@Override
	public SimpleEquation resolve() {
		// TODO Auto-generated method stub
		return null;
	}
}
