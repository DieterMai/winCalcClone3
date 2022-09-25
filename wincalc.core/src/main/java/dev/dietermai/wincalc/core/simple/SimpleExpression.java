package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public sealed interface SimpleExpression permits SimpleIdleExpression, SimpleNumberExpression {
	public BigDecimal value();
	public SimpleEquation resolve();
}
