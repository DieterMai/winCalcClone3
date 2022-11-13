package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class MemoryHistory {
	private final LinkedList<BigDecimal> savedValues = new LinkedList<>();

	public void save(BigDecimal newValue) {
		savedValues.push(newValue);
	}

	public List<BigDecimal> getValues() {
		return List.copyOf(savedValues);
	}

	public BigDecimal getValue() {
		if (savedValues.isEmpty()) {
			return null;
		} else {
			return savedValues.peek();
		}
	}

	public void delete(int index) {
		if (index < savedValues.size()) {
			savedValues.remove(index);
		}
	}

	public void clear() {
		savedValues.clear();
	}

	public void addToSaved(BigDecimal valueToAdd) {
		if (savedValues.isEmpty()) {
			savedValues.push(valueToAdd);
		} else {
			savedValues.push(savedValues.pop().add(valueToAdd));
		}
	}

	public void subtractFromSaved(BigDecimal valueToAdd) {
		if (savedValues.isEmpty()) {
			savedValues.push(valueToAdd);
		} else {
			savedValues.push(savedValues.pop().subtract(valueToAdd));
		}
	}
}
