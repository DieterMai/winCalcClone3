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
	private SimpleCalculatorRecord record = SimpleCalculatorRecord.initial();;
	
	
	@BeforeEach
	public void beforeEach() {
		this.calculator = new SimpleCalculator();
	}

	@Test
	void testResolveOfInitialEquation() {
		var record = calculator.resolve(this.record);

		assertIdleExpression(record);
		assertEquation(record, "0", "0");
	}

	@Test
	void testInitialNumberInput() {
		String number = "123";

		var record = calculator.number(number);

		assertExpression(record, number);
		assertNull(record.equation());
	}

	@Test
	void testResolveOfInitialNumber() {
		// Arrange
		String number = "123";
		record = calculator.number(number);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		assertEquation(record, number, number);
	}

	@Test
	void testNumberAfterResolvedNumber() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.resolve(record);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertEquation(record, number1, number1);
		assertExpression(record, number2);
	}

	@Test
	void testReloveOfNumberAfterResolvedNumber() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.resolve(record);
		record = calculator.number(number2);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		assertIdleExpression(record);
		assertEquation(record, number2, number2);
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		var record = calculator.binary(BiOperator.plus);

		// Assert
		assertExpression(record, "0", BiOperator.plus);
		verifyNoEquation(record);
	}

	@Test
	void testResolveOfPlusAfterInit() {
		// Arrange
		record = calculator.binary(BiOperator.plus);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, "0", BiOperator.plus, "0", "0");
	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		String number = "123";
		record = calculator.number(number);
		record = calculator.binary(BiOperator.plus);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number, BiOperator.plus, number, "246");
	}

	@Test
	void testResolveOfPlusDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.plus);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number1, BiOperator.plus, number2, "579");
	}

	@Test
	void testPlusUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.plus);
		record = calculator.number(number2);

		// Act
		var record = calculator.binary(BiOperator.plus);

		// Assert
		assertExpression(record, result, BiOperator.plus);
		verifyEquation(record, number1, BiOperator.plus, number2, result);
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		var record = calculator.binary(BiOperator.minus);

		// Assert
		assertExpression(record, "0", BiOperator.minus);
		verifyNoEquation(record);
	}

	@Test
	void testResolveOfMinusAfterInit() {
		// Arrange
		record = calculator.binary(BiOperator.minus);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, "0", BiOperator.minus, "0", "0");
	}

	@Test
	void testResloveOfMinusAfterNumberInput() {
		// Arrange
		String number = "123";
		record = calculator.number(number);
		record = calculator.binary(BiOperator.minus);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number, BiOperator.minus, number, "0");
	}

	@Test
	void testResolveOfMinusDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.minus);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number1, BiOperator.minus, number2, "-333");
	}

	@Test
	void testMinusUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.plus);
		record = calculator.number(number2);

		// Act
		var record = calculator.binary(BiOperator.minus);

		// Assert
		assertExpression(record, result, BiOperator.minus);
		verifyEquation(record, number1, BiOperator.plus, number2, result);
	}

	/* ***********************/
	/* Multiply related methods */
	/* ***********************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		var record = calculator.binary(BiOperator.multiply);

		// Assert
		assertExpression(record, "0", BiOperator.multiply);
		verifyNoEquation(record);
	}

	@Test
	void testResolveOfMultiplyAfterInit() {
		// Arrange
		record = calculator.binary(BiOperator.multiply);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, "0", BiOperator.multiply, "0", "0");
	}

	@Test
	void testResloveOfMultiplyAfterNumberInput() {
		// Arrange
		String number = "123";
		record = calculator.number(number);
		record = calculator.binary(BiOperator.multiply);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number, BiOperator.multiply, number, "15129");
	}

	@Test
	void testResolveOfMultiplyDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.multiply);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number1, BiOperator.multiply, number2, "56088");
	}

	@Test
	void testMultiplyUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.plus);
		record = calculator.number(number2);

		// Act
		var record = calculator.binary(BiOperator.multiply);

		// Assert
		assertExpression(record, result, BiOperator.multiply);
		verifyEquation(record, number1, BiOperator.plus, number2, result);
	}

	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		var record = calculator.binary(BiOperator.divide);

		// Assert
		assertExpression(record, "0", BiOperator.divide);
		verifyNoEquation(record);
	}

	@Test
	void testResolveOfDivideAfterInit() {
		// Arrange
		record = calculator.binary(BiOperator.divide);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, "0", BiOperator.divide, "0", ResolveType.UNDEFINED);
	}

	@Test
	void testResloveOfDivideAfterNumberInput() {
		// Arrange
		String number = "123";
		record = calculator.number(number);
		record = calculator.binary(BiOperator.divide);

		// Act
		var record = calculator.resolve(this.record);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number, BiOperator.divide, number, "1");
	}

	@Test
	void testResolveOfDivideDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.divide);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number1, BiOperator.divide, number2, "0.2697368421052632");
	}

	@Test // TODO
	void testResolveOfDivideByZero() {
		// Arrange
		String number1 = "123";
		String number2 = "0";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.divide);

		// Act
		var record = calculator.number(number2);

		// Assert
		assertIdleExpression(record);
		verifyEquation(record, number1, BiOperator.divide, number2, ResolveType.DIVIDE_BY_ZERO);
	}

	@Test
	void testDivideUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		record = calculator.number(number1);
		record = calculator.binary(BiOperator.plus);
		record = calculator.number(number2);

		// Act
		var record = calculator.binary(BiOperator.divide);

		// Assert
		assertExpression(record, result, BiOperator.divide);
		verifyEquation(record, number1, BiOperator.plus, number2, result);

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
