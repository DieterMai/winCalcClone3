package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
		verifyInput("");
		verifyIdleExpression();
		assertNull(calculator.getState().equation());
	}

	@Test
	void testResolveOfInitialState() {
		calculator.resolve();

		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}

	@Test
	void testInitialNumberInput() {
		String number = "123";

		calculator.number(number);

		verifyInput("123");
		verifyIdleExpression();
		assertNull(calculator.getState().equation());
	}

	@Test
	void testResolveOfInitialNumber() {
		// Arrange
		String number = "123";
		calculator.number(number);

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation(number, number);
	}

	@Test
	void testNumberAfterResolvedNumber() {
		// Arrange
		calculator.number("123");
		calculator.resolve();

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyIdleExpression();
		verifyEquation("123", "123");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("456", "456");
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyInput("");
		verifyExpression("0", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testResolveOfInitialPlus() {
		// Arrange
		calculator.binary(BiOperator.plus);

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", BiOperator.plus, "0", "0");
	}

	@Test
	void testPlusAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterPlus() {
		// Arrange
		calculator.number("123");
		calculator.binary(BiOperator.plus);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.binary(BiOperator.minus);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.binary(BiOperator.plus);

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("123", BiOperator.plus, "123", "246");
	}

	@Test
	void testSecondNumberOfPlus() {
		// Arrange
		calculator.number("123");
		calculator.binary(BiOperator.plus);

		// Act
		calculator.number("456");

		// Assert
		verifyInput("456");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterCompletePlusExpression() {
		// Arrange
		calculator.number("123");
		calculator.binary(BiOperator.plus);
		calculator.number("456");

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyInput("");
		verifyExpression("579", BiOperator.plus);
		verifyEquation("123", BiOperator.plus, "456", "579");
	}

	@Test
	void testResolveAfterCompletePlusEquation() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.plus);
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("1", BiOperator.plus, "2", "3");
	}
	
	@Test
	void testResolveAfterResolvedPlusExpression() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.plus);
		calculator.number("2");
		calculator.resolve();

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("3", BiOperator.plus, "2", "5");
	}

//	/* ***********************/
//	/* Minus related methods */
//	/* ***********************/
//	@Test
//	void testMinusAfterInit() {
//		// Act
//		calculator.binary(BiOperator.minus);
//
//		// Assert
//		verifyExpression("0", BiOperator.minus);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResolveOfMinusAfterInit() {
//		// Arrange
//		calculator.binary(BiOperator.minus);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("0", BiOperator.minus, "0", "0");
//	}
//
//	@Test
//	void testMinusAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//
//		// Act
//		calculator.binary(BiOperator.minus);
//
//		// Assert
//		verifyExpression(number, BiOperator.minus);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testMinusAfterMinus() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.minus);
//
//		// Act
//		calculator.binary(BiOperator.minus);
//
//		// Assert
//		verifyExpression(number, BiOperator.minus);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testMinusAfterPlus() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.plus);
//
//		// Act
//		calculator.binary(BiOperator.minus);
//
//		// Assert
//		verifyExpression(number, BiOperator.minus);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResloveOfMinusAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.minus);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number, BiOperator.minus, number, "0");
//	}
//
//	@Test
//	void testResolveOfMinusDueToNumberInput() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		calculator.number(number1);
//		calculator.binary(BiOperator.minus);
//
//		// Act
//		calculator.number(number2);
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number1, BiOperator.minus, number2, "-333");
//	}
//
//	@Test
//	void testMinusUsesResultOfPreviousEquation() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		String result = "579";
//		calculator.number(number1);
//		calculator.binary(BiOperator.plus);
//		calculator.number(number2);
//
//		// Act
//		calculator.binary(BiOperator.minus);
//
//		// Assert
//		verifyExpression(result, BiOperator.minus);
//		verifyEquation(number1, BiOperator.plus, number2, result);
//	}
//
//	@Test
//	void testResolveAfterResolvedMinusEquation() {
//		// Arrange
//		calculator.number("1");
//		calculator.binary(BiOperator.minus);
//		calculator.number("2");
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("-1", BiOperator.minus, "2", "-3");
//	}
//
//	/* ***********************/
//	/* Multiply related methods */
//	/* ***********************/
//	@Test
//	void testMultiplyAfterInit() {
//		// Act
//		calculator.binary(BiOperator.multiply);
//
//		// Assert
//		verifyExpression("0", BiOperator.multiply);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResolveOfMultiplyAfterInit() {
//		// Arrange
//		calculator.binary(BiOperator.multiply);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("0", BiOperator.multiply, "0", "0");
//	}
//
//	@Test
//	void testMultiplyAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//
//		// Act
//		calculator.binary(BiOperator.multiply);
//
//		// Assert
//		verifyExpression(number, BiOperator.multiply);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testMultiplyAfterMultiply() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.multiply);
//
//		// Act
//		calculator.binary(BiOperator.multiply);
//
//		// Assert
//		verifyExpression(number, BiOperator.multiply);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testMultiplyAfterPlus() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.plus);
//
//		// Act
//		calculator.binary(BiOperator.multiply);
//
//		// Assert
//		verifyExpression(number, BiOperator.multiply);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResloveOfMultiplyAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.multiply);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number, BiOperator.multiply, number, "15129");
//	}
//
//	@Test
//	void testResolveOfMultiplyDueToNumberInput() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		calculator.number(number1);
//		calculator.binary(BiOperator.multiply);
//
//		// Act
//		calculator.number(number2);
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number1, BiOperator.multiply, number2, "56088");
//	}
//
//	@Test
//	void testMultiplyUsesResultOfPreviousEquation() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		String result = "579";
//		calculator.number(number1);
//		calculator.binary(BiOperator.plus);
//		calculator.number(number2);
//
//		// Act
//		calculator.binary(BiOperator.multiply);
//
//		// Assert
//		verifyExpression(result, BiOperator.multiply);
//		verifyEquation(number1, BiOperator.plus, number2, result);
//	}
//
//	@Test
//	void testResolveAfterResolvedMultiplyEquation() {
//		// Arrange
//		calculator.number("1");
//		calculator.binary(BiOperator.multiply);
//		calculator.number("2");
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("2", BiOperator.multiply, "2", "4");
//	}
//
//	/* ************************/
//	/* Divide related methods */
//	/* ************************/
//	@Test
//	void testDivideAfterInit() {
//		// Act
//		calculator.binary(BiOperator.divide);
//
//		// Assert
//		verifyExpression("0", BiOperator.divide);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResolveOfDivideAfterInit() {
//		// Arrange
//		calculator.binary(BiOperator.divide);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("0", BiOperator.divide, "0", ResolveType.UNDEFINED);
//	}
//
//	@Test
//	void testDivideAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//
//		// Act
//		calculator.binary(BiOperator.divide);
//
//		// Assert
//		verifyExpression(number, BiOperator.divide);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testDivideAfterDivide() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.divide);
//
//		// Act
//		calculator.binary(BiOperator.divide);
//
//		// Assert
//		verifyExpression(number, BiOperator.divide);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testDivideAfterPlus() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.plus);
//
//		// Act
//		calculator.binary(BiOperator.divide);
//
//		// Assert
//		verifyExpression(number, BiOperator.divide);
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResloveOfDivideAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.divide);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number, BiOperator.divide, number, "1");
//	}
//
//	@Test
//	void testResolveOfDivideDueToNumberInput() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		calculator.number(number1);
//		calculator.binary(BiOperator.divide);
//
//		// Act
//		calculator.number(number2);
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number1, BiOperator.divide, number2, "0.2697368421052632");
//	}
//
//	@Test
//	void testResolveOfDivideByZero() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "0";
//		calculator.number(number1);
//		calculator.binary(BiOperator.divide);
//
//		// Act
//		calculator.number(number2);
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(number1, BiOperator.divide, number2, ResolveType.DIVIDE_BY_ZERO);
//	}
//
//	@Test
//	void testDivideUsesResultOfPreviousEquation() {
//		// Arrange
//		String number1 = "123";
//		String number2 = "456";
//		String result = "579";
//		calculator.number(number1);
//		calculator.binary(BiOperator.plus);
//		calculator.number(number2);
//
//		// Act
//		calculator.binary(BiOperator.divide);
//
//		// Assert
//		verifyExpression(result, BiOperator.divide);
//		verifyEquation(number1, BiOperator.plus, number2, result);
//
//	}
//
//	@Test
//	void testResolveAfterResolvedDivideEquation() {
//		// Arrange
//		calculator.number("1");
//		calculator.binary(BiOperator.divide);
//		calculator.number("2");
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation("0.5", BiOperator.divide, "2", "0.25");
//	}
//	
//	/* ***********************/
//	/* Negate related methods */
//	/* ***********************/
//	@Test
//	void testNegateAfterInit() {
//		// Act
//		calculator.unary(UnaryOperator.negate);
//
//		// Assert
//		verifyExpression(UnaryOperator.negate, "0");
//		verifyNoEquation();
//	}
//
//	@Test
//	void testResolveOfNegateAfterInit() {
//		// Arrange
//		calculator.unary(UnaryOperator.negate);
//
//		// Act
//		calculator.resolve();
//
//		// Assert
//		verifyIdleExpression();
//		verifyEquation(UnaryOperator.negate, "0", "0");
//	}
//
//	@Test
//	void testNegateAfterNumberInput() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//
//		// Act
//		calculator.unary(UnaryOperator.negate);
//
//		// Assert
//		verifyNumberExpression("-123");
//		verifyNoEquation();
//	}
//	
//	@Test
//	void testNegateAfterBinary() {
//		// Arrange
//		String number = "123";
//		calculator.number(number);
//		calculator.binary(BiOperator.plus);
//
//		// Act
//		calculator.unary(UnaryOperator.negate);
//
//		// Assert
//		verifyExpression("123", BiOperator.plus, unary(UnaryOperator.negate, "-123"));
//		verifyNoEquation();
//	}

	
	private void verifyInput(String expected) {
		assertEquals(expected, calculator.getState().input());
	}

	private void verifyIdleExpression() {
		assertEquals(IdleExpression.of(), getExpression());
	}

	private void verifyExpression(String number) {
		assertEquals(number(number), getExpression());
	}

	private void verifyExpression(String number, BiOperator operator) {
		assertEquals(BinaryExpression.of(bd(number), operator), getExpression());
	}
	
	private void verifyExpression(String number, BiOperator operator, UnaryExpression right) {
//		assertEquals(BinaryExpression.of(bd(number), operator, right), getExpression());
	}
	
	
	private void verifyExpression(UnaryOperator operator, String number) {
		assertEquals(UnaryExpression.of(operator, bd(number)), getExpression());
	}

	private void verifyNumberExpression(String number) {
		assertEquals(number(number), getExpression());
	}

	private void verifyNoEquation() {
		assertNull(getEquation());
	}

	private void verifyEquation(String left, String right) {
		assertEquals(Equation.of(number(left), Result.of(bd(right))), getEquation());
	}

	private void verifyEquation(String left, BiOperator operator, String right, String result) {
		assertEquals(Equation.of(BinaryExpression.of(bd(left), operator, bd(right)), Result.of(bd(result))), getEquation());
	}

	private void verifyEquation(String left, BiOperator operator, String right, ResolveType type) {
		assertEquals(Equation.of(BinaryExpression.of(bd(left), operator, bd(right)), Result.of(type)), getEquation());
		
	}
	
	private void verifyEquation(UnaryOperator operator, String value, String result) {
		assertEquals(Equation.of(UnaryExpression.of(operator, bd(value)), Result.of((bd(result)))), getEquation());
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
	
	private UnaryExpression unary(UnaryOperator operator, String number) {
		return UnaryExpression.of(operator, bd(number));
	}
	
}
