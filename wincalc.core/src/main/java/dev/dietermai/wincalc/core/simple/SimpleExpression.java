package dev.dietermai.wincalc.core.simple;

public sealed interface SimpleExpression 
permits SimpleIdleExpression, SimpleNumberExpression, PlusExpression
{
	public SimpleEquation resolve();
}
