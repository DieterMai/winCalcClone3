package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

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
		switch(currentExpression) {
		case null -> throw new NullPointerException();
		case IdleExpression i -> previousEquation = Equation.of(NumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO);
		case NumberExpression ne -> previousEquation = Equation.of(ne, ne.value());
		case BinaryExpression binary -> previousEquation = resolveBinaryExpression(binary);
		}
		
		currentExpression = INITIAL_EXPRESSION;
	}

	public void number(String number) {
		if (currentExpression instanceof BinaryExpression binaryExpression) {
			currentExpression = binaryExpression.withRight(new BigDecimal(number));
			resolve();
		} else {
			currentExpression = NumberExpression.of(number);
		}
	}

	public void binary(BiOperator operator) {
		// TODO handle case when there is already a incomplete binary expression
		BigDecimal initalValue = getInitialValueForBinaryOperation();
		currentExpression = BinaryExpression.of(initalValue, operator);
//		currentExpression = operator.of(getInitialValueForBinaryOperation());
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

	private Equation resolveBinaryExpression(BinaryExpression expression) {
		BiOperator operator = expression.operator();
		BigDecimal left = expression.left();
		BigDecimal right = Objects.requireNonNullElse(expression.right(), left);
		return switch (operator) {
		case plus -> resolvePlusExpression(left, right);
		case minus -> resolveMinusExpression(left, right);
		case multiply -> resolveMultiplyExpression(left, right);
		case divide -> resolveDivideExpression(left, right);
		};
	}

	public Equation resolvePlusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.add(right);
		Expression expression = BinaryExpression.of(BiOperator.plus, left, right);
		Equation equation = Equation.of(expression, result);
		return equation;
	}

	public Equation resolveMinusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.subtract(right);
		Expression expression = BinaryExpression.of(BiOperator.minus, left, right);
		Equation equation = Equation.of(expression, result);
		return equation;
	}

	public Equation resolveMultiplyExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.multiply(right);
		Expression expression = BinaryExpression.of(BiOperator.multiply, left, right);
		Equation equation = Equation.of(expression, result);
		return equation;
	}

	public Equation resolveDivideExpression(BigDecimal left, BigDecimal right) {
		Expression expression = BinaryExpression.of(BiOperator.divide, left, right);
		if (right.equals(BigDecimal.ZERO)) {
			if (left.equals(BigDecimal.ZERO)) {
				return Equation.of(expression, ResolveType.UNDEFINED);
			} else {
				return Equation.of(expression, ResolveType.DIVIDE_BY_ZERO);
			}
		} else {
			BigDecimal result = left.divide(right, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			Equation equation = Equation.of(expression, result);
			return equation;
		}
	}
}
