package dev.dietermai.wincalc.core.simple;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class MemoryHistory {
private final LinkedList<BigDecimal> savedValues = new LinkedList<>();
	public void add(BigDecimal newValue) {
		savedValues.addFirst(newValue);
	}
	
	public List<BigDecimal> getValues(){
		return List.copyOf(savedValues);
	}
	
	public BigDecimal getValue() {
		if(savedValues.isEmpty()) {
			return null;
		}else {
			return savedValues.getFirst();
		}
	}
	
	public void delete(int index) {
		if(index < savedValues.size()) {
			savedValues.remove(index);
		}
	}
	
	public void clear() {
		savedValues.clear();
	}
}
