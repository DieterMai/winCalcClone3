package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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
		return SimpleCalculatorRecord.of(resolveEquation(before));
	}

	private static Equation resolveEquation(final SimpleCalculatorRecord state) {
		return switch (state.expression()) {
		case IdleExpression i -> resolveOfIdle(state);
		case UnaryExpression unary -> resolveOfUnary(unary);
		case BinaryExpression binary -> resolveBinaryExpression(state);
		case NumberExpression number -> Equation.of(number, Result.of(number.value()));
		default -> throw new IllegalStateException("Not yet implemented!");
		};
	}

	private static Equation resolveOfIdle(final SimpleCalculatorRecord state) {
		if (state.input().isBlank()) {
			Equation previousEquation = state.equation();
			Expression previousExpression = previousEquation != null ? previousEquation.expression() : NumberExpression.of(BigDecimal.ZERO);
			return switch (previousExpression) {
			case NumberExpression ne -> equationOf(ne);
			case BinaryExpression be -> equationOf(BinaryExpression.of(previousEquation.value(), be.operator(), be.right()));
			default -> throw new IllegalStateException("expression: " + previousExpression);
			};
		} else {
			return equationOf(NumberExpression.of(state.input()));
		}
	}

	private static Equation resolveOfUnary(UnaryExpression unary) {
		return equationOf(unary);
	}

	private static Equation equationOf(Expression expression) {
		return Equation.of(expression, resultOf(expression));
	}

	private static Equation resolveBinaryExpression(final SimpleCalculatorRecord state) {
		BinaryExpression expression = (BinaryExpression) state.expression();
		BiOperator operator = expression.operator();
		Expression leftExpression = expression.left();
		Result left = resultOf(leftExpression);
		if (left.error())
			return Equation.of(expression, left);
		Expression rightExpression = getRightExpression(expression.right(), state.input(), left.value());
		Result right = resultOf(rightExpression);
		if (right.error())
			return Equation.of(expression, right);

		return switch (operator) {
		case plus -> resolvePlusExpression(left.value(), right.value());
		case minus -> resolveMinusExpression(left.value(), right.value());
		case multiply -> resolveMultiplyExpression(left.value(), right.value());
		case divide -> resolveDivideExpression(left.value(), right.value());
		};
	}

	private static Expression getRightExpression(Expression right, String input, BigDecimal leftValue) {
		if (right != null) {
			return right;
		} else if (!input.isBlank()) {
			return NumberExpression.of(input);
		} else {
			return NumberExpression.of(leftValue);
		}
	}

	public static SimpleCalculatorRecord number(final SimpleCalculatorRecord before, final String input) {
		return before.with(input);
	}

	public static SimpleCalculatorRecord plus(final SimpleCalculatorRecord state) {
		return binaryOperation(state, BiOperator.plus);
	}

	public static SimpleCalculatorRecord minus(final SimpleCalculatorRecord state) {
		return binaryOperation(state, BiOperator.minus);
	}

	public static SimpleCalculatorRecord multiply(final SimpleCalculatorRecord state) {
		return binaryOperation(state, BiOperator.multiply);
	}

	public static SimpleCalculatorRecord divide(final SimpleCalculatorRecord state) {
		return binaryOperation(state, BiOperator.divide);
	}

	private static SimpleCalculatorRecord binaryOperation(final SimpleCalculatorRecord state, final BiOperator operator) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}
		final Expression expression = state.expression();
		final String input = state.input();
		final Equation equation = state.equation();

		if (expression instanceof IdleExpression) {
			if (!input.isBlank()) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(input, operator), equation);
			} else if (equation != null) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(equation.value(), operator), equation);
			} else {
				return SimpleCalculatorRecord.of(BinaryExpression.of(BigDecimal.ZERO, operator), equation);
			}
		} else if (expression instanceof BinaryExpression be) {
			if (!input.isBlank()) {
				BinaryExpression completedExpression = be.withRight(input);
				final Result result = resultOf(completedExpression);
				final Equation newEquation = Equation.of(completedExpression, result);
				if (result.error()) {
					return SimpleCalculatorRecord.of(newEquation, result.type());
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), operator), newEquation);
				}
			} else if (be.isComplete()) {
				if (be.right() instanceof UnaryExpression unaryRight) {
					be = be.withRight(unaryRight); // WHY?
					Result result = resultOf(be);
					Equation newEquation = Equation.of(be, result);
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), operator), newEquation);
				} else if (be.right() instanceof NumberExpression ne) {
					Result result = resultOf(be);
					Equation newEquation = Equation.of(be, result);
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), operator), newEquation);
				}
			} else {
				return state.with(be.with(operator));
			}
		}

		throw new IllegalStateException("Not implemented yet");
	}

	public static SimpleCalculatorRecord negate(final SimpleCalculatorRecord state) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}
		String input = state.input();
		if (!input.isBlank()) {
			if (input.equals("0")) {
				return state;
			} else if (input.startsWith("-")) {
				return state.with(input.substring(1));
			} else {
				return state.with("-" + input);
			}
		}

		Expression expression = state.expression();
		if (expression instanceof UnaryExpression unary) {
			return state.with(UnaryExpression.of(UnaryOperator.negate, unary));
		}
		if (expression instanceof BinaryExpression binary) {
			if (binary.right() != null) {
				return state.with(binary.withRight(UnaryExpression.of(UnaryOperator.negate, binary.right())));
			} else {
				return state.with(binary.withRight(UnaryExpression.of(UnaryOperator.negate, binary.left())));
			}
		}

		Equation equation = state.equation();
		if (equation == null) {
			return state;
		} else {
			return state.with(UnaryExpression.of(UnaryOperator.negate, equation.value()));
		}
	}

	public static SimpleCalculatorRecord percent(final SimpleCalculatorRecord state) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}

		Expression expression = state.expression();

		if (expression instanceof BinaryExpression be) {
			BigDecimal percentNumber;
			String input = state.input();
			if (!input.isBlank()) {
				percentNumber = new BigDecimal(input);
			} else {
				Result result;
				if (be.right() == null) {
					result = resultOf(be.left());
					percentNumber = result.value();
				} else {
					result = resultOf(be.right());
				}
				if (result.error()) {
					throw new IllegalStateException("Not implemented yet!");
				}
				percentNumber = result.value();
			}
			Result leftResult = resultOf(be.left());
			if (leftResult.error()) {
				throw new IllegalStateException("Not implemented yet!");
			}
			BigDecimal leftValue = leftResult.value();
			BigDecimal rightValue = switch (be.operator()) {
			case plus, minus -> leftValue.multiply(percentNumber.divide(new BigDecimal("100"))).stripTrailingZeros();
			case multiply, divide -> percentNumber.divide(new BigDecimal("100")).stripTrailingZeros();
			};

			return state.with(be.withLeft(leftValue).withRight(rightValue)).with("");
		} else {
			return SimpleCalculatorRecord.of(NumberExpression.ZERO);
		}
	}

	public static SimpleCalculatorRecord square(SimpleCalculatorRecord state) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}
		
		Expression newExpression = appyUnaryOperator(state, UnaryOperator.square);
		Result newExpressionResult = resultOf(newExpression);
		if(newExpressionResult.error()) {
			return SimpleCalculatorRecord.of(newExpression, newExpressionResult.type());
		}else {
			return SimpleCalculatorRecord.of(newExpression);
		}
	}

	public static SimpleCalculatorRecord root(SimpleCalculatorRecord state) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}
		
		Expression newExpression = appyUnaryOperator(state, UnaryOperator.root);
		Result newExpressionResult = resultOf(newExpression);
		if(newExpressionResult.error()) {
			return SimpleCalculatorRecord.of(newExpression, newExpressionResult.type());
		}else {
			return SimpleCalculatorRecord.of(newExpression);
		}
	}

	public static SimpleCalculatorRecord oneDivX(SimpleCalculatorRecord state) {
		if(state.lastResolve().isError()) {
			throw new IllegalStateException("Can't operate on an error");
		}
		
		Expression newExpression = appyUnaryOperator(state, UnaryOperator.oneDivX);
		Result newExpressionResult = resultOf(newExpression);
		if(newExpressionResult.error()) {
			return SimpleCalculatorRecord.of(newExpression, newExpressionResult.type());
		}else {
			return SimpleCalculatorRecord.of(newExpression);
		}
	}
	
	private static Expression appyUnaryOperator(SimpleCalculatorRecord state, UnaryOperator operator) {
		String input = state.input();
		if(!input.isBlank()) {
			return UnaryExpression.of(operator, input);
		}
		
		Expression expression = state.expression();
		if (expression instanceof UnaryExpression unary) {
			return UnaryExpression.of(operator, unary);
		}
		
		if (expression instanceof BinaryExpression binary) {
			if (binary.right() != null) {
				return binary.withRight(UnaryExpression.of(operator, binary.right()));
			} else {
				return binary.withRight(UnaryExpression.of(operator, resultOf(binary.left()).value()));
			}
		}
		
		Equation equation = state.equation();
		if (equation == null) {
			return UnaryExpression.of(operator, BigDecimal.ZERO);
		} else {
			return UnaryExpression.of(operator, equation.value());
		}
	}
	
//	private static BigDecimal getUnaryValueToOperateOn(SimpleCalculatorRecord state) {
//		String input = state.input();
//		if(!input.isBlank()) {
//			return new BigDecimal(input);
//		}
//		
//		Expression expression = state.expression();
//		if (expression instanceof UnaryExpression unary) {
//			return resultOf(unary).value();
//		}
//		
//		if (expression instanceof BinaryExpression binary) {
//			
//			if (binary.right() != null) {
//				return resultOf(binary.right()).value();
//			} else {
//				return resultOf(binary.left()).value();
//			}
//		}
//		
//		Equation equation = state.equation();
//		if (equation == null) {
//			return BigDecimal.ZERO;
//		} else {
//			return equation.value();
//		}
//	}

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
		if (leftResult.error())
			return leftResult;
		if (rightResult.error())
			return rightResult;

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
		return Result.of(left.multiply(right).stripTrailingZeros());
	}

	private static Result resultDivideExpression(BigDecimal left, BigDecimal right) {
		if (right.equals(BigDecimal.ZERO)) {
			if (left.equals(BigDecimal.ZERO)) {
				return Result.of(ResolveType.UNDEFINED);
			} else {
				return Result.of(ResolveType.DIVIDE_BY_ZERO);
			}
		} else {
			BigDecimal result = normalize(left.divide(right, 16, RoundingMode.HALF_UP));
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

		if (nestedResult.error()) {
			return nestedResult;
		}

		return switch (unary.operator()) {
		case negate -> resultNegateExpression(nestedResult.value());
		case oneDivX -> resultOneDivX(nestedResult.value());
		case percent -> throw new IllegalStateException("Not implemented yet!");
		case root -> resultRoot(nestedResult.value());
		case square -> resultSquare(nestedResult.value());
		default -> null;
		};
	}

	private static Result resultNegateExpression(BigDecimal value) {
		return Result.of(value.negate());
	}

	private static Result resultSquare(BigDecimal value) {
		return Result.of(value.multiply(value, MathContext.DECIMAL64));
	}

	private static Result resultRoot(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) == -1) {
			return Result.of(ResolveType.INVALID_INPUT);
		} else {
			return Result.of(value.sqrt(MathContext.DECIMAL64));
		}
	}

	private static Result resultOneDivX(BigDecimal value) {
		if (value.equals(BigDecimal.ZERO)) {
			return Result.of(ResolveType.DIVIDE_BY_ZERO);
		} else {
			return Result.of(BigDecimal.ONE.divide(value, MathContext.DECIMAL64));
		}
	}

	private static BigDecimal normalize(BigDecimal bd) {
		return new BigDecimal(bd.stripTrailingZeros().toPlainString());
	}
}
