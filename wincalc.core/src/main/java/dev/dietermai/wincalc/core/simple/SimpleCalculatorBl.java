package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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
		var after = before.with(equation);
		return after.with(IdleExpression.of());
	}

	private static Equation resolveEquation(Expression expression, Equation previousEquation) {
		return switch (expression) {
		case IdleExpression i -> resolveOfIdle(previousEquation);
		case UnaryExpression unary -> resolveOfUnary(unary);
		case BinaryExpression binary -> resolveBinaryExpression(binary);
		case NumberExpression number -> Equation.of(number, Result.of(number.value()));
		default -> throw new IllegalStateException("Not yet implemented!");
		};
	}

	private static Equation resolveOfIdle(Equation previousEquation) {
		Expression previousExpression = previousEquation != null ? previousEquation.expression() : NumberExpression.of(BigDecimal.ZERO);
		return switch(previousExpression) {
		case NumberExpression ne -> equationOf(ne);
		case BinaryExpression be -> equationOf(BinaryExpression.of(previousEquation.value(), be.operator(), be.right()));
		default -> throw new IllegalStateException("expression: " + previousExpression);
		};
	}
	
	private static Equation resolveOfUnary(UnaryExpression unary) {
		return equationOf(unary);
	}
	
	private static Equation equationOf(Expression expression) {
		return Equation.of(expression, resultOf(expression));
	}

	private static Equation resolveBinaryExpression(BinaryExpression expression) {
		BiOperator operator = expression.operator();
		Expression leftExpression = expression.left();
		Result left = resultOf(leftExpression);
		if(left.error()) return Equation.of(expression, left);
		Expression rightExpression = Objects.requireNonNullElse(expression.right(), NumberExpression.of(left.value()));
		Result right = resultOf(rightExpression);
		if(right.error()) return Equation.of(expression, right);

		return switch (operator) {
		case plus -> resolvePlusExpression(left.value(), right.value());
		case minus -> resolveMinusExpression(left.value(), right.value());
		case multiply -> resolveMultiplyExpression(left.value(), right.value());
		case divide -> resolveDivideExpression(left.value(), right.value());
		};
	}

	public static SimpleCalculatorRecord number(final SimpleCalculatorRecord before, final String number) {
		if (before.expression() instanceof BinaryExpression binaryExpression) {
			final SimpleCalculatorRecord after = before.with(binaryExpression.withRight(new BigDecimal(number)));
			return resolve(after);
		} else {
			return before.with(NumberExpression.of(number));
		}
	}

	public static SimpleCalculatorRecord binary(final SimpleCalculatorRecord before, final BiOperator operator) {
		Expression currentExpression = before.expression();
		if (currentExpression instanceof BinaryExpression be) {
			return before.with(be.withOperator(operator));
		} else {
			BigDecimal initalValue = getInitialValueForBinaryOperation(before);
			return before.with(BinaryExpression.of(initalValue, operator));
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
		return Equation.of(expression, Result.of(result));
	}

	private static Equation resolveMinusExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.subtract(right);
		Expression expression = BinaryExpression.of(left, BiOperator.minus, right);
		return Equation.of(expression, Result.of(result));
	}

	private static Equation resolveMultiplyExpression(BigDecimal left, BigDecimal right) {
		BigDecimal result = left.multiply(right);
		Expression expression = BinaryExpression.of(left, BiOperator.multiply, right);
		return Equation.of(expression, Result.of(result));
	}

	private static Equation resolveDivideExpression(BigDecimal left, BigDecimal right) {
		Expression expression = BinaryExpression.of(left, BiOperator.divide, right);
		if (right.equals(BigDecimal.ZERO)) {
			if (left.equals(BigDecimal.ZERO)) {
				return Equation.of(expression, Result.of(ResolveType.UNDEFINED));
			} else {
				return Equation.of(expression, Result.of(ResolveType.DIVIDE_BY_ZERO));
			}
		} else {
			BigDecimal result = left.divide(right, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			return Equation.of(expression, Result.of(result));
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
		case IdleExpression idle -> before.with(UnaryExpression.of(UnaryOperator.negate, new BigDecimal("0")));
		case NumberExpression ne -> before.with(NumberExpression.of(resultNegateExpression(ne.value()).value()));
		case UnaryExpression ue -> throw new IllegalStateException("Not implemented yet!");
		case BinaryExpression be -> before.with(be.withRight(UnaryExpression.of(UnaryOperator.negate, be.left())));
		};
	}

	private static Result resultOf(Expression expression) {
		return switch (expression) {
		case IdleExpression idle -> resultOfIdleExpression();
		case NumberExpression ne -> resultOfNumberExpression(ne);
		case UnaryExpression ue -> resultOfUnaryExpression(ue);
		case BinaryExpression be -> resultOfBinaryExpression(be);
		};
	}

	private static Result resultOfIdleExpression() {
		return Result.of(BigDecimal.ZERO);
	}

	private static Result resultOfNumberExpression(NumberExpression number) {
		return Result.of(number.value());
	}

	private static Result resultOfBinaryExpression(BinaryExpression binary) {
		Result leftResult = resultOf(binary.left());
		Result rightResult = resultOf(binary.right());
		if(leftResult.error()) return leftResult;
		if(rightResult.error()) return rightResult;
		
		BigDecimal left = leftResult.value();
		BigDecimal right = rightResult.value();

		return switch (binary.operator()) {
		case plus -> resultPlusExpression(left, right);
		case minus -> resultMinusExpression(left, right);
		case multiply -> resultMultiplyExpression(left, right);
		case divide -> resultDivideExpression(left, right);
		};
	}

	private static Result resultPlusExpression(BigDecimal left, BigDecimal right) {
		return Result.of(left.add(right));
	}

	private static Result resultMinusExpression(BigDecimal left, BigDecimal right) {
		return Result.of(left.subtract(right));
	}

	private static Result resultMultiplyExpression(BigDecimal left, BigDecimal right) {
		return Result.of(left.multiply(right));
	}

	private static Result resultDivideExpression(BigDecimal left, BigDecimal right) {
		if (right.equals(BigDecimal.ZERO)) {
			if (left.equals(BigDecimal.ZERO)) {
				return Result.of(ResolveType.UNDEFINED);
			} else {
				return Result.of(ResolveType.DIVIDE_BY_ZERO);
			}
		} else {
			BigDecimal result = left.divide(right, 16, RoundingMode.HALF_UP).stripTrailingZeros();
			return Result.of(result);
		}
	}

	private static Result resultOfUnaryExpression(UnaryExpression unary) {
		Result nestedResult = switch (unary.nested()) {
		case IdleExpression idle -> Result.of(BigDecimal.ZERO);
		case NumberExpression ne -> Result.of(ne.value());
		case UnaryExpression ue -> resultOfUnaryExpression(ue);
		case BinaryExpression be -> throw new IllegalStateException("No binary expression in unary expression allowed");
		};

		if(nestedResult.error()) {
			return nestedResult;
		}

		return switch (unary.operator()) {
		case negate -> resultNegateExpression(nestedResult.value());
		case divByX -> throw new IllegalStateException("Not implemented yet!");
		case percent -> throw new IllegalStateException("Not implemented yet!");
		case root -> throw new IllegalStateException("Not implemented yet!");
		case sqrt -> throw new IllegalStateException("Not implemented yet!");
		default -> null;
		};
	}

	public static Result resultNegateExpression(BigDecimal value) {
		return Result.of(value.negate());
	}

}
