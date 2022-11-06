package dev.dietermai.wincalc.core.simple.model;

public sealed interface Expression 
permits IdleExpression, NumberExpression, BinaryExpression, UnaryExpression
{
}
