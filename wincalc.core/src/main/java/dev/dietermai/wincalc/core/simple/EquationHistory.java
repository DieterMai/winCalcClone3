package dev.dietermai.wincalc.core.simple;

import java.util.LinkedList;
import java.util.List;

import dev.dietermai.wincalc.core.simple.model.Equation;

public class EquationHistory {
	private final LinkedList<Equation> equations = new LinkedList<>();
	
	public void add(Equation newEquation) {
		equations.add(newEquation);
	}
	
	public List<Equation> getEquations(){
		return List.copyOf(equations);
	}
	
	public void delete(int index) {
		if(index < equations.size()) {
			equations.remove(index);
		}
	}
	
	public void clear() {
		equations.clear();
	}

	void addIfNewEquation(Equation equation) {
		if(equation == null || isSameAsPrevEquation(equation)) {
			return;
		}
		equations.addFirst(equation);
	}
	
	private boolean isSameAsPrevEquation(Equation equation) {
		return !equations.isEmpty() && equation == equations.getFirst();
	}
}
