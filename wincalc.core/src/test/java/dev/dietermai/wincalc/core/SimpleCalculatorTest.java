package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;
import dev.dietermai.wincalc.core.simple.Result;
import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;
import dev.dietermai.wincalc.core.simple.expr.UnaryExpression;
import dev.dietermai.wincalc.core.simple.expr.UnaryOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

class SimpleCalculatorTest {

	private SimpleCalculator calculator;

	@BeforeEach
	public void beforeEach() {
		this.calculator = new SimpleCalculator();
	}

	@Test
	void testInitialState() {
		verify(IdleExpression.of());
	}

	@Test
	void testResolveOfInitialState() {
		calculator.resolve();
		verify(equation("0", "0"));
	}

	@Test
	void testInitialNumberInput() {
		calculator.number("123");
		verify("123");
	}

	@Test
	void testResolveOfInitialNumber() {
		calculator.number("123");
		calculator.resolve();

		verify(equation("123", "123"));
	}

	@Test
	void testNumberAfterResolvedNumber() {
		calculator.number("123");
		calculator.resolve();
		calculator.number("456");

		verify("456", equation("123", "123"));
	}

	@Test
	void testReloveOfNumberAfterResolvedNumber() {
		// Arrange
		calculator.number("123");
		calculator.resolve();
		calculator.number("456");

		// Act
		calculator.resolve();

		// Assert
		verify(equation("456", "456"));
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.plus();

		// Assert
		verify(expression("0", BiOperator.plus));
	}

	@Test
	void testResolveOfInitialPlus() {
		// Arrange
		calculator.plus();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("0", BiOperator.plus, "0"), "0"));

	}

	@Test
	void testPlusAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.plus();

		// Assert

		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();

	}

	@Test
	void testPlusAfterPlus() {
		calculator.number("123");
		calculator.plus();
		calculator.plus();

		// Assert
		verify(expression("123", BiOperator.plus));
	}

	@Test
	void testPlusAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.plus();

		// Assert

		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();

	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("123", BiOperator.plus, "123"), "246"));

	}

	@Test
	void testSecondNumberOfPlus() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();

	}

	@Test
	void testPlusAfterCompletePlusExpression() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.plus();

		verify(expression("579", BiOperator.plus), equation(expression("123", BiOperator.plus, "456"), "579"));
	}

	@Test
	void testResolveAfterCompletePlusEquation() {
		// Arrange
		calculator.number("1");
		calculator.plus();
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("1", BiOperator.plus, "2"), "3"));

	}

	@Test
	void testResolveAfterResolvedPlusExpression() {
		// Arrange
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("3", BiOperator.plus, "2"), "5"));

	}

	@Test
	void testPlusAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.plus();

		verify(expression("3", BiOperator.plus), equation(expression("1", BiOperator.plus, "2"), "3"));
	}

	@Test
	void testPlusAfterErrorInResolve() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		// Act
		assertThrowsExactly(IllegalStateException.class, () -> calculator.plus());
	}

	@Test
	void testPlusCompletesErrorEquation() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.plus();

		// Assert

		verify(equation(expression("1", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));

	}

	@Test
	void testPlusOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.plus();

		verify(expression("0", BiOperator.plus), equation(binary("5", BiOperator.plus, negate("5")), "0"));
	}

	@Test
	void testPlusOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();
		calculator.plus();

		verify(expression("21", BiOperator.plus), equation(binary("20", BiOperator.plus, "1"), "21"));
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		calculator.minus();

		// Assert
		verify(expression("0", BiOperator.minus));
	}

	@Test
	void testResolveOfInitialMinus() {
		// Arrange
		calculator.minus();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("0", BiOperator.minus, "0"), "0"));

	}

	@Test
	void testMinusAfterNumber() {
		calculator.number("123");
		calculator.minus();

		verify(expression("123", BiOperator.minus));
	}

	@Test
	void testMinusAfterMinus() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.minus();

		// Assert
		verify(expression("123", BiOperator.minus));
	}

	@Test
	void testMinusAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.minus();

		// Assert

		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();

	}

	@Test
	void testResloveOfMinusAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("123", BiOperator.minus, "123"), "0"));

	}

	@Test
	void testSecondNumberOfMinus() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();

	}

	@Test
	void testMinusAfterCompleteMinusExpression() {
		calculator.number("123");
		calculator.minus();
		calculator.number("456");
		calculator.minus();

		verify(expression("-333", BiOperator.minus), equation(expression("123", BiOperator.minus, "456"), "-333"));
	}

	@Test
	void testResolveAfterCompleteMinusEquation() {
		// Arrange
		calculator.number("1");
		calculator.minus();
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("1", BiOperator.minus, "2"), "-1"));

	}

	@Test
	void testResolveAfterResolvedMinusExpression() {
		// Arrange
		calculator.number("1");
		calculator.minus();
		calculator.number("2");
		calculator.resolve();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("-1", BiOperator.minus, "2"), "-3"));

	}

	@Test
	void testMinusAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.minus();

		verify(expression("3", BiOperator.minus), equation(expression("1", BiOperator.plus, "2"), "3"));
	}

	@Test
	void testMinusAfterErrorInResolve() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		// Act
		assertThrowsExactly(IllegalStateException.class, () -> calculator.minus());

	}

	@Test
	void testMinusCompletesErrorEquation() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.minus();

		// Assert

		verify(equation(expression("1", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));

	}

	@Test
	void testMinusOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.minus();

		verify(expression("0", BiOperator.minus),equation(binary("5", BiOperator.plus, negate("5")), "0"));
	}

	@Test
	void testMinusOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.minus();
		calculator.number("5");
		calculator.percent();
		calculator.minus();

		verify(expression("19", BiOperator.minus), equation(binary("20", BiOperator.minus, "1"), "19"));
	}

	/* **************************/
	/* Multiply related methods */
	/* **************************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		calculator.multiply();

		// Assert

		verifyExpression("0", BiOperator.multiply);
		verifyNoEquation();

	}

	@Test
	void testResolveOfInitialMultiply() {
		calculator.multiply();
		calculator.resolve();

		// Assert
		verify(equation(expression("0", BiOperator.multiply, "0"), "0"));
	}

	@Test
	void testMultiplyAfterNumber() {
		calculator.number("123");
		calculator.multiply();

		verify(expression("123", BiOperator.multiply));
	}

	@Test
	void testMultiplyAfterMultiply() {
		// Arrange
		calculator.number("123");
		calculator.multiply();

		// Act
		calculator.multiply();

		// Assert

		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();

	}

	@Test
	void testMultiplyAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.multiply();

		// Assert

		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();

	}

	@Test
	void testResloveOfMultiplyAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.multiply();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("123", BiOperator.multiply, "123"), "15129"));

	}

	@Test
	void testSecondNumberOfMultiply() {
		// Arrange
		calculator.number("123");
		calculator.multiply();

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();

	}

	@Test
	void testMultiplyAfterCompleteMultiplyExpression() {
		calculator.number("123");
		calculator.multiply();
		calculator.number("456");
		calculator.multiply();

		verify(expression("56088", BiOperator.multiply), equation(expression("123", BiOperator.multiply, "456"), "56088"));
	}

	@Test
	void testResolveAfterCompleteMultiplyEquation() {
		// Arrange
		calculator.number("1");
		calculator.multiply();
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("1", BiOperator.multiply, "2"), "2"));

	}

	@Test
	void testResolveAfterResolvedMultiplyExpression() {
		// Arrange
		calculator.number("1");
		calculator.multiply();
		calculator.number("2");
		calculator.resolve();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("2", BiOperator.multiply, "2"), "4"));

	}

	@Test
	void testMultiplyAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.multiply();

		verify(expression("3", BiOperator.multiply), equation(expression("1", BiOperator.plus, "2"), "3"));
	}

	@Test
	void testMultiplyAfterErrorInResolve() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		// Act
		assertThrowsExactly(IllegalStateException.class, () -> calculator.multiply());

	}

	@Test
	void testMultiplyCompletesErrorEquation() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.multiply();

		// Assert

		verify(equation(expression("1", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));

	}

	@Test
	void testMultiplyOnCompletedButPendingBinaryExpression() {
		// Arrange
		calculator.number("5");
		calculator.plus();
		calculator.negate();

		// Act
		calculator.multiply();

		// Assert

		verify(expression("0", BiOperator.multiply), equation(expression("5", BiOperator.plus, negate("5")), "0"));

	}

	@Test
	void testMultiplyOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.multiply();
		calculator.number("5");
		calculator.percent();
		calculator.multiply();

		verify(expression("1", BiOperator.multiply), equation(binary("20", BiOperator.multiply, "0.05"), "1"));
	}

	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		calculator.divide();

		// Assert

		verifyExpression("0", BiOperator.divide);
		verifyNoEquation();

	}

	@Test
	void testResolveOfInitialDivide() {
		// Arrange
		calculator.divide();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("0", BiOperator.divide, "0"), ResolveType.UNDEFINED));

	}

	@Test
	void testDivideAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.divide();

		// Assert

		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();

	}

	@Test
	void testDivideAfterDivide() {
		// Arrange
		calculator.number("123");
		calculator.divide();

		// Act
		calculator.divide();

		// Assert

		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();

	}

	@Test
	void testDivideAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.divide();

		// Assert
		verify(expression("123", BiOperator.divide));
	}

	@Test
	void testResloveOfDivideAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.divide();

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("123", BiOperator.divide, "123"), "1"));

	}

	@Test
	void testSecondNumberOfDivide() {
		// Arrange
		calculator.number("123");
		calculator.divide();

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();

	}

	@Test
	void testDivideAfterCompleteDivideExpression() {
		calculator.number("100");
		calculator.divide();
		calculator.number("8");
		calculator.divide();

		verify(expression("12.5", BiOperator.divide), equation(expression("100", BiOperator.divide, "8"), "12.5"));
	}

	@Test
	void testResolveAfterCompleteDivideEquation() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert

		verify(equation(expression("1", BiOperator.divide, "2"), "0.5"));

	}

	@Test
	void testResolveAfterResolvedDivideExpression() {
		calculator.number("1");
		calculator.divide();
		calculator.number("2");
		calculator.resolve();
		calculator.resolve();

		verify(equation(expression("0.5", BiOperator.divide, "2"), "0.25"));
	}

	@Test
	void testDivideBy0TestTriggerdViaBinaryOperator() {
		calculator.number("100");
		calculator.divide();
		calculator.number("0");
		calculator.divide();

		verify(equation(expression("100", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

	@Test
	void testDivideBy0TestTriggerdViaResolve() {
		calculator.number("100");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		verify(equation(expression("100", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

	@Test
	void testdivideAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.divide();

		verify(expression("3", BiOperator.divide), equation(expression("1", BiOperator.plus, "2"), "3"));
	}

	@Test
	void testDivideAfterErrorInResolve() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		// Act
		assertThrowsExactly(IllegalStateException.class, () -> calculator.divide());

	}

	@Test
	void testDivideCompletesErrorEquation() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.divide();

		// Assert
		verify(equation(expression("1", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

//	@Test
//	void testDivideOnCompletedButPendingBinaryExpression() {
//		// Arrange
//		calculator.number("5");
//		calculator.plus();
//		calculator.negate(); // TODO switch this with another unary operation
//
//		// Act
//		calculator.divide();
//		
//		// Assert
//		
//		verifyExpression("0", BiOperator.divide);
//		verify(equation(plus("5", negate("5")), "0");
//	
//	}

	@Test
	void testDivideOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.divide();
		calculator.number("5");
		calculator.percent();
		calculator.divide();

		verify(expression("400", BiOperator.divide), equation(binary("20", BiOperator.divide, "0.05"), "400"));
	}

	/* ************************/
	/* Negate related methods */
	/* ************************/
	@Test
	void testInitialNegate() {
		// Act
		calculator.negate();

		// Assert

		verifyNoEquation();

	}

	@Test
	void testNegateOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.negate();

		// Assert
		verify("-123");
	}

	@Test
	void testNegateOfNegativeNumber() {
		// Act
		calculator.number("-123");
		calculator.negate();

		// Assert
		verifyInput("123");

		verifyNoEquation();

	}

	@Test
	void testNegateOfZero() {
		// Act
		calculator.number("0");
		calculator.negate();

		// Assert
		verifyInput("0");

		verifyNoEquation();

	}

	@Test
	void testNegateOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.negate();

		verify(expression(UnaryOperator.negate, "0"), equation("0", "0"));
	}

	@Test
	void testNegateOfResult() {
		calculator.number("5");
		calculator.resolve();
		calculator.negate();

		verify(expression(UnaryOperator.negate, "5"), equation("5", "5"));
	}

	@Test
	void testNegateOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.negate();
		calculator.negate();

		verify(expression(UnaryOperator.negate, unary(UnaryOperator.negate, "5")), equation("5", "5"));
	}

	@Test
	void testNegateOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.negate();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.negate, "5")));
		verifyNoEquation();

	}

	@Test
	void testNegateOfBinaryRight() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.negate();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.negate, unary(UnaryOperator.negate, "5"))));
		verifyNoEquation();

	}

	@Test
	void testNegateOfError() {
		// Act
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		assertThrowsExactly(IllegalStateException.class, () -> calculator.negate());
	}

	/* *************************/
	/* Percent related methods */
	/* *************************/
	@Test
	void testPercentNegate() {
		calculator.percent();

		verify(NumberExpression.ZERO);
	}

	@Test
	void testPercentOfPositiveNumber() {
		calculator.number("123");
		calculator.percent();

		verify(NumberExpression.ZERO);
	}

	@Test
	void testPercentOfNegativeNumber() {
		// Act
		calculator.number("-123");
		calculator.percent();

		// Assert
		verify(NumberExpression.ZERO);
	}

	@Test
	void testPercentOfZero() {
		calculator.number("0");
		calculator.percent();

		verify(NumberExpression.ZERO);
	}

	@Test
	void testPercentOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.percent();

		verify(NumberExpression.ZERO, equation("0", "0"));
	}

	@Test
	void testPercentOfResult() {
		calculator.number("5");
		calculator.resolve();
		calculator.percent();

		verify(NumberExpression.ZERO, equation("5", "5"));
	}

	@Test
	void testPercentOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.negate();
		calculator.percent();

		verify(NumberExpression.ZERO, equation(expression(UnaryOperator.negate, "5"), "0"));
	}

	@Test
	void testPercentOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.percent();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, "0.25"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfBinaryUnaryRight() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.percent();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, "-0.25"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfPlusRightNumber() {
		// Act
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();

		// Assert

		verifyExpression(binary("20", BiOperator.plus, "1"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfMinusRightNumber() {
		// Act
		calculator.number("20");
		calculator.minus();
		calculator.number("5");
		calculator.percent();

		// Assert

		verifyExpression(binary("20", BiOperator.minus, "1"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfMultiplyRightNumber() {
		// Act
		calculator.number("20");
		calculator.multiply();
		calculator.number("5");
		calculator.percent();

		// Assert

		verifyExpression(binary("20", BiOperator.multiply, "0.05"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfDivideRightNumber() {
		// Act
		calculator.number("20");
		calculator.divide();
		calculator.number("5");
		calculator.percent();

		// Assert

		verifyExpression(binary("20", BiOperator.divide, "0.05"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfError() {
		// Act
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.percent());

		// Assert

		verify(equation(expression("5", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));

	}

	@Test
	void testPercentBinaryWithInput() {
		// Act
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();

		// Assert
		verify(expression("20", BiOperator.plus, "1"));
	}

	/* ************************/
	/* Square related methods */
	/* ************************/
	@Test
	void testInitialSquare() {
		// Act
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "123"));
	}

	@Test
	void testSquareOfNegativeNumber() {
		// Act
		calculator.number("-123");
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "-123"));
	}

	@Test
	void testSquareOfZero() {
		// Act
		calculator.number("0");
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfZeroResult() {
		// Act
		calculator.number("0");
		calculator.resolve();
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfResult() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.square();

		// Assert
		verify(expression(UnaryOperator.square, "5"));
	}

	@Test
	void testSquareOfUnaryExpression() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.square();
		calculator.square();

		// Assert

		verifyExpression(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")));
		verifyNoEquation();
		verify(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")));
	}

	@Test
	void testSquareOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.square();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.square, "5")));
		verifyNoEquation();
	}

	@Test
	void testSquareOfBinaryRight() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.square();
		calculator.square();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.square, unary(UnaryOperator.square, "5"))));
		verifyNoEquation();
	}

	@Test
	void testSquareOfError() {
		// Act
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.square());

		// Assert

		verify(equation(expression("5", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

	@Test
	void testSquareOfRoot() {
		// Act
		calculator.number("5");
		calculator.square();
		calculator.resolve();

		// Assert

		verify(equation(unary(UnaryOperator.square, "5"), "25"));
	}

	@Test
	void testSquareOfNestedRoot() {
		// Act
		calculator.number("5");
		calculator.square();
		calculator.square();
		calculator.resolve();

		// Assert

		verify(equation(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")), "625"));
	}

	/* **********************/
	/* Root related methods */
	/* **********************/
	@Test
	void testInitialRoot() {
		// Act
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "123");
		verifyNoEquation();
	}

	@Test
	void testRootOfNegativeNumber() {
		// Act
		calculator.number("123");
		calculator.negate();
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "-123");
		verifyNoEquation();
		verifyError(ResolveType.INVALID_INPUT);
	}

	@Test
	void testRootOfZero() {
		// Act
		calculator.number("0");
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfZeroResult() {
		// Act
		calculator.number("0");
		calculator.resolve();
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfResult() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.root();

		// Assert

		verifyExpression(UnaryOperator.root, "5");
		verifyNoEquation();
	}

	@Test
	void testRootOfUnaryExpression() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.root();
		calculator.root();

		// Assert

		verifyExpression(expression(UnaryOperator.root, unary(UnaryOperator.root, "5")));
		verifyNoEquation();
	}

	@Test
	void testRootOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.root();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.root, "5")));
		verifyNoEquation();
	}

	@Test
	void testRootOfBinaryRight() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.root();
		calculator.root();

		// Assert

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.root, unary(UnaryOperator.root, "5"))));
		verifyNoEquation();
	}

	@Test
	void testRootOfError() {
		// Act
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.root());

		// Assert

		verify(equation(expression("5", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

	@Test
	void testResolveOfRoot() {
		// Act
		calculator.number("25");
		calculator.root();
		calculator.resolve();

		// Assert

		verify(equation(unary(UnaryOperator.root, "25"), "5"));
	}

	@Test
	void testResolveOfNestedRoot() {
		// Act
		calculator.number("625");
		calculator.root();
		calculator.root();
		calculator.resolve();

		// Assert

		verify(equation(expression(UnaryOperator.root, unary(UnaryOperator.root, "625")), "5"));
	}

	@Test
	void testResolveOfRootIrrationalResult() {
		// Act
		calculator.number("2");
		calculator.root();
		calculator.resolve();

		// Assert

		verify(equation(unary(UnaryOperator.root, "2"), "1.414213562373095"));
	}

//	@Test
//	void testRootOfRootWithNegativeNumber() {
//		// Act
//		calculator.number("5");
//		calculator.negate();
//		calculator.root();
//		assertThrowsExactly(IllegalStateException.class, () -> calculator.root()); // TODO implement error state into calculator state
//
//		// Assert
//		
//		
//		verify(equation("5", BiOperator.divide, "0", ResolveType.INVALID_INPUT);
//	}

	/* **********************/
	/* OneDivX related methods */
	/* **********************/
	@Test
	void testOneDivXOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.oneDivX();

		// Assert

		verifyExpression(UnaryOperator.oneDivX, "123");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfNegativeNumber() {
		// Act
		calculator.number("123");
		calculator.negate();
		calculator.oneDivX();

		// Assert

		verifyExpression(UnaryOperator.oneDivX, "-123");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfZero() {
		// Act
		calculator.number("0");
		calculator.oneDivX();

		// Assert

		verifyExpression(UnaryOperator.oneDivX, "0");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfZeroResult() {
		// Act
		calculator.number("0");
		calculator.resolve();
		calculator.oneDivX();

		// Assert

		verifyExpression(UnaryOperator.oneDivX, "0");
		verifyNoEquation();
		verifyError(ResolveType.DIVIDE_BY_ZERO);
	}

	@Test
	void testOneDivXOfResult() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "5");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.oneDivX();
		calculator.oneDivX();

		verify(expression(UnaryOperator.oneDivX, expression(UnaryOperator.oneDivX, "5")));
	}

	@Test
	void testOneDivXOfBinaryLeft() {
		calculator.number("5");
		calculator.plus();
		calculator.oneDivX();

		verify(expression("5", BiOperator.plus, expression(UnaryOperator.oneDivX, "5")));
	}

	@Test
	void testOneDivXOfBinaryRight() {
		calculator.number("5");
		calculator.plus();
		calculator.oneDivX();
		calculator.oneDivX();

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.oneDivX, unary(UnaryOperator.oneDivX, "5"))));
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfError() {
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.oneDivX());

		verify(equation(expression("5", BiOperator.divide, "0"), ResolveType.DIVIDE_BY_ZERO));
	}

	@Test
	void testResolveOfOneDivX() {
		calculator.number("25");
		calculator.oneDivX();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.oneDivX, "25"), "0.04"));
	}

	@Test
	void testResolveOfNestedOneDivX() {
		calculator.number("625");
		calculator.oneDivX();
		calculator.oneDivX();
		calculator.resolve();

		verify(equation(expression(UnaryOperator.oneDivX, unary(UnaryOperator.oneDivX, "625")), "625"));
	}

	@Test
	void testResolveOfOneDivXIrrationalResult() {
		calculator.number("3");
		calculator.oneDivX();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.oneDivX, "3"), "0.3333333333333333"));
	}

	@Test
	void testResolveOfOneDivXIrrationalResult2() {
		calculator.number("6");
		calculator.oneDivX();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.oneDivX, "6"), "0.1666666666666667"));
	}

	@Test
	void testInitialOneDivX() {
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "0");
		verifyNoEquation();
		verifyError(ResolveType.DIVIDE_BY_ZERO);
	}

	private void verifyExpression(String number, BiOperator operator) {
		assertEquals(BinaryExpression.of(bd(number), operator), getExpression());
	}

	private void verifyExpression(UnaryOperator operator, String number) {
		assertEquals(UnaryExpression.of(operator, bd(number)), getExpression());
	}

	private BinaryExpression binary(String left, BiOperator operator, Expression right) {
		return BinaryExpression.of(bd(left), operator, right);
	}

	private BinaryExpression binary(String left, BiOperator operator, String right) {
		return BinaryExpression.of(bd(left), operator, bd(right));
	}

	private UnaryExpression unary(UnaryOperator operator, String number) {
		return UnaryExpression.of(operator, number(number));
	}

	private BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	private NumberExpression number(String s) {
		return NumberExpression.of(s);
	}

	private Expression getExpression() {
		return calculator.getState().expression();
	}

	private Equation getEquation() {
		return calculator.getState().equation();
	}

	private UnaryExpression negate(String number) {
		return UnaryExpression.of(UnaryOperator.negate, number);
	}

	// NEW

	// Verify methods
	private void verify(String input) {
		verifyInput(input);
		verifyExpression(IdleExpression.of());
		verifyNoEquation();
		verifyError(ResolveType.SUCCESS);
	}

	private void verify(Expression expression) {
		verifyInput("");
		verifyExpression(expression);
		verifyNoEquation();
		verifyError(ResolveType.SUCCESS);
	}

	private void verify(Expression expression, Equation equation) {
		verifyInput("");
		verifyExpression(expression);
		verifyEquation(equation);
		verifyError(ResolveType.SUCCESS);
	}

	private void verify(Equation equation) {
		verifyInput("");
		verifyExpression(IdleExpression.of());
		verifyEquation(equation);
		verifyError(equation.type());
	}

	private void verify(String input, Equation equation) {
		verifyInput(input);
		verifyExpression(IdleExpression.of());
		verifyEquation(equation);
		verifyError(equation.type());
	}

	private void verifyInput(String expected) {
		assertEquals(expected, calculator.getState().input());
	}

	private void verifyExpression(Expression expected) {
		assertEquals(expected, getExpression());
	}

	private void verifyEquation(Equation expected) {
		assertEquals(expected, getEquation());
	}

	private void verifyNoEquation() {
		assertNull(getEquation());
	}

	private void verifyError(ResolveType error) {
		assertEquals(error, calculator.getState().lastResolve());
	}

	// Util methods
	private BinaryExpression expression(String number, BiOperator opreator) {
		return BinaryExpression.of(number, opreator);
	}

	private UnaryExpression expression(UnaryOperator opreator, String number) {
		return UnaryExpression.of(opreator, number);
	}

	private UnaryExpression expression(UnaryOperator operator, Expression nested) {
		return UnaryExpression.of(operator, nested);
	}

	private BinaryExpression expression(String left, BiOperator opreator, String right) {
		return BinaryExpression.of(left, opreator, right);
	}

	private BinaryExpression expression(String left, BiOperator opreator, Expression right) {
		return BinaryExpression.of(left, opreator, right);
	}

	private Equation equation(String left, String right) {
		return Equation.of(number(left), Result.of(bd(right)));
	}

	private Equation equation(Expression expression, String value) {
		return Equation.of(expression, Result.of(bd(value)));
	}

	private Equation equation(Expression expression, ResolveType type) {
		return Equation.of(expression, Result.of(type));
	}
}
