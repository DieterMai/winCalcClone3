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

	private SimpleCalculatorRecord record = SimpleCalculatorRecord.initial();

	public SimpleCalculatorRecord getState() {
		return record;
	}

	public void resolve() {
		switch (record.expression()) {
		case null -> throw new NullPointerException();
		case IdleExpression i -> resolveOfIdle();
		case NumberExpression ne -> record = record.withEquation(Equation.of(ne, ne.value()));
		case BinaryExpression binary -> record = record.withEquation(resolveBinaryExpression(binary));
		}

		record = record.withExpression(INITIAL_EXPRESSION);
	}

	private void resolveOfIdle() {
		if (record.equation() == null) {
			record = record.withEquation(Equation.of(NumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO));
		} else if (record.equation().expression() instanceof BinaryExpression be) {
			record = record.withExpression(BinaryExpression.of(record.equation().value(), be.operator(), be.right()));
			resolve();
		} else {
			throw new IllegalStateException("expression: " + record.equation().expression());
		}
	}

	public void number(String number) {
		if (record.expression() instanceof BinaryExpression binaryExpression) {
			record = record.withExpression(binaryExpression.withRight(new BigDecimal(number)));
			resolve();
		} else {
			record = record.withExpression(NumberExpression.of(number));
		}
	}

	public void binary(BiOperator operator) {
		Expression currentExpression = record.expression();
		if (currentExpression instanceof BinaryExpression be) {
			record = record.withExpression(be.withOperator(operator));
		} else {
			BigDecimal initalValue = getInitialValueForBinaryOperation();
			record = record.withExpression(BinaryExpression.of(initalValue, operator));
		}
	}

	private BigDecimal getInitialValueForBinaryOperation() {
		if (record.expression() instanceof NumberExpression numberExpression) {
			return numberExpression.value();
		} else {
			return getPreviousResult();
		}
	}

	private BigDecimal getPreviousResult() {
		if (record.equation() != null) {
			return record.equation().value();
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
		Expression expression = BinaryExpression.of(left, BiOperator.plus, right);
		return Equation.of(expression, result);
	}

	public Equation resolveMinusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.subtract(right);
		Expression expression = BinaryExpression.of(left, BiOperator.minus, right);
		return Equation.of(expression, result);
	}

	public Equation resolveMultiplyExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.multiply(right);
		Expression expression = BinaryExpression.of(left, BiOperator.multiply, right);
		Equation equation = Equation.of(expression, result);
		return equation;
	}

	public Equation resolveDivideExpression(BigDecimal left, BigDecimal right) {
		Expression expression = BinaryExpression.of(left, BiOperator.divide, right);
		if (right.equals(BigDecimal.ZERO)) {
			if (left.equals(BigDecimal.ZERO)) {
				return Equation.of(expression, ResolveType.UNDEFINED);
			} else {
				return Equation.of(expression, ResolveType.DIVIDE_BY_ZERO);
			}
		} else {
			BigDecimal result = left.divide(right, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			return Equation.of(expression, result);
		}
	}
}
