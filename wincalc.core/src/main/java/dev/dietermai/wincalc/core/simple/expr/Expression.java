package dev.dietermai.wincalc.core.simple.expr;

import dev.dietermai.wincalc.core.simple.Equation;

public sealed interface Expression 
permits IdleExpression, NumberExpression, PlusExpression
{
	public Equation resolve();
}
