package dev.dietermai.wincalc.core.simple.model;

/**
 * IdleExpression is used to prevent to much null checking. This is basically a Null-Object.
 */
public final record IdleExpression() implements Expression {
	private static final IdleExpression instance = new IdleExpression();
	
	public static IdleExpression of() {
		return instance;
	}
}
