package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public record SimpleNumberExpression(BigDecimal value) implements SimpleExpression{

	public static SimpleNumberExpression of(BigDecimal value) {
		return new SimpleNumberExpression(value);
	}

	@Override
	public SimpleEquation resolve() {
		return SimpleEquation.of(this, value);
	}
}
