package dev.dietermai.wincalc.core.simple;

public enum ResolveType {
	SUCCESS,
	DIVIDE_BY_ZERO,
	UNDEFINED,
	INVALID_INPUT;
	
	public boolean isSuccess() {
		return this == SUCCESS;
	}
	
	public boolean isError() {
		return !isSuccess();
	}
}
