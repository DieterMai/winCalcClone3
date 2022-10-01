package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;
import dev.dietermai.wincalc.core.simple.SimpleCalculator;
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
	void testInitialState() {
		verifyIdleExpression();
		assertNull(calculator.getPreviousEquation());
	}
	
	
	@Test
	void testResolveOfInitialEquation() {
		calculator.resolve();

		verifyIdleExpression();
		verifyIdentityEquation("0");
	}

	@Test
	void testInitialNumberInput() {
		String number = "123";

		calculator.number(number);

		verifyNumberExpression(number);
		assertNull(calculator.getPreviousEquation());
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
		verifyIdentityEquation(number);
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
		verifyNumberExpression(number2);
		verifyIdentityEquation(number1);
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
		verifyIdentityEquation(number2);
	}

	/* ******************** */
	/* Plus related methods */
	/* ******************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.binary(BiOperator.plus);

		// Assert
		verifyExpression(BiOperator.plus, "0");
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
		verifyEquation(BiOperator.plus, "0", "0", "0");
	}

	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.binary(BiOperator.plus);
		;

		// Act
		calculator.resolve();

		// Assert
		verifyIdleExpression();
		verifyEquation(BiOperator.plus, number, number, "246");
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
		verifyEquation(BiOperator.plus, number1, number2, "579");
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
		verifyExpression(BiOperator.plus, result);
		verifyEquation(BiOperator.plus, number1, number2, result);
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		calculator.binary(BiOperator.minus);

		// Assert
		verifyExpression(BiOperator.minus, "0");
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
		verifyEquation(BiOperator.minus, "0", "0", "0");
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
		verifyEquation(BiOperator.minus, number, number, "0");
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
		verifyEquation(BiOperator.minus, number1, number2, "-333");
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
		verifyExpression(BiOperator.minus, result);
		verifyEquation(BiOperator.plus, number1, number2, result);
	}

	
	/* ***********************/
	/* Multiply related methods */
	/* ***********************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		calculator.binary(BiOperator.multiply);

		// Assert
		verifyExpression(BiOperator.multiply, "0");
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
		verifyEquation(BiOperator.multiply, "0", "0", "0");
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
		verifyEquation(BiOperator.multiply, number, number, "15129");
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
		verifyEquation(BiOperator.multiply, number1, number2, "56088");
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
		verifyExpression(BiOperator.multiply, result);
		verifyEquation(BiOperator.plus, number1, number2, result);
	}
	
	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		calculator.binary(BiOperator.divide);

		// Assert
		verifyExpression(BiOperator.divide, "0");
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
		verifyEquation(BiOperator.divide, "0", "0", ResolveType.UNDEFINED);
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
		verifyEquation(BiOperator.divide, number, number, "1");
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
		verifyEquation(BiOperator.divide, number1, number2, "0.2697368421052632");
	}
	
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
//		verifyEquation(BiOperator.divide, number1, ResolveType.DIVIDE_BY_ZERO);
//	}

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
		verifyExpression(BiOperator.divide, result);
		verifyEquation(BiOperator.plus, number1, number2, result);
	}
	
	private BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	private void verifyIdleExpression() {
		var expression = calculator.getExpression();
		assertEquals(IdleExpression.of(), expression);
	}

	private void verifyNumberExpression(String number) {
		assertEquals(NumberExpression.of(number), calculator.getExpression());
	}

	private void verifyExpression(BiOperator operator, String left) {
		assertEquals(BinaryExpression.of(bd(left), operator), calculator.getExpression());
	}

	private void verifyNoEquation() {
		assertNull(calculator.getPreviousEquation());
	}

	private void verifyIdentityEquation(String number) {
		assertEquals(Equation.of(NumberExpression.of(bd(number)), bd(number)), calculator.getPreviousEquation());
	}

	private void verifyEquation(BiOperator operator, String left, String right, String result) {
		assertEquals(Equation.of(BinaryExpression.of(operator, bd(left), bd(right)), bd(result)), calculator.getPreviousEquation());
	}
	
	private void verifyEquation(BiOperator operator, String left, String right, ResolveType type) {
		assertEquals(Equation.of(BinaryExpression.of(operator, bd(left), bd(right)), type), calculator.getPreviousEquation());
	}
}
