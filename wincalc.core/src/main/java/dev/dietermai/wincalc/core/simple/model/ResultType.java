package dev.dietermai.wincalc.core.simple.model;

/**
 * The ResultType of an resolved Expression.
 */
public enum ResultType {
	OK, DIVIDE_BY_ZERO, UNDEFINED, INVALID_INPUT;

	public boolean ok() {
		return this == OK;
	}

	public boolean error() {
		return !ok();
	}
}
