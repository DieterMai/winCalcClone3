package dev.dietermai.wincalc.core.simple.model;

public final record IdleExpression() implements Expression {
	private static final IdleExpression instance = new IdleExpression();
	
	public static IdleExpression of() {
		return instance;
	}
}
