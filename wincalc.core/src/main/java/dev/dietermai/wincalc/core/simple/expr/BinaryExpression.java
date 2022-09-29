package dev.dietermai.wincalc.core.simple.expr;

import java.math.BigDecimal;

public sealed interface BinaryExpression extends Expression
permits PlusExpression, MinusExpression
{
	BinaryExpression withRight(BigDecimal right); // TODO make gerneric
}
