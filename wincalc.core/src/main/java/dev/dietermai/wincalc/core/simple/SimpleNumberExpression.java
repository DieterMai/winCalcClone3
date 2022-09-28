package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public record SimpleNumberExpression(BigDecimal value) implements SimpleExpression{

	public static SimpleNumberExpression of(BigDecimal value) {
		return new SimpleNumberExpression(value);
	}
	
	public static SimpleNumberExpression of(String value) {
		var bd = new BigDecimal(value);
		return of(bd);
	}

	@Override
	public SimpleEquation resolve() {
		return SimpleEquation.of(this, value);
	}
}
