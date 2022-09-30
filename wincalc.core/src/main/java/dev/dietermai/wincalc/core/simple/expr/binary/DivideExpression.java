package dev.dietermai.wincalc.core.simple.expr.binary;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dev.dietermai.wincalc.core.simple.Equation;
import dev.dietermai.wincalc.core.simple.ResolveType;

public record DivideExpression(BigDecimal left, BigDecimal right) implements BinaryExpression {
	public static DivideExpression of(BigDecimal left, BigDecimal right) {
		return new DivideExpression(left, right);
	}

	public static DivideExpression of(BigDecimal left) {
		return new DivideExpression(left, null);
	}

	@Override
	public BinaryExpression withRight(BigDecimal localRight) {
		return of(left, localRight);
	}

	@Override
	public Equation resolve() {
		if (right == null) {
			return resolve(left, left);
		} else {
			return resolve(left, right);
		}
	}

	private Equation resolve(BigDecimal localleft, BigDecimal localright) {
		if(right == null) {
			if(left.equals(BigDecimal.ZERO)) {
				return Equation.of(this, ResolveType.UNDEFINED);
			}else {
				return Equation.of(withRight(left), BigDecimal.ONE);
			}
		}else if(right.equals(BigDecimal.ZERO)){
			return Equation.of(this, ResolveType.DIVIDE_BY_ZERO);
		}else {
			return Equation.of(this, localleft.divide(localright, 16, RoundingMode.HALF_UP).stripTrailingZeros());
		}
	}
}
