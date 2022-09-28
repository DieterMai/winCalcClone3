package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.PlusExpression;
import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.SimpleEquation;
import dev.dietermai.wincalc.core.simple.SimpleExpression;
import dev.dietermai.wincalc.core.simple.SimpleIdleExpression;
import dev.dietermai.wincalc.core.simple.SimpleNumberExpression;

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
	void testInitialPlus() {
		// Act
		calculator.plus();
		
		// Assert
		verifyPlusExpression("0");
		verifyNoEquation();
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
	void testResolveOfInitialPlus() {
		// Arrange
		calculator.plus();
		
		// Act
		calculator.resolve();
		
		// Assert
		verifyIdleExpression();
		verifyPlusEquation("0", "0", "0");
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
	
	
	private BigDecimal bd(String s) {
		return BigDecimal.valueOf(Long.parseLong(s));
	}
	
	private void verifyIdleExpression() {
		var expression = calculator.getExpression();
		assertEquals(SimpleIdleExpression.of(), expression);
	}
	
	private void verifyNumberExpression(String number) {
		assertEquals(SimpleNumberExpression.of(number), calculator.getExpression());
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
		assertEquals(SimpleEquation.of(SimpleNumberExpression.of(bd(number)), bd(number)), calculator.getPreviousEquation());
	}
	
	private void verifyPlusEquation(String left, String right, String result) {
		assertEquals(SimpleEquation.of(PlusExpression.of(bd(left), bd(right)), bd(result)), calculator.getPreviousEquation());
	}
}
