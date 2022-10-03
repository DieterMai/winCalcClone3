package dev.dietermai.wincalc.core.simple.bl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;
import dev.dietermai.wincalc.core.simple.SimpleCalculatorRecord;
import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

public class SimpleCalculatorBl {
	public static SimpleCalculatorRecord resolve(final SimpleCalculatorRecord before) {
		final var after = switch (before.expression()) {
		case null -> throw new NullPointerException();
		case IdleExpression i -> resolveOfIdle(before);
		case NumberExpression ne -> before.withEquation(Equation.of(ne, ne.value()));
		case BinaryExpression binary -> before.withEquation(resolveBinaryExpression(binary));
		};

		return after.withExpression(IdleExpression.of());
	}

	public static SimpleCalculatorRecord number(final SimpleCalculatorRecord before, final String number) {
		if (before.expression() instanceof BinaryExpression binaryExpression) {
			final SimpleCalculatorRecord after = before.withExpression(binaryExpression.withRight(new BigDecimal(number)));
			return resolve(after);
		} else {
			return before.withExpression(NumberExpression.of(number));
		}
	}

	public static SimpleCalculatorRecord binary(final SimpleCalculatorRecord before, final BiOperator operator) {
		Expression currentExpression = before.expression();
		if (currentExpression instanceof BinaryExpression be) {
			return before.withExpression(be.withOperator(operator));
		} else {
			BigDecimal initalValue = getInitialValueForBinaryOperation(before);
			return before.withExpression(BinaryExpression.of(initalValue, operator));
		}
	}

	private static SimpleCalculatorRecord resolveOfIdle(SimpleCalculatorRecord before) {
		if (before.equation() == null) {
			return before.withEquation(Equation.of(NumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO));
		} else if (before.equation().expression() instanceof BinaryExpression be) {
			var after = before.withExpression(BinaryExpression.of(before.equation().value(), be.operator(), be.right()));
			return resolve(after);
		} else {
			throw new IllegalStateException("expression: " + before.equation().expression());
		}
	}

	private static Equation resolveBinaryExpression(BinaryExpression expression) {
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

	private static Equation resolvePlusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.add(right);
		Expression expression = BinaryExpression.of(left, BiOperator.plus, right);
		return Equation.of(expression, result);
	}

	private static Equation resolveMinusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.subtract(right);
		Expression expression = BinaryExpression.of(left, BiOperator.minus, right);
		return Equation.of(expression, result);
	}

	private static Equation resolveMultiplyExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.multiply(right);
		Expression expression = BinaryExpression.of(left, BiOperator.multiply, right);
		return Equation.of(expression, result);
	}

	private static Equation resolveDivideExpression(BigDecimal left, BigDecimal right) {
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

	private static BigDecimal getInitialValueForBinaryOperation(final SimpleCalculatorRecord before) {
		if (before.expression() instanceof NumberExpression numberExpression) {
			return numberExpression.value();
		} else {
			return getPreviousResult(before);
		}
	}

	private static BigDecimal getPreviousResult(final SimpleCalculatorRecord before) {
		if (before.equation() != null) {
			return before.equation().value();
		} else {
			return BigDecimal.ZERO;
		}
	}
}
