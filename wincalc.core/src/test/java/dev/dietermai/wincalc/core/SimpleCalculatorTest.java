package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.expr.PlusExpression;
import dev.dietermai.wincalc.core.simple.expr.IdleExpression;
import dev.dietermai.wincalc.core.simple.expr.NumberExpression;

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
	
	
	/* ****************************** */
	/*      Plus related methods      */
	/* ****************************** */
	@Test
	void testPlusAfterInit() {
		// Act
		calculator.plus();
		
		// Assert
		verifyPlusExpression("0");
		verifyNoEquation();
	}
	
	@Test
	void testResolveOfPlusAfterInit() {
		// Arrange
		calculator.plus();
		
		// Act
		calculator.resolve();
		
		// Assert
		verifyIdleExpression();
		verifyPlusEquation("0", "0", "0");
	}
	
	@Test
	void testResloveOfPlusAfterNumberInput() {
		// Arrange
		String number = "123";
		calculator.number(number);
		calculator.plus();
		
		// Act
		calculator.resolve();
		
		// Assert
		verifyIdleExpression();
		verifyPlusEquation(number, number, "246");
	}
	
	@Test
	void testResolveOfPlusDueToNumberInput() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		calculator.number(number1);
		calculator.plus();
		
		// Act
		calculator.number(number2);
		
		// Assert
		verifyIdleExpression();
		verifyPlusEquation(number1, number2, "579");
	}
	
	@Test
	void testPlusUsesResultOfPreviousEquation() {
		// Arrange
		String number1 = "123";
		String number2 = "456";
		String result = "579";
		calculator.number(number1);
		calculator.plus();
		calculator.number(number2);
		
		// Act
		calculator.plus();
		
		// Assert
		verifyPlusExpression(result);
		verifyPlusEquation(number1, number2, result);
	}
	
	
	private BigDecimal bd(String s) {
		return BigDecimal.valueOf(Long.parseLong(s));
	}
	
	private void verifyIdleExpression() {
		var expression = calculator.getExpression();
		assertEquals(IdleExpression.of(), expression);
	}
	
	private void verifyNumberExpression(String number) {
		assertEquals(NumberExpression.of(number), calculator.getExpression());
	}
	
	private void verifyPlusExpression(String left) {
		assertEquals(PlusExpression.of(bd(left)), calculator.getExpression());
	}
	
	private void verifyPlusExpression(String left, String right) {
		assertEquals(PlusExpression.of(bd(left), bd(right)), calculator.getExpression());
	}
	
	private void verifyNoEquation() {
		assertNull(calculator.getPreviousEquation());
	}
	
	private void verifyIdentityEquation(String number) {
		assertEquals(Equation.of(NumberExpression.of(bd(number)), bd(number)), calculator.getPreviousEquation());
	}
	
	private void verifyPlusEquation(String left, String right, String result) {
		assertEquals(Equation.of(PlusExpression.of(bd(left), bd(right)), bd(result)), calculator.getPreviousEquation());
	}
}
