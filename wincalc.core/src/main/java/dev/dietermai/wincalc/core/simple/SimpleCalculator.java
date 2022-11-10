package dev.dietermai.wincalc.core.simple;

import java.util.List;

import dev.dietermai.wincalc.core.simple.model.Equation;
import dev.dietermai.wincalc.core.simple.model.SimpleCalculatorRecord;

/**
 * State-full simple calculator.
 */
public class SimpleCalculator {
	private SimpleCalculatorRecord state = SimpleCalculatorRecord.of();
	private EquationHistory equationHistory = new EquationHistory();

	/* **********************************/
	/* Calculator state related methods */
	/* **********************************/
	public SimpleCalculatorRecord getState() {
		return state;
	}

	public void resolve() {
		state = SimpleCalculatorBl.resolve(state);
		equationHistory.addIfNewEquation(state.equation());
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

	public void ce() {
		state = SimpleCalculatorBl.ce(state);
	}
	
	public void c() {
		state = SimpleCalculatorRecord.of();
	}
	
	/* ****************************************/
	/* Equation History state related methods */
	/* ****************************************/
	public void add(Equation newEquation) {
		equationHistory.add(newEquation);
	}
	
	public List<Equation> getEquations(){
		return equationHistory.getEquations();
	}
	
	public void deleteEquation(int index) {
		equationHistory.delete(index);
	}
	
	public void clearEquationHistory() {
		equationHistory.clear();
	}
}
