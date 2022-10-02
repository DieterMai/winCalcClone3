package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;
import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.SimpleCalculatorRecord;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

// TODO add tests for multiple operations
// TODO add tests for multiple of the same operation
class SimpleCalculatorTest {

	private SimpleCalculator calculator;

	@BeforeEach
	public void beforeEach() {
		this.calculator = new SimpleCalculator();
	}

	@Test
	void testResolveOfInitialEquation() {
		calculator.resolve();

		assertIdleExpression(calculator.getState());
		assertEquation(calculator.getState(), "0", "0");
	}

	@Test
	void testInitialNumberInput() {
		String number = "123";

		calculator.number(number);

		assertExpression(calculator.getState(), number);
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
		assertIdleExpression(calculator.getState());
		assertEquation(calculator.getState(), number, number);
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
		assertEquation(calculator.getState(), number1, number1);
		assertExpression(calculator.getState(), number2);
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
		assertIdleExpression(calculator.getState());
		assertIdleExpression(calculator.getState());
		assertEquation(calculator.getState(), number2, number2);
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		assertExpression(calculator.getState(), "0", BiOperator.plus);
		verifyNoEquation(calculator.getState());
	}

	@Test
	void testResolveOfPlusAfterInit() {
		// Arrange
		calculator.binary(BiOperator.plus);

		// Act
		calculator.resolve();

		// Assert
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), "0", BiOperator.plus, "0", "0");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number, BiOperator.plus, number, "246");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number1, BiOperator.plus, number2, "579");
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
		assertExpression(calculator.getState(), result, BiOperator.plus);
		verifyEquation(calculator.getState(), number1, BiOperator.plus, number2, result);
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		assertExpression(calculator.getState(), "0", BiOperator.minus);
		verifyNoEquation(calculator.getState());
	}

	@Test
	void testResolveOfMinusAfterInit() {
		// Arrange
		calculator.binary(BiOperator.minus);

		// Act
		calculator.resolve();

		// Assert
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), "0", BiOperator.minus, "0", "0");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number, BiOperator.minus, number, "0");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number1, BiOperator.minus, number2, "-333");
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
		assertExpression(calculator.getState(), result, BiOperator.minus);
		verifyEquation(calculator.getState(), number1, BiOperator.plus, number2, result);
	}

	/* ***********************/
	/* Multiply related methods */
	/* ***********************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		assertExpression(calculator.getState(), "0", BiOperator.multiply);
		verifyNoEquation(calculator.getState());
	}

	@Test
	void testResolveOfMultiplyAfterInit() {
		// Arrange
		calculator.binary(BiOperator.multiply);

		// Act
		calculator.resolve();

		// Assert
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), "0", BiOperator.multiply, "0", "0");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number, BiOperator.multiply, number, "15129");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number1, BiOperator.multiply, number2, "56088");
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
		assertExpression(calculator.getState(), result, BiOperator.multiply);
		verifyEquation(calculator.getState(), number1, BiOperator.plus, number2, result);
	}

	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		assertExpression(calculator.getState(), "0", BiOperator.divide);
		verifyNoEquation(calculator.getState());
	}

	@Test
	void testResolveOfDivideAfterInit() {
		// Arrange
		calculator.binary(BiOperator.divide);

		// Act
		calculator.resolve();

		// Assert
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), "0", BiOperator.divide, "0", ResolveType.UNDEFINED);
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number, BiOperator.divide, number, "1");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number1, BiOperator.divide, number2, "0.2697368421052632");
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
		assertIdleExpression(calculator.getState());
		verifyEquation(calculator.getState(), number1, BiOperator.divide, number2, ResolveType.DIVIDE_BY_ZERO);
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
		assertExpression(calculator.getState(), result, BiOperator.divide);
		verifyEquation(calculator.getState(), number1, BiOperator.plus, number2, result);

	}

	private void assertIdleExpression(SimpleCalculatorRecord actual) {
		assertEquals(IdleExpression.of(), actual.expression());
	}

	private void assertExpression(SimpleCalculatorRecord actual, String number) {
		assertEquals(number(number), actual.expression());
	}

	private void assertExpression(SimpleCalculatorRecord actual, String number, BiOperator operator) {
		assertEquals(BinaryExpression.of(bd(number), operator), actual.expression());
	}

	private void verifyNoEquation(SimpleCalculatorRecord actual) {
		assertNull(actual.equation());
	}

	private void assertEquation(SimpleCalculatorRecord actual, String left, String right) {
		assertEquals(Equation.of(number(left), bd(right)), actual.equation());
	}

	private void verifyEquation(SimpleCalculatorRecord actual, String left, BiOperator operator, String right, String result) {
		assertEquals(Equation.of(BinaryExpression.of(operator, bd(left), bd(right)), bd(result)), actual.equation());
	}

	private void verifyEquation(SimpleCalculatorRecord actual, String left, BiOperator operator, String right, ResolveType type) {
		assertEquals(Equation.of(BinaryExpression.of(operator, bd(left), bd(right)), type), actual.equation());
	}

	private BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	private NumberExpression number(String s) {
		return NumberExpression.of(bd(s));
	}
}
