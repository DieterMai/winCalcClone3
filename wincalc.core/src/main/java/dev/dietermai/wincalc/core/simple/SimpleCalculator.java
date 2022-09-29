package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.PlusExpression;
import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;

public class SimpleCalculator {

	private static final Expression INITIAL_EXPRESSION = IdleExpression.of();

	private Expression currentExpression = INITIAL_EXPRESSION;
	private Equation previousEquation;

	public Expression getExpression() {
		return currentExpression;
	}

	public Equation getPreviousEquation() {
		return previousEquation;
	}

	public void resolve() {
		previousEquation = currentExpression.resolve();
		currentExpression = INITIAL_EXPRESSION;
	}

	public void number(String number) {
		if (currentExpression instanceof PlusExpression plus) {
			currentExpression = PlusExpression.of(plus.left(), new BigDecimal(number));
			resolve();
		} else {
			currentExpression = NumberExpression.of(number);
		}
	}

	public void plus() {
		currentExpression = PlusExpression.of(getInitialValueForBinaryOperation());
	}

	private BigDecimal getInitialValueForBinaryOperation() {
		if (currentExpression instanceof NumberExpression numberExpression) {
			return numberExpression.value();
		} else {
			return getPreviousResult();
		}
	}
	
	private BigDecimal getPreviousResult() {
		if (previousEquation != null) {
			return previousEquation.value();
		} else {
			return BigDecimal.ZERO;
		}
	}
}
