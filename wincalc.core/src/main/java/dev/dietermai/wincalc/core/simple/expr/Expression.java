package dev.dietermai.wincalc.core.simple.expr;

import dev.dietermai.wincalc.core.simple.expr.binary.BinaryExpression;

public sealed interface Expression 
permits IdleExpression, BinaryExpression, UnaryExpression
{
}
