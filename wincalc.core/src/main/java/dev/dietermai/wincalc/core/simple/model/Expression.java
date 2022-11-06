package dev.dietermai.wincalc.core.simple.model;

/**
 * Base type of all expressions
 */
public sealed interface Expression permits IdleExpression, NumberExpression, BinaryExpression, UnaryExpression {
}
