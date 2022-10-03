package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;
import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.expr.Expression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

class SimpleCalculatorTest {

	private SimpleCalculator calculator;

	@BeforeEach
	public void beforeEach() {
		this.calculator = new SimpleCalculator();
	}

	@Test
	void testResolveOfInitialEquation() {
		calculator.resolve();

		verifyIdleExpression();
		verifyEquation("0", "0");
	}

	@Test
	void testInitialNumberInput() {
		String number = "123";

		calculator.number(number);

		verifyExpression(number);
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
		verifyIdleExpression();
		verifyEquation(number, number);
	}

	@Test
	void testNumberAfterResolvedNumber() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.resolve();

		// Act
		calculator.number(number2);

		// Assert
		verifyEquation(number1, number1);
		verifyExpression(number2);
	}

	@Test
	void testReloveOfNumberAfterResolvedNumber() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.resolve();
		calculator.number(number2);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyIdleExpression();
		verifyEquation(number2, number2);
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression("0", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testResolveOfPlusAfterInit() {
		// Arrange
		calculator.binary(BiOperator.plus);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("0", BiOperator.plus, "0", "0");
	}

	@Test
	void testPlusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression(number, BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterPlus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression(number, BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterMinus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.minus);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression(number, BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation(number, BiOperator.plus, number, "246");
	}

	@Test
	void testResolveOfPlusDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.number(number2);

		// Assert
		verifyIdleExpression();
		verifyEquation(number1, BiOperator.plus, number2, "579");
	}

	@Test
	void testPlusUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		calculator.number(number1);
		calculator.binary(BiOperator.plus);
		calculator.number(number2);

		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression(result, BiOperator.plus);
		verifyEquation(number1, BiOperator.plus, number2, result);
	}

	@Test
	void testResolveAfterResolvedPlusEquation() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.plus);
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("3", BiOperator.plus, "2", "5");
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression("0", BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testResolveOfMinusAfterInit() {
		// Arrange
		calculator.binary(BiOperator.minus);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("0", BiOperator.minus, "0", "0");
	}

	@Test
	void testMinusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);

		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression(number, BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testMinusAfterMinus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.minus);

		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression(number, BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testMinusAfterPlus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression(number, BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testResloveOfMinusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.minus);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation(number, BiOperator.minus, number, "0");
	}

	@Test
	void testResolveOfMinusDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.binary(BiOperator.minus);

		// Act
		calculator.number(number2);

		// Assert
		verifyIdleExpression();
		verifyEquation(number1, BiOperator.minus, number2, "-333");
	}

	@Test
	void testMinusUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		calculator.number(number1);
		calculator.binary(BiOperator.plus);
		calculator.number(number2);

		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression(result, BiOperator.minus);
		verifyEquation(number1, BiOperator.plus, number2, result);
	}

	@Test
	void testResolveAfterResolvedMinusEquation() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.minus);
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("-1", BiOperator.minus, "2", "-3");
	}

	/* ***********************/
	/* Multiply related methods */
	/* ***********************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression("0", BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testResolveOfMultiplyAfterInit() {
		// Arrange
		calculator.binary(BiOperator.multiply);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("0", BiOperator.multiply, "0", "0");
	}

	@Test
	void testMultiplyAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);

		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression(number, BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testMultiplyAfterMultiply() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.multiply);

		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression(number, BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testMultiplyAfterPlus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression(number, BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testResloveOfMultiplyAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.multiply);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation(number, BiOperator.multiply, number, "15129");
	}

	@Test
	void testResolveOfMultiplyDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.binary(BiOperator.multiply);

		// Act
		calculator.number(number2);

		// Assert
		verifyIdleExpression();
		verifyEquation(number1, BiOperator.multiply, number2, "56088");
	}

	@Test
	void testMultiplyUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		calculator.number(number1);
		calculator.binary(BiOperator.plus);
		calculator.number(number2);

		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression(result, BiOperator.multiply);
		verifyEquation(number1, BiOperator.plus, number2, result);
	}

	@Test
	void testResolveAfterResolvedMultiplyEquation() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.multiply);
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("2", BiOperator.multiply, "2", "4");
	}

	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression("0", BiOperator.divide);
		verifyNoEquation();
	}

	@Test
	void testResolveOfDivideAfterInit() {
		// Arrange
		calculator.binary(BiOperator.divide);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("0", BiOperator.divide, "0", ResolveType.UNDEFINED);
	}

	@Test
	void testDivideAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);

		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression(number, BiOperator.divide);
		verifyNoEquation();
	}

	@Test
	void testDivideAfterDivide() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.divide);

		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression(number, BiOperator.divide);
		verifyNoEquation();
	}

	@Test
	void testDivideAfterPlus() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);

		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression(number, BiOperator.divide);
		verifyNoEquation();
	}

	@Test
	void testResloveOfDivideAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.divide);

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation(number, BiOperator.divide, number, "1");
	}

	@Test
	void testResolveOfDivideDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.binary(BiOperator.divide);

		// Act
		calculator.number(number2);

		// Assert
		verifyIdleExpression();
		verifyEquation(number1, BiOperator.divide, number2, "0.2697368421052632");
	}

	@Test
	void testResolveOfDivideByZero() {
		// Arrange
		String number1 = "123";
		String number2 = "0";
		calculator.number(number1);
		calculator.binary(BiOperator.divide);

		// Act
		calculator.number(number2);

		// Assert
		verifyIdleExpression();
		verifyEquation(number1, BiOperator.divide, number2, ResolveType.DIVIDE_BY_ZERO);
	}

	@Test
	void testDivideUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		calculator.number(number1);
		calculator.binary(BiOperator.plus);
		calculator.number(number2);

		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression(result, BiOperator.divide);
		verifyEquation(number1, BiOperator.plus, number2, result);

	}

	@Test
	void testResolveAfterResolvedDivideEquation() {
		// Arrange
		calculator.number("1");
		calculator.binary(BiOperator.divide);
		calculator.number("2");

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation("0.5", BiOperator.divide, "2", "0.25");
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

	private void verifyNoEquation() {
		assertNull(getEquation());
	}

	private void verifyEquation(String left, String right) {
		assertEquals(Equation.of(number(left), bd(right)), getEquation());
	}

	private void verifyEquation(String left, BiOperator operator, String right, String result) {
		assertEquals(Equation.of(BinaryExpression.of(bd(left), operator, bd(right)), bd(result)), getEquation());
	}

	private void verifyEquation(String left, BiOperator operator, String right, ResolveType type) {
		assertEquals(Equation.of(BinaryExpression.of(bd(left), operator, bd(right)), type), getEquation());
	}

	private BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	private NumberExpression number(String s) {
		return NumberExpression.of(bd(s));
	}

	private Expression getExpression() {
		return calculator.getState().expression();
	}

	private Equation getEquation() {
		return calculator.getState().equation();
	}
}
