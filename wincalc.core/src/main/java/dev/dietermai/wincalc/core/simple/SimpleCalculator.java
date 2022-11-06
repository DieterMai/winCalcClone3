package dev.dietermai.wincalc.core.simple;

import dev.dietermai.wincalc.core.simple.model.SimpleCalculatorRecord;

public class SimpleCalculator {
	private SimpleCalculatorRecord state = SimpleCalculatorRecord.of();

	public SimpleCalculatorRecord getState() {
		return state;
	}

	public void resolve() {
		state = SimpleCalculatorBl.resolve(state);
	}

	public void number(String number) {
		state = SimpleCalculatorBl.number(state, number);
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

	public void square() {
		state = SimpleCalculatorBl.square(state);
	}

	public void root() {
		state = SimpleCalculatorBl.root(state);
	}

	public void oneDivX() {
		state = SimpleCalculatorBl.oneDivX(state);
	}
}
