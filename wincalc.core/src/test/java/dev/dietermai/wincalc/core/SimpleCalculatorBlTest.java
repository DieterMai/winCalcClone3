package dev.dietermai.wincalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import dev.dietermai.wincalc.core.simple.Result;
import dev.dietermai.wincalc.core.simple.SimpleCalculatorBl;

class SimpleCalculatorBlTest {
	
	/* ************ */
	/* negate Tests */
	/* ************ */
	@Test
	void testNegateOfPositiveValue() {
		assertEquals(result("-5"), SimpleCalculatorBl.resultNegateExpression(bd("5")));
	}

	@Test
	void testNegateOfNegativeValue() {
		assertEquals(result("5"), SimpleCalculatorBl.resultNegateExpression(bd("-5")));
	}
	
	@Test
	void testNegateOfZeroValue() {
		assertEquals(result("0"), SimpleCalculatorBl.resultNegateExpression(bd("0")));
	}
	

	private Result result(String s) {
		return Result.of(bd(s));
	}

	private BigDecimal bd(String s) {
		return new BigDecimal(s);
	}
	
	
	
}
