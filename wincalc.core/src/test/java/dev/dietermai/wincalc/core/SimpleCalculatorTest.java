package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.model.BiOperator;
import dev.dietermai.wincalc.core.simple.model.BinaryExpression;
import dev.dietermai.wincalc.core.simple.model.Equation;
import dev.dietermai.wincalc.core.simple.model.Expression;
import dev.dietermai.wincalc.core.simple.model.IdleExpression;
import dev.dietermai.wincalc.core.simple.model.NumberExpression;
import dev.dietermai.wincalc.core.simple.model.ResultType;
import dev.dietermai.wincalc.core.simple.model.Result;
import dev.dietermai.wincalc.core.simple.model.UnaryExpression;
import dev.dietermai.wincalc.core.simple.model.UnaryOperator;

class SimpleCalculatorTest {

	private SimpleCalculator calculator;

	@BeforeEach
	public void beforeEach() {
		this.calculator = new SimpleCalculator();
	}

	@Test
	void testInitialState() {
		verify(IdleExpression.of());
		assertNull(calculator.getMemoryValue());
	}

	@Test
	void testResolveOfInitialState() {
		calculator.resolve();
		verify(equation("0", "0"));
		verifyMemory("0");
	}

	@Test
	void testInitialNumberInput() {
		calculator.number("123");
		verify("123");
		verifyMemory("123");
	}
	
	@Test
	void testNumberOnEquation() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.resolve();
		calculator.number("111");
		
		verify("111");
		verifyMemory("111");
	}
	

	@Test
	void testResolveOfInitialNumber() {
		calculator.number("123");
		calculator.resolve();

		verify(equation("123", "123"));
		verifyMemory("123");
	}

	@Test
	void testNumberAfterResolvedNumber() {
		calculator.number("123");
		calculator.resolve();
		calculator.number("456");

		verify("456", equation("123", "123"));
		verifyMemory("456");
	}

	@Test
	void testReloveOfNumberAfterResolvedNumber() {
		calculator.number("123");
		calculator.resolve();
		calculator.number("456");

		calculator.resolve();

		verify(equation("456", "456"));
		verifyMemory("456");
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		calculator.plus();

		verify(expression("0", BiOperator.plus));
		verifyMemory("0");
	}

	@Test
	void testResolveOfInitialPlus() {
		calculator.plus();

		calculator.resolve();

		verify(equation(expression("0", BiOperator.plus, "0"), "0"));
		verifyMemory("0");
	}

	@Test
	void testPlusAfterNumber() {
		calculator.number("123");

		calculator.plus();

		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testPlusAfterPlus() {
		calculator.number("123");
		calculator.plus();
		calculator.plus();

		verify(expression("123", BiOperator.plus));
		verifyMemory("123");
	}

	@Test
	void testPlusAfterDifferentBinaryOperator() {
		calculator.number("123");
		calculator.minus();

		calculator.plus();

		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		calculator.number("123");
		calculator.plus();

		calculator.resolve();

		verify(equation(expression("123", BiOperator.plus, "123"), "246"));
		verifyMemory("246");
	}

	@Test
	void testSecondNumberOfPlus() {
		calculator.number("123");
		calculator.plus();

		calculator.number("456");

		verifyInput("456");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
		verifyMemory("456");
	}

	@Test
	void testPlusAfterCompletePlusExpression() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.plus();

		verify(expression("579", BiOperator.plus), equation(expression("123", BiOperator.plus, "456"), "579"));
		verifyMemory("579");
	}

	@Test
	void testResolveAfterCompletePlusEquation() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");

		calculator.resolve();

		verify(equation(expression("1", BiOperator.plus, "2"), "3"));
		verifyMemory("3");
	}

	@Test
	void testResolveAfterResolvedPlusExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();

		calculator.resolve();

		verify(equation(expression("3", BiOperator.plus, "2"), "5"));
		verifyMemory("5");
	}

	@Test
	void testPlusAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.plus();

		verify(expression("3", BiOperator.plus), equation(expression("1", BiOperator.plus, "2"), "3"));
		verifyMemory("3");
	}

	@Test
	void testPlusAfterErrorInResolve() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		assertThrowsExactly(IllegalStateException.class, () -> calculator.plus());
	}

	@Test
	void testPlusCompletesErrorEquation() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		calculator.plus();

		verify(equation(expression("1", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testPlusOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.plus();

		verify(expression("0", BiOperator.plus), equation(binary("5", BiOperator.plus, negate("5")), "0"));
		verifyMemory("0");
	}

	@Test
	void testPlusOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();
		calculator.plus();

		verify(expression("21", BiOperator.plus), equation(binary("20", BiOperator.plus, "1"), "21"));
		verifyMemory("21");
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		calculator.minus();

		verify(expression("0", BiOperator.minus));
		verifyMemory("0");
	}

	@Test
	void testResolveOfInitialMinus() {
		calculator.minus();
		calculator.resolve();

		verify(equation(expression("0", BiOperator.minus, "0"), "0"));
		verifyMemory("0");
	}

	@Test
	void testMinusAfterNumber() {
		calculator.number("123");
		calculator.minus();

		verify(expression("123", BiOperator.minus));
		verifyMemory("123");
	}

	@Test
	void testMinusAfterMinus() {
		calculator.number("123");
		calculator.minus();
		calculator.minus();

		verify(expression("123", BiOperator.minus));
		verifyMemory("123");
	}

	@Test
	void testMinusAfterDifferentBinaryOperator() {
		calculator.number("123");
		calculator.plus();
		calculator.minus();

		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testResloveOfMinusAfterNumberInput() {
		calculator.number("123");
		calculator.minus();
		calculator.resolve();

		verify(equation(expression("123", BiOperator.minus, "123"), "0"));
		verifyMemory("0");
	}

	@Test
	void testSecondNumberOfMinus() {
		calculator.number("123");
		calculator.minus();
		calculator.number("456");

		verifyInput("456");
		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();
		verifyMemory("456");
	}

	@Test
	void testMinusAfterCompleteMinusExpression() {
		calculator.number("123");
		calculator.minus();
		calculator.number("456");
		calculator.minus();

		verify(expression("-333", BiOperator.minus), equation(expression("123", BiOperator.minus, "456"), "-333"));
		verifyMemory("-333");
	}

	@Test
	void testResolveAfterCompleteMinusEquation() {
		calculator.number("1");
		calculator.minus();
		calculator.number("2");
		calculator.resolve();

		verify(equation(expression("1", BiOperator.minus, "2"), "-1"));
		verifyMemory("-1");
	}

	@Test
	void testResolveAfterResolvedMinusExpression() {
		calculator.number("1");
		calculator.minus();
		calculator.number("2");
		calculator.resolve();
		calculator.resolve();

		verify(equation(expression("-1", BiOperator.minus, "2"), "-3"));
		verifyMemory("-3");
	}

	@Test
	void testMinusAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.minus();

		verify(expression("3", BiOperator.minus), equation(expression("1", BiOperator.plus, "2"), "3"));
		verifyMemory("3");
	}

	@Test
	void testMinusAfterErrorInResolve() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		assertThrowsExactly(IllegalStateException.class, () -> calculator.minus());
	}

	@Test
	void testMinusCompletesErrorEquation() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.minus();

		verify(equation(expression("1", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testMinusOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.minus();

		verify(expression("0", BiOperator.minus),equation(binary("5", BiOperator.plus, negate("5")), "0"));
		verifyMemory("0");
	}

	@Test
	void testMinusOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.minus();
		calculator.number("5");
		calculator.percent();
		calculator.minus();

		verify(expression("19", BiOperator.minus), equation(binary("20", BiOperator.minus, "1"), "19"));
		verifyMemory("19");
	}

	/* **************************/
	/* Multiply related methods */
	/* **************************/
	@Test
	void testMultiplyAfterInit() {
		calculator.multiply();

		verifyExpression("0", BiOperator.multiply);
		verifyNoEquation();
		verifyMemory("0");
	}

	@Test
	void testResolveOfInitialMultiply() {
		calculator.multiply();
		calculator.resolve();

		verify(equation(expression("0", BiOperator.multiply, "0"), "0"));
		verifyMemory("0");
	}

	@Test
	void testMultiplyAfterNumber() {
		calculator.number("123");
		calculator.multiply();

		verify(expression("123", BiOperator.multiply));
		verifyMemory("123");
	}

	@Test
	void testMultiplyAfterMultiply() {
		calculator.number("123");
		calculator.multiply();
		calculator.multiply();


		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testMultiplyAfterDifferentBinaryOperator() {
		calculator.number("123");
		calculator.plus();
		calculator.multiply();

		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testResloveOfMultiplyAfterNumberInput() {
		calculator.number("123");
		calculator.multiply();
		calculator.resolve();

		verify(equation(expression("123", BiOperator.multiply, "123"), "15129"));
		verifyMemory("15129");
	}

	@Test
	void testSecondNumberOfMultiply() {
		calculator.number("123");
		calculator.multiply();
		calculator.number("456");

		verifyInput("456");
		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();
		verifyMemory("456");
	}

	@Test
	void testMultiplyAfterCompleteMultiplyExpression() {
		calculator.number("123");
		calculator.multiply();
		calculator.number("456");
		calculator.multiply();

		verify(expression("56088", BiOperator.multiply), equation(expression("123", BiOperator.multiply, "456"), "56088"));
		verifyMemory("56088");
	}

	@Test
	void testResolveAfterCompleteMultiplyEquation() {
		calculator.number("1");
		calculator.multiply();
		calculator.number("2");
		calculator.resolve();

		verify(equation(expression("1", BiOperator.multiply, "2"), "2"));
		verifyMemory("2");
	}

	@Test
	void testResolveAfterResolvedMultiplyExpression() {
		calculator.number("1");
		calculator.multiply();
		calculator.number("2");
		calculator.resolve();
		calculator.resolve();

		verify(equation(expression("2", BiOperator.multiply, "2"), "4"));
		verifyMemory("4");
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
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		assertThrowsExactly(IllegalStateException.class, () -> calculator.multiply());
	}

	@Test
	void testMultiplyCompletesErrorEquation() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.multiply();

		verify(equation(expression("1", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testMultiplyOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.multiply();

		verify(expression("0", BiOperator.multiply), equation(expression("5", BiOperator.plus, negate("5")), "0"));
		verifyMemory("0");
	}

	@Test
	void testMultiplyOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.multiply();
		calculator.number("5");
		calculator.percent();
		calculator.multiply();

		verify(expression("1", BiOperator.multiply), equation(binary("20", BiOperator.multiply, "0.05"), "1"));
		verifyMemory("1");
	}

	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		calculator.divide();

		verifyExpression("0", BiOperator.divide);
		verifyNoEquation();
		verifyMemory("0");
	}

	@Test
	void testResolveOfInitialDivide() {
		calculator.divide();
		calculator.resolve();

		verify(equation(expression("0", BiOperator.divide, "0"), ResultType.UNDEFINED));
	}

	@Test
	void testDivideAfterNumber() {
		calculator.number("123");
		calculator.divide();

		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testDivideAfterDivide() {
		calculator.number("123");
		calculator.divide();
		calculator.divide();

		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();
		verifyMemory("123");
	}

	@Test
	void testDivideAfterDifferentBinaryOperator() {
		calculator.number("123");
		calculator.plus();
		calculator.divide();

		verify(expression("123", BiOperator.divide));
		verifyMemory("123");
	}

	@Test
	void testResloveOfDivideAfterNumberInput() {
		calculator.number("123");
		calculator.divide();
		calculator.resolve();

		verify(equation(expression("123", BiOperator.divide, "123"), "1"));
		verifyMemory("1");
	}

	@Test
	void testSecondNumberOfDivide() {
		calculator.number("123");
		calculator.divide();
		calculator.number("456");

		verifyInput("456");
		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();
		verifyMemory("456");
	}

	@Test
	void testDivideAfterCompleteDivideExpression() {
		calculator.number("100");
		calculator.divide();
		calculator.number("8");
		calculator.divide();

		verify(expression("12.5", BiOperator.divide), equation(expression("100", BiOperator.divide, "8"), "12.5"));
		verifyMemory("12.5");
	}

	@Test
	void testResolveAfterCompleteDivideEquation() {
		calculator.number("1");
		calculator.divide();
		calculator.number("2");
		calculator.resolve();

		verify(equation(expression("1", BiOperator.divide, "2"), "0.5"));
		verifyMemory("0.5");
	}

	@Test
	void testResolveAfterResolvedDivideExpression() {
		calculator.number("1");
		calculator.divide();
		calculator.number("2");
		calculator.resolve();
		calculator.resolve();

		verify(equation(expression("0.5", BiOperator.divide, "2"), "0.25"));
		verifyMemory("0.25");
	}

	@Test
	void testDivideBy0TestTriggerdViaBinaryOperator() {
		calculator.number("100");
		calculator.divide();
		calculator.number("0");
		calculator.divide();

		verify(equation(expression("100", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testDivideBy0TestTriggerdViaResolve() {
		calculator.number("100");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		verify(equation(expression("100", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testdivideAfterResolvedExpression() {
		calculator.number("1");
		calculator.plus();
		calculator.number("2");
		calculator.resolve();
		calculator.divide();

		verify(expression("3", BiOperator.divide), equation(expression("1", BiOperator.plus, "2"), "3"));
		verifyMemory("3");
	}

	@Test
	void testDivideAfterErrorInResolve() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();

		assertThrowsExactly(IllegalStateException.class, () -> calculator.divide());
	}

	@Test
	void testDivideCompletesErrorEquation() {
		calculator.number("1");
		calculator.divide();
		calculator.number("0");

		calculator.divide();

		verify(equation(expression("1", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testDivideOnCompletedButPendingBinaryExpression() {
		calculator.number("5");
		calculator.plus();
		calculator.square();
		calculator.divide();
		
		verify(expression("30", BiOperator.divide), equation(binary("5", BiOperator.plus, unary(UnaryOperator.square, "5")), "30"));
		verifyMemory("30");
	}

	@Test
	void testDivideOnCompletedBinaryWithTwoNumbers() {
		calculator.number("20");
		calculator.divide();
		calculator.number("5");
		calculator.percent();
		calculator.divide();

		verify(expression("400", BiOperator.divide), equation(binary("20", BiOperator.divide, "0.05"), "400"));
		verifyMemory("400");
	}

	/* ************************/
	/* Negate related methods */
	/* ************************/
	@Test
	void testInitialNegate() {
		calculator.negate();

		verifyNoEquation();
		verifyMemory("0");
	}

	@Test
	void testNegateOfPositiveNumber() {
		calculator.number("123");
		calculator.negate();

		verify("-123");
		verifyMemory("-123");
	}

	@Test
	void testNegateOfNegativeNumber() {
		calculator.number("-123");
		calculator.negate();
		verifyInput("123");

		verifyNoEquation();
		verifyMemory("123");

	}

	@Test
	void testNegateOfZero() {
		calculator.number("0");
		calculator.negate();
		verifyInput("0");

		verifyNoEquation();
		verifyMemory("0");
	}

	@Test
	void testNegateOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.negate();

		verify(expression(UnaryOperator.negate, "0"), equation("0", "0"));
		verifyMemory("0");
	}

	@Test
	void testNegateOfResult() {
		calculator.number("5");
		calculator.resolve();
		calculator.negate();

		verify(expression(UnaryOperator.negate, "5"), equation("5", "5"));
		verifyMemory("-5");
	}

	@Test
	void testNegateOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.negate();
		calculator.negate();

		verify(expression(UnaryOperator.negate, unary(UnaryOperator.negate, "5")), equation("5", "5"));
		verifyMemory("5");
	}

	@Test
	void testNegateOfBinaryLeft() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.negate, "5")));
		verifyNoEquation();
		verifyMemory("-5");

	}

	@Test
	void testNegateOfBinaryRight() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.negate();

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.negate, unary(UnaryOperator.negate, "5"))));
		verifyNoEquation();
		verifyMemory("5");
	}

	@Test
	void testNegateOfError() {
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
		calculator.number("-123");
		calculator.percent();

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
		calculator.number("5");
		calculator.plus();
		calculator.percent();

		verifyExpression(binary("5", BiOperator.plus, "0.25"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfBinaryUnaryRight() {
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.percent();

		verifyExpression(binary("5", BiOperator.plus, "-0.25"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfPlusRightNumber() {
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();

		verifyExpression(binary("20", BiOperator.plus, "1"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfMinusRightNumber() {
		calculator.number("20");
		calculator.minus();
		calculator.number("5");
		calculator.percent();

		verifyExpression(binary("20", BiOperator.minus, "1"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfMultiplyRightNumber() {
		calculator.number("20");
		calculator.multiply();
		calculator.number("5");
		calculator.percent();

		verifyExpression(binary("20", BiOperator.multiply, "0.05"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfDivideRightNumber() {
		calculator.number("20");
		calculator.divide();
		calculator.number("5");
		calculator.percent();

		verifyExpression(binary("20", BiOperator.divide, "0.05"));
		verifyNoEquation();

	}

	@Test
	void testPercentOfError() {
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.percent());

		verify(equation(expression("5", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));

	}

	@Test
	void testPercentBinaryWithInput() {
		calculator.number("20");
		calculator.plus();
		calculator.number("5");
		calculator.percent();

		verify(expression("20", BiOperator.plus, "1"));
	}

	/* ************************/
	/* Square related methods */
	/* ************************/
	@Test
	void testInitialSquare() {
		calculator.square();

		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfPositiveNumber() {
		calculator.number("123");
		calculator.square();

		verify(expression(UnaryOperator.square, "123"));
	}

	@Test
	void testSquareOfNegativeNumber() {
		calculator.number("-123");
		calculator.square();

		verify(expression(UnaryOperator.square, "-123"));
	}

	@Test
	void testSquareOfZero() {
		calculator.number("0");
		calculator.square();

		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.square();

		verify(expression(UnaryOperator.square, "0"));
	}

	@Test
	void testSquareOfResult() {
		calculator.number("5");
		calculator.resolve();
		calculator.square();

		verify(expression(UnaryOperator.square, "5"));
	}

	@Test
	void testSquareOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.square();
		calculator.square();

		verifyExpression(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")));
		verifyNoEquation();
		verify(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")));
	}

	@Test
	void testSquareOfBinaryLeft() {
		calculator.number("5");
		calculator.plus();
		calculator.square();

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.square, "5")));
		verifyNoEquation();
	}

	@Test
	void testSquareOfBinaryRight() {
		calculator.number("5");
		calculator.plus();
		calculator.square();
		calculator.square();

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.square, unary(UnaryOperator.square, "5"))));
		verifyNoEquation();
	}

	@Test
	void testSquareOfError() {
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.square());

		verify(equation(expression("5", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testSquareOfRoot() {
		calculator.number("5");
		calculator.square();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.square, "5"), "25"));
	}

	@Test
	void testSquareOfNestedRoot() {
		calculator.number("5");
		calculator.square();
		calculator.square();
		calculator.resolve();

		verify(equation(expression(UnaryOperator.square, unary(UnaryOperator.square, "5")), "625"));
	}

	/* **********************/
	/* Root related methods */
	/* **********************/
	@Test
	void testInitialRoot() {
		calculator.root();

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfPositiveNumber() {
		calculator.number("123");
		calculator.root();

		verifyExpression(UnaryOperator.root, "123");
		verifyNoEquation();
	}

	@Test
	void testRootOfNegativeNumber() {
		calculator.number("123");
		calculator.negate();
		calculator.root();

		verifyExpression(UnaryOperator.root, "-123");
		verifyNoEquation();
		verifyError(ResultType.INVALID_INPUT);
	}

	@Test
	void testRootOfZero() {
		calculator.number("0");
		calculator.root();

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.root();

		verifyExpression(UnaryOperator.root, "0");
		verifyNoEquation();
	}

	@Test
	void testRootOfResult() {
		calculator.number("5");
		calculator.resolve();
		calculator.root();

		verifyExpression(UnaryOperator.root, "5");
		verifyNoEquation();
	}

	@Test
	void testRootOfUnaryExpression() {
		calculator.number("5");
		calculator.resolve();
		calculator.root();
		calculator.root();

		verifyExpression(expression(UnaryOperator.root, unary(UnaryOperator.root, "5")));
		verifyNoEquation();
	}

	@Test
	void testRootOfBinaryLeft() {
		calculator.number("5");
		calculator.plus();
		calculator.root();

		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.root, "5")));
		verifyNoEquation();
	}

	@Test
	void testRootOfBinaryRight() {
		calculator.number("5");
		calculator.plus();
		calculator.root();
		calculator.root();

		verifyExpression(binary("5", BiOperator.plus, expression(UnaryOperator.root, unary(UnaryOperator.root, "5"))));
		verifyNoEquation();
	}

	@Test
	void testRootOfError() {
		calculator.number("5");
		calculator.divide();
		calculator.number("0");
		calculator.resolve();
		assertThrowsExactly(IllegalStateException.class, () -> calculator.root());

		verify(equation(expression("5", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
	}

	@Test
	void testResolveOfRoot() {
		calculator.number("25");
		calculator.root();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.root, "25"), "5"));
	}

	@Test
	void testResolveOfNestedRoot() {
		calculator.number("625");
		calculator.root();
		calculator.root();
		calculator.resolve();

		verify(equation(expression(UnaryOperator.root, unary(UnaryOperator.root, "625")), "5"));
	}

	@Test
	void testResolveOfRootIrrationalResult() {
		calculator.number("2");
		calculator.root();
		calculator.resolve();

		verify(equation(unary(UnaryOperator.root, "2"), "1.414213562373095"));
	}


	/* *************************/
	/* OneDivX related methods */
	/* *************************/
	@Test
	void testOneDivXOfPositiveNumber() {
		calculator.number("123");
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "123");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfNegativeNumber() {
		calculator.number("123");
		calculator.negate();
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "-123");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfZero() {
		calculator.number("0");
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "0");
		verifyNoEquation();
	}

	@Test
	void testOneDivXOfZeroResult() {
		calculator.number("0");
		calculator.resolve();
		calculator.oneDivX();

		verifyExpression(UnaryOperator.oneDivX, "0");
		verifyNoEquation();
		verifyError(ResultType.DIVIDE_BY_ZERO);
	}

	@Test
	void testOneDivXOfResult() {
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

		verify(equation(expression("5", BiOperator.divide, "0"), ResultType.DIVIDE_BY_ZERO));
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
		verifyError(ResultType.DIVIDE_BY_ZERO);
	}
	
	/* ********************/
	/* CE related methods */
	/* ********************/

	@Test
	void testCeOnInitialState() {
		calculator.ce();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCeOnInitialInput() {
		calculator.number("123");
		calculator.ce();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCeOnStartedExpression() {
		calculator.number("123");
		calculator.plus();
		calculator.ce();
		
		verify(expression("123", BiOperator.plus));
	}
	
	@Test
	void testCeOnStartedExpressionWithNumberInput() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.ce();
		
		verify(expression("123", BiOperator.plus));
	}
	
	@Test
	void testCeOnEquation() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.resolve();
		calculator.ce();
		
		verify(equation(expression("123", BiOperator.plus, "456"),  "579"));
	}
	
	@Test
	void testCeOnEquationWithNumberInput() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.resolve();
		calculator.number("111");
		calculator.ce();
		
		verify(IdleExpression.of());
	}
	
	/* *******************/
	/* C related methods */
	/* *******************/
	@Test
	void testCOnInitialState() {
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCOnInitialInput() {
		calculator.number("123");
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCOnStartedExpression() {
		calculator.number("123");
		calculator.plus();
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCOnStartedExpressionWithNumberInput() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCOnEquation() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.resolve();
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	@Test
	void testCOnEquationWithNumberInput() {
		calculator.number("123");
		calculator.plus();
		calculator.number("456");
		calculator.resolve();
		calculator.number("111");
		calculator.c();
		
		verify(IdleExpression.of());
	}
	
	
	/* ********************/
	/* MS related methods */
	/* ********************/
	
	@Test
	void testMsOnInput() {
		calculator.number("123");
		assertNull(calculator.getMemoryValue());
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
		verifyError(ResultType.OK);
	}

	private void verify(Expression expression) {
		verifyInput("");
		verifyExpression(expression);
		verifyNoEquation();
		verifyError(ResultType.OK);
	}

	private void verify(Expression expression, Equation equation) {
		verifyInput("");
		verifyExpression(expression);
		verifyEquation(equation);
		verifyError(ResultType.OK);
	}

	private void verify(Equation equation) {
		verifyInput("");
		verifyExpression(IdleExpression.of());
		verifyEquation(equation);
		verifyError(equation.error());
	}

	private void verify(String input, Equation equation) {
		verifyInput(input);
		verifyExpression(IdleExpression.of());
		verifyEquation(equation);
		verifyError(equation.error());
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

	private void verifyError(ResultType error) {
		assertEquals(error, calculator.getState().lastResolve());
	}

	private void verifyMemory(String expected) {
		calculator.ms();
		assertEquals(bd(expected), calculator.getMemoryValue());
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

	private Equation equation(Expression expression, ResultType type) {
		return Equation.of(expression, Result.of(type));
	}
	
}
