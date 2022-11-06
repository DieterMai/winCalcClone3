package dev.dietermai.wincalc.core.simple.model;

public enum Error {
	NONE,
	DIVIDE_BY_ZERO,
	UNDEFINED,
	INVALID_INPUT;
	
	public boolean none() {
		return this == NONE;
	}
	
	public boolean isError() {
		return !none();
	}
}
