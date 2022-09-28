package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

public class SimpleCalculator {

	private static final SimpleExpression INITIAL_EXPRESSION = SimpleIdleExpression.of();
	
	private SimpleExpression currentExpression = INITIAL_EXPRESSION;
	private SimpleEquation previousEquation;
	
	public SimpleExpression getExpression() {
		return currentExpression;
	}
	
	public SimpleEquation getPreviousEquation() {
		return previousEquation;
	}

	public void resolve() {
		previousEquation = currentExpression.resolve();
		currentExpression = INITIAL_EXPRESSION;
	}

	public void number(String number) {
		currentExpression = SimpleNumberExpression.of(number);
	}

	public void plus() {
		currentExpression = PlusExpression.of(BigDecimal.ZERO);
	}
}
