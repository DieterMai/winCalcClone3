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
		calculator.plus();

		// Assert
		verifyInput("");
		verifyExpression("0", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testResolveOfInitialPlus() {
		// Arrange
		calculator.plus();

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
		calculator.plus();

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterPlus() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.plus();

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.plus);
		verifyNoEquation();
	}

	@Test
	void testPlusAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.plus();

		// Assert
		verifyInput("");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("123", BiOperator.plus, "123", "246");
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
		// Arrange
		calculator.number("123");
		calculator.plus();
		calculator.number("456");

		// Act
		calculator.plus();

		// Assert
		verifyInput("");
		verifyExpression("579", BiOperator.plus);
		verifyEquation("123", BiOperator.plus, "456", "579");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("1", BiOperator.plus, "2", "3");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("3", BiOperator.plus, "2", "5");
	}

	/* ***********************/
	/* Minus related methods */
	/* ***********************/
	@Test
	void testMinusAfterInit() {
		// Act
		calculator.minus();

		// Assert
		verifyInput("");
		verifyExpression("0", BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testResolveOfInitialMinus() {
		// Arrange
		calculator.minus();

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", BiOperator.minus, "0", "0");
	}

	@Test
	void testMinusAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.minus();

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testMinusAfterMinus() {
		// Arrange
		calculator.number("123");
		calculator.minus();

		// Act
		calculator.minus();

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.minus);
		verifyNoEquation();
	}

	@Test
	void testMinusAfterDifferentBinaryOperator() {
		// Arrange
		calculator.number("123");
		calculator.plus();

		// Act
		calculator.minus();

		// Assert
		verifyInput("");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("123", BiOperator.minus, "123", "0");
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
		// Arrange
		calculator.number("123");
		calculator.minus();
		calculator.number("456");

		// Act
		calculator.minus();

		// Assert
		verifyInput("");
		verifyExpression("-333", BiOperator.minus);
		verifyEquation("123", BiOperator.minus, "456", "-333");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("1", BiOperator.minus, "2", "-1");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("-1", BiOperator.minus, "2", "-3");
	}
	
	
	/* **************************/
	/* Multiply related methods */
	/* **************************/
	@Test
	void testMultiplyAfterInit() {
		// Act
		calculator.multiply();

		// Assert
		verifyInput("");
		verifyExpression("0", BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testResolveOfInitialMultiply() {
		// Arrange
		calculator.multiply();

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", BiOperator.multiply, "0", "0");
	}

	@Test
	void testMultiplyAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.multiply();

		// Assert
		verifyInput("");
		verifyExpression("123", BiOperator.multiply);
		verifyNoEquation();
	}

	@Test
	void testMultiplyAfterMultiply() {
		// Arrange
		calculator.number("123");
		calculator.multiply();

		// Act
		calculator.multiply();

		// Assert
		verifyInput("");
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
		verifyInput("");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("123", BiOperator.multiply, "123", "15129");
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
		// Arrange
		calculator.number("123");
		calculator.multiply();
		calculator.number("456");

		// Act
		calculator.multiply();

		// Assert
		verifyInput("");
		verifyExpression("56088", BiOperator.multiply);
		verifyEquation("123", BiOperator.multiply, "456", "56088");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("1", BiOperator.multiply, "2", "2");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("2", BiOperator.multiply, "2", "4");
	}
	
	
	/* ************************/
	/* Divide related methods */
	/* ************************/
	@Test
	void testDivideAfterInit() {
		// Act
		calculator.divide();

		// Assert
		verifyInput("");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", BiOperator.divide, "0", ResolveType.UNDEFINED);
	}

	@Test
	void testDivideAfterNumber() {
		// Arrange
		calculator.number("123");

		// Act
		calculator.divide();

		// Assert
		verifyInput("");
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
		verifyInput("");
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
		verifyInput("");
		verifyExpression("123", BiOperator.divide);
		verifyNoEquation();
	}

	@Test
	void testResloveOfDivideAfterNumberInput() {
		// Arrange
		calculator.number("123");
		calculator.divide();

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("123", BiOperator.divide, "123", "1");
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
		// Arrange
		calculator.number("100");
		calculator.divide();
		calculator.number("8");

		// Act
		calculator.divide();

		// Assert
		verifyInput("");
		verifyExpression("12.5", BiOperator.divide);
		verifyEquation("100", BiOperator.divide, "8", "12.5");
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("1", BiOperator.divide, "2", "0.5");
	}
	
	@Test
	void testResolveAfterResolvedDivideExpression() {
		// Arrange
		calculator.number("1");
		calculator.divide();
		calculator.number("2");
		calculator.resolve();

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0.5", BiOperator.divide, "2", "0.25");
	}
	
	@Test
	void testDivideBy0TestTriggerdViaBinaryOperator() {
		// Arrange
		calculator.number("100");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.divide();

		// Assert
		verifyInput("");
		verifyEquation("100", BiOperator.divide, "0", ResolveType.DIVIDE_BY_ZERO);
		verifyIdleExpression();
	}
	
	@Test
	void testDivideBy0TestTriggerdViaResolve() {
		// Arrange
		calculator.number("100");
		calculator.divide();
		calculator.number("0");

		// Act
		calculator.resolve();

		// Assert
		verifyInput("");
		verifyEquation("100", BiOperator.divide, "0", ResolveType.DIVIDE_BY_ZERO);
		verifyIdleExpression();
	}
	
	/* ************************/
	/* Negate related methods */
	/* ************************/
	@Test
	void testInitialNegate() {
		// Act
		calculator.negate();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyNoEquation();
	}
	
	@Test
	void testNegateOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.negate();

		// Assert
		verifyInput("-123");
		verifyIdleExpression();
		verifyNoEquation();
	}
	
	@Test
	void testNegateOfNegativeNumber() {
		// Act
		calculator.number("-123");
		calculator.negate();

		// Assert
		verifyInput("123");
		verifyIdleExpression();
		verifyNoEquation();
	}
	
	@Test
	void testNegateOfZero() {
		// Act
		calculator.number("0");
		calculator.negate();

		// Assert
		verifyInput("0");
		verifyIdleExpression();
		verifyNoEquation();
	}
	
	@Test
	void testNegateOfZeroResult() {
		// Act
		calculator.number("0");
		calculator.resolve();
		calculator.negate();

		// Assert
		verifyInput("");
		verifyExpression(UnaryOperator.negate, "0");
		verifyEquation("0", "0");
	}
	
	@Test
	void testNegateOfResult() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.negate();

		// Assert
		verifyInput("");
		verifyExpression(UnaryOperator.negate, "5");
		verifyEquation("5", "5");
	}
	
	@Test
	void testNegateOfUnaryExpression() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.negate();
		calculator.negate();

		// Assert
		verifyInput("");
		verifyExpression(unary(UnaryOperator.negate, unary(UnaryOperator.negate, "5")));
		verifyEquation("5", "5");
	}
	
	@Test
	void testNegateOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.negate();

		// Assert
		verifyInput("");
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
		verifyInput("");
		verifyExpression(binary("5", BiOperator.plus, unary(UnaryOperator.negate, unary(UnaryOperator.negate, "5"))));
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

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("5", BiOperator.divide, "0", ResolveType.DIVIDE_BY_ZERO);
	}
	
	
	/* *************************/
	/* Percent related methods */
	/* *************************/
	@Test
	void testPercentNegate() {
		// Act
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfPositiveNumber() {
		// Act
		calculator.number("123");
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfNegativeNumber() {
		// Act
		calculator.number("-123");
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfZero() {
		// Act
		calculator.number("0");
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfZeroResult() {
		// Act
		calculator.number("0");
		calculator.resolve();
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfResult() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfUnaryExpression() {
		// Act
		calculator.number("5");
		calculator.resolve();
		calculator.negate();
		calculator.percent();

		// Assert
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("0", "0");
	}
	
	@Test
	void testPercentOfBinaryLeft() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.percent();

		// Assert
		verifyInput("");
		verifyExpression(binary("5", BiOperator.plus, "0.25"));
		verifyNoEquation();
	}
	
	@Test
	void testPercentOfBinaryRight() {
		// Act
		calculator.number("5");
		calculator.plus();
		calculator.negate();
		calculator.percent();

		// Assert
		verifyInput("");
		verifyExpression(binary("5", BiOperator.plus, "-0.25"));
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
		verifyInput("");
		verifyIdleExpression();
		verifyEquation("5", BiOperator.divide, "0", ResolveType.DIVIDE_BY_ZERO);
	}
	
	
	private void verifyInput(String expected) {
		assertEquals(expected, calculator.getState().input());
	}

	private void verifyIdleExpression() {
		assertEquals(IdleExpression.of(), getExpression());
	}

	private void verifyExpression(Expression expected) {
		assertEquals(expected, getExpression());
	}
	
	private void verifyExpression(String number, BiOperator operator) {
		assertEquals(BinaryExpression.of(bd(number), operator), getExpression());
	}
	
	private void verifyExpression(UnaryOperator operator, String number) {
		assertEquals(UnaryExpression.of(operator, bd(number)), getExpression());
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
	
	private BinaryExpression binary(String left, BiOperator operator, Expression right) {
		return BinaryExpression.of(bd(left), operator, right);
	}
	
	private BinaryExpression binary(String left, BiOperator operator, String right) {
		return BinaryExpression.of(bd(left), operator, bd(right));
	}
	
	private UnaryExpression unary(UnaryOperator operator, Expression nested) {
		return UnaryExpression.of(operator, nested);
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
}
