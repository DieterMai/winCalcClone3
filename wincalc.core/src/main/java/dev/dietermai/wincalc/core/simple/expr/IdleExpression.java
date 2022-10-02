package dev.dietermai.wincalc.core.simple.expr;

public final class IdleExpression implements Expression {
	private static final IdleExpression instance = new IdleExpression();
	
	public static IdleExpression of() {
		return instance;
	}
}
