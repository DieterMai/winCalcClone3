package dev.dietermai.wincalc.core.simple.expr;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.Equation;

public final class IdleExpression implements Expression {
	private static final IdleExpression instance = new IdleExpression();
	
	public static IdleExpression of() {
		return instance;
	}
	
	@Override
	public Equation resolve() {
		return Equation.of(NumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO);
	}
	
	
}
