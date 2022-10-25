package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
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
		if(right != null) {
			return right;
		}else if(!input.isBlank()) {
			return NumberExpression.of(input);
		}else {
			return NumberExpression.of(leftValue);
		}
	}

	public static SimpleCalculatorRecord number(final SimpleCalculatorRecord before, final String input) {
		return before.with(input);
	}

	public static SimpleCalculatorRecord plus(final SimpleCalculatorRecord state) {
		final Expression expression = state.expression();
		final String input = state.input();
		final Equation equation = state.equation();

		if (expression instanceof IdleExpression) {
			if (!input.isBlank()) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(input, BiOperator.plus), equation);
			} else if (equation != null) {
				if (equation.type().isError()) {
					throw new IllegalStateException("Can't add an error");
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(equation.value(), BiOperator.plus), equation);
				}
			} else {
				return SimpleCalculatorRecord.of(BinaryExpression.of(BigDecimal.ZERO, BiOperator.plus), equation);
			}
		} else if (expression instanceof BinaryExpression be) {
			if (!input.isBlank()) {
				BinaryExpression completedExpression = be.withRight(input);
				final Result result = resultOf(completedExpression);
				final Equation newEquation = Equation.of(completedExpression, result);
				if (result.error()) {
					return SimpleCalculatorRecord.of(newEquation);
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.plus), newEquation);
				}
			} else {
				if (!input.isBlank()) {
					BinaryExpression newExpression = be.withRight(input);
					Result result = resultOf(newExpression);
					Equation newEquation = Equation.of(newExpression, result);
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.plus), newEquation);
				} else {
					return state.with(be.with(BiOperator.plus));
				}
			}
		}

		return null;// TODO
	}
	
	public static SimpleCalculatorRecord minus(final SimpleCalculatorRecord state) {
		final Expression expression = state.expression();
		final String input = state.input();
		final Equation equation = state.equation();

		if (expression instanceof IdleExpression) {
			if (!input.isBlank()) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(input, BiOperator.minus), equation);
			} else if (equation != null) {
				if (equation.type().isError()) {
					throw new IllegalStateException("Can't add an error");
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(equation.value(), BiOperator.minus), equation);
				}
			} else {
				return SimpleCalculatorRecord.of(BinaryExpression.of(BigDecimal.ZERO, BiOperator.minus), equation);
			}
		} else if (expression instanceof BinaryExpression be) {
			if (!input.isBlank()) {
				BinaryExpression completedExpression = be.withRight(input);
				final Result result = resultOf(completedExpression);
				final Equation newEquation = Equation.of(completedExpression, result);
				if (result.error()) {
					return SimpleCalculatorRecord.of(newEquation);
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.minus), newEquation);
				}
			} else {
				if (!input.isBlank()) {
					BinaryExpression newExpression = be.withRight(input);
					Result result = resultOf(newExpression);
					Equation newEquation = Equation.of(newExpression, result);
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.minus), newEquation);
				} else {
					return state.with(be.with(BiOperator.minus));
				}
			}
		}

		return null;// TODO
	}
	
	public static SimpleCalculatorRecord multiply(final SimpleCalculatorRecord state) {
		final Expression expression = state.expression();
		final String input = state.input();
		final Equation equation = state.equation();

		if (expression instanceof IdleExpression) {
			if (!input.isBlank()) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(input, BiOperator.multiply), equation);
			} else if (equation != null) {
				if (equation.type().isError()) {
					throw new IllegalStateException("Can't add an error");
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(equation.value(), BiOperator.multiply), equation);
				}
			} else {
				return SimpleCalculatorRecord.of(BinaryExpression.of(BigDecimal.ZERO, BiOperator.multiply), equation);
			}
		} else if (expression instanceof BinaryExpression be) {
			if (!input.isBlank()) {
				BinaryExpression completedExpression = be.withRight(input);
				final Result result = resultOf(completedExpression);
				final Equation newEquation = Equation.of(completedExpression, result);
				if (result.error()) {
					return SimpleCalculatorRecord.of(newEquation);
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.multiply), newEquation);
				}
			} else {
				if (!input.isBlank()) {
					BinaryExpression newExpression = be.withRight(input);
					Result result = resultOf(newExpression);
					Equation newEquation = Equation.of(newExpression, result);
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.multiply), newEquation);
				} else {
					return state.with(be.with(BiOperator.multiply));
				}
			}
		}

		return null;// TODO
	}
	
	public static SimpleCalculatorRecord divide(final SimpleCalculatorRecord state) {
		final Expression expression = state.expression();
		final String input = state.input();
		final Equation equation = state.equation();

		if (expression instanceof IdleExpression) {
			if (!input.isBlank()) {
				return SimpleCalculatorRecord.of(BinaryExpression.of(input, BiOperator.divide), equation);
			} else if (equation != null) {
				if (equation.type().isError()) {
					throw new IllegalStateException("Can't add an error");
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(equation.value(), BiOperator.divide), equation);
				}
			} else {
				return SimpleCalculatorRecord.of(BinaryExpression.of(BigDecimal.ZERO, BiOperator.divide), equation);
			}
		} else if (expression instanceof BinaryExpression be) {
			if (!input.isBlank()) {
				BinaryExpression completedExpression = be.withRight(input);
				final Result result = resultOf(completedExpression);
				final Equation newEquation = Equation.of(completedExpression, result);
				if (result.error()) {
					return SimpleCalculatorRecord.of(newEquation);
				} else {
					return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.divide), newEquation);
				}
			} else {
				if (!input.isBlank()) {
					BinaryExpression newExpression = be.withRight(input);
					Result result = resultOf(newExpression);
					Equation newEquation = Equation.of(newExpression, result);
					if(result.error()) {
						return SimpleCalculatorRecord.of(newEquation);
					}else {
						return SimpleCalculatorRecord.of(BinaryExpression.of(result.value(), BiOperator.divide), newEquation);
					}
				} else {
					return state.with(be.with(BiOperator.divide));
				}
			}
		}

		return null;// TODO
	}
	
	public static SimpleCalculatorRecord negate(final SimpleCalculatorRecord state) {
		String input = state.input();
		if(!input.isBlank()) {
			if(input.equals("0")) {
				return state;
			}else if(input.startsWith("-")) {
				return state.with(input.substring(1));
			}else {
				return state.with("-"+input);
			}
		}
		
		Expression expression = state.expression();
		if(expression instanceof UnaryExpression unary) {
			return state.with(UnaryExpression.of(UnaryOperator.negate, unary));
		}
		if(expression instanceof BinaryExpression binary) {
			if(binary.right() != null) {
				return state.with(binary.withRight(UnaryExpression.of(UnaryOperator.negate, binary.right())));
			}else {
				return state.with(binary.withRight(UnaryExpression.of(UnaryOperator.negate, binary.left())));
			}
		}
		
		Equation equation = state.equation();
		if(equation == null) {
			return state;
		}else if(equation.type().isError()) {
			throw new IllegalStateException("Can not operatoe on error result");
		}else {
			return state.with(UnaryExpression.of(UnaryOperator.negate, equation.value()));
		}
	}	
	
	public static SimpleCalculatorRecord percent(final SimpleCalculatorRecord state) {
		if(state.equation() != null && state.equation().type().isError()) {
			throw new IllegalStateException("Can not operatoe on error result");
		}
		
		Expression expression = state.expression();
		
		if(expression instanceof BinaryExpression be){
			BigDecimal percentNumber;
			String input = state.input();
			if(!input.isBlank()) {
				percentNumber = new BigDecimal(input);
			}else {
				Result result;
				if(be.right() == null) {
					result = resultOf(be.left());
					percentNumber = result.value();
				}else {
					result = resultOf(be.right());
				}
				if(result.error()) {
					throw new IllegalStateException("Not implemented yet!");
				}
				percentNumber = result.value().divide(new BigDecimal(100));
			}
			Result leftResult = resultOf(be.left());
			if(leftResult.error()) {
				throw new IllegalStateException("Not implemented yet!");
			}
			BigDecimal leftValue = leftResult.value();
			BigDecimal rightValue = leftValue.multiply(percentNumber);
			return state.with(be.withLeft(leftValue).withRight(rightValue));
		}else {
			return SimpleCalculatorRecord.of(Equation.of(BigDecimal.ZERO));
		}
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

		if (nestedResult.error()) {
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

	private static Result resultNegateExpression(BigDecimal value) {
		return Result.of(value.negate());
	}
}
