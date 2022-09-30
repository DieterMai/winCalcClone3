package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;

import dev.dietermai.wincalc.core.simple.expr.Expression;

public sealed interface BinaryExpression extends Expression
permits PlusExpression, MinusExpression, MultiplyExpression, DivideExpression
{
	BinaryExpression withRight(BigDecimal right); // TODO make gerneric
}
