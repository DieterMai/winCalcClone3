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
import dev.dietermai.wincalc.core.simple.expr.UnaryExpression;
import dev.dietermai.wincalc.core.simple.expr.UnaryOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

/*
 * resolve* -> creates an equation out of the current expression
 * valueOf* -> Calculates the BigDecimal result of the given expression
 */

public class SimpleCalculatorBl {

	public static SimpleCalculatorRecord resolve(final SimpleCalculatorRecord before) {
		final Equation equation = resolveEquation(before.expression(), before.equation());
		var after = before.withEquation(equation);
		return after.withExpression(IdleExpression.of());
	}

	private static Equation resolveEquation(Expression expression, Equation previousEquation) {
		return switch (expression) {
		case IdleExpression i -> resolveOfIdle(expression, previousEquation);
		case UnaryExpression unary -> resolveOfUnary(unary);
		case BinaryExpression binary -> resolveBinaryExpression(binary);
		case NumberExpression number -> Equation.of(number, number.value());
		default -> throw new IllegalStateException("Not yet implemented!");
		};
	}

	private static Equation resolveOfIdle(Expression expression, Equation previousEquation) {
		if (previousEquation == null) {
			return Equation.of(NumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO);
		} else if (previousEquation.expression() instanceof BinaryExpression be) {
			var after = BinaryExpression.of(previousEquation.value(), be.operator(), be.right());
			return resolveEquation(after, previousEquation);
		} else {
			throw new IllegalStateException("expression: " + previousEquation.expression());
		}
	}

	private static Equation resolveOfUnary(UnaryExpression unary) {
		return Equation.of(unary, valueOfUnaryEquation(unary));
	}

	private static Equation resolveBinaryExpression(BinaryExpression expression) {
		BiOperator operator = expression.operator();
		Expression leftExpression = expression.left();
		BigDecimal left = valueOfExpression(leftExpression);
		Expression rightExpression = Objects.requireNonNullElse(expression.right(), NumberExpression.of(left));
		BigDecimal right = valueOfExpression(rightExpression);

		return switch (operator) {
		case plus -> resolvePlusExpression(left, right);
		case minus -> resolveMinusExpression(left, right);
		case multiply -> resolveMultiplyExpression(left, right);
		case divide -> resolveDivideExpression(left, right);
		};
	}

	private static BigDecimal valueOfExpression(Expression expression) {
		return switch (expression) {
		case IdleExpression idle -> BigDecimal.ZERO;
		case NumberExpression ne -> ne.value();
		case UnaryExpression ue -> valueOfUnaryEquation(ue);
		case BinaryExpression be -> throw new IllegalArgumentException("Unexpected value: " + expression);
		};
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

	public static SimpleCalculatorRecord unary(final SimpleCalculatorRecord before, UnaryOperator operator) {
		return switch (operator) {
		case negate -> handleNegate(before);
		case divByX -> null;
		case percent -> null;
		case root -> null;
		case sqrt -> null;
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
		if (before.expression() instanceof UnaryExpression unary) {
			return getUnaryValue(unary);
		} else if (before.expression() instanceof NumberExpression ne) {
			return ne.value();
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

	private static BigDecimal getUnaryValue(UnaryExpression unary) {
		return switch (unary.operator()) {
		case divByX -> null;
		case negate -> null;
		case percent -> null;
		case root -> null;
		case sqrt -> null;
		};
	}

	private static SimpleCalculatorRecord handleNegate(SimpleCalculatorRecord before) {
		var beforeExpression = before.expression();

		return switch (beforeExpression) {
		case IdleExpression idle -> before.withExpression(UnaryExpression.of(UnaryOperator.negate, new BigDecimal("0")));
		case NumberExpression ne -> before.withExpression(NumberExpression.of(resolveNegateExpression(ne.value())));
		case UnaryExpression ue -> throw new IllegalStateException("Not implemented yet!");
		case BinaryExpression be -> before.withExpression(be.withRight(UnaryExpression.of(UnaryOperator.negate, be.left())));
		};
	}

	private static BigDecimal valueOfUnaryEquation(UnaryExpression unary) {
		BigDecimal value = switch (unary.nested()) {
		case IdleExpression idle -> BigDecimal.ZERO;
		case NumberExpression ne -> ne.value();
		case UnaryExpression ue -> valueOfUnaryEquation(ue);
		case BinaryExpression be -> throw new IllegalStateException("No binary expression in unary expression allowed");
		};

		return switch (unary.operator()) {
		case negate -> resolveNegateExpression(value);
		case divByX -> throw new IllegalStateException("Not implemented yet!");
		case percent -> throw new IllegalStateException("Not implemented yet!");
		case root -> throw new IllegalStateException("Not implemented yet!");
		case sqrt -> throw new IllegalStateException("Not implemented yet!");
		default -> null;
		};
	}

	private static BigDecimal resolveNegateExpression(BigDecimal value) {
		return value.negate();
	}

}
