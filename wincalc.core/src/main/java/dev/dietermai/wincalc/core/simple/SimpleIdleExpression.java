package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public final class SimpleIdleExpression implements SimpleExpression {
	private static final SimpleIdleExpression instance = new SimpleIdleExpression();
	
	public static SimpleIdleExpression of() {
		return instance;
	}
	
	@Override
	public BigDecimal value() {
		return BigDecimal.ZERO;
	}

	@Override
	public SimpleEquation resolve() {
		return SimpleEquation.of(SimpleNumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO);
	}
	
	
}
