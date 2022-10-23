package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.expr.UnaryOperator;
import dev.dietermai.wincalc.core.simple.expr.binary.BiOperator;

public class SimpleCalculator {
	private SimpleCalculatorRecord state = SimpleCalculatorRecord.initial();

	public SimpleCalculatorRecord getState() {
		return state;
	}

	public void resolve() {
		state = SimpleCalculatorBl.resolve(state);
	}

	public void number(String number) {
		state = SimpleCalculatorBl.number(state, number);
	}

	public void unary(UnaryOperator operator) {
		state = SimpleCalculatorBl.unary(state, operator);
	}
	
	public void plus() {
		state = SimpleCalculatorBl.plus(state);
	}
	
	public void minus() {
		state = SimpleCalculatorBl.minus(state);
	}
	
	public void multiply() {
		state = SimpleCalculatorBl.multiply(state);
	}
	
	public void divide() {
		state = SimpleCalculatorBl.divide(state);
	}
	
	public void negate() {
		state = SimpleCalculatorBl.negate(state);
	}
	
	public void percent() {
		state = SimpleCalculatorBl.percent(state);
	}


}
