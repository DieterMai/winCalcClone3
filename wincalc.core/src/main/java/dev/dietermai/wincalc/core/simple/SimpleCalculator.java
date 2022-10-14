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

	public void binary(BiOperator operator) {
		state = SimpleCalculatorBl.binary(state, operator);
	}

	public void unary(UnaryOperator operator) {
		state = SimpleCalculatorBl.unary(state, operator);
	}
}
