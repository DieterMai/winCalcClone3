package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.util.List;

import dev.dietermai.wincalc.core.simple.model.Equation;
import dev.dietermai.wincalc.core.simple.model.SimpleCalculatorRecord;

/**
 * State-full simple calculator.
 */
public class SimpleCalculator {
	private final EquationHistory equationHistory = new EquationHistory();
	private final MemoryHistory memory = new MemoryHistory();

	private SimpleCalculatorRecord state = SimpleCalculatorRecord.of();

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
		equationHistory.addIfNewEquation(state.equation());
	}

	public void plus() {
		state = SimpleCalculatorBl.plus(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void minus() {
		state = SimpleCalculatorBl.minus(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void multiply() {
		state = SimpleCalculatorBl.multiply(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void divide() {
		state = SimpleCalculatorBl.divide(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void negate() {
		state = SimpleCalculatorBl.negate(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void percent() {
		state = SimpleCalculatorBl.percent(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void square() {
		state = SimpleCalculatorBl.square(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void root() {
		state = SimpleCalculatorBl.root(state);
		equationHistory.addIfNewEquation(state.equation());
	}

	public void oneDivX() {
		state = SimpleCalculatorBl.oneDivX(state);
		equationHistory.addIfNewEquation(state.equation());
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

	public List<Equation> getEquations() {
		return equationHistory.getEquations();
	}

	public void deleteEquation(int index) {
		equationHistory.delete(index);
	}

	public void clearEquationHistory() {
		equationHistory.clear();
	}

	/* *************************************/
	/* Memory related methods */
	/* *************************************/
	public List<BigDecimal> getAllMemoryValues() {
		return memory.getValues();
	}

	public BigDecimal mr() {
		return memory.getValue();
	}

	public void ms() {
		memory.save(SimpleCalculatorBl.getCurrentValue(state));
	}

	public void mc() {
		memory.clear();
	}

	public void mPlus() {
		memory.addToSaved(SimpleCalculatorBl.getCurrentValue(state));
	}

	public void mMinus() {
		memory.subtractFromSaved(SimpleCalculatorBl.getCurrentValue(state));
	}
}
