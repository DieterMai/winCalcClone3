package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.SimpleCalculator;
import dev.dietermai.wincalc.core.simple.SimpleEquation;
import dev.dietermai.wincalc.core.simple.SimpleExpression;
import dev.dietermai.wincalc.core.simple.SimpleNumberExpression;

class SimpleCalculatorTest {

	private SimpleCalculator simple;
	
	@BeforeEach
	public void beforeEach() {
		this.simple = new SimpleCalculator();
	}
	
	@Test
	void testInitialState() {
		SimpleExpression expression = simple.getExpression();
		
		assertEquals(BigDecimal.ZERO, expression.value());
		assertNull(simple.getPreviousEquation());
	}
	
	
	@Test
	void testResolveOfInitialEquation() {
		simple.resolve();
		
		SimpleExpression expression = simple.getExpression();
		SimpleEquation equation = simple.getPreviousEquation();

		assertEquals(BigDecimal.ZERO, expression.value());
		assertEquals(SimpleEquation.of(SimpleNumberExpression.of(BigDecimal.ZERO), BigDecimal.ZERO), equation);
	}
	
	@Test
	void testInitialNumberInput() {
		String number = "123";
		BigDecimal numberBD = bigDecimal(number);

		simple.number(number);
		simple.resolve();
		
		SimpleExpression expression = simple.getExpression();
		SimpleEquation equation = simple.getPreviousEquation();

		assertEquals(numberBD, expression.value());
		assertEquals(SimpleEquation.of(SimpleNumberExpression.of(numberBD), numberBD), equation);
		
	}
	
	
	private static BigDecimal bigDecimal(String s) {
		return BigDecimal.valueOf(Long.parseLong("123"));
	}

}
