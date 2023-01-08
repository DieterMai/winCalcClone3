package dev.dietermai.wincalc.ui.fx.widget;

import dev.dietermai.wincalc.ui.fx.util.FxUtil;
import javafx.scene.paint.Color;

public enum Style {
	;
	
	
	public static final double SPACING_1 = 2;
	
	public static final double GRAY_VALUE_0 = 0.12D;
	public static final double GRAY_VALUE_1 = 0.23D;
	public static final double GRAY_VALUE_2 = 0.19D;
	public static final double GRAY_VALUE_3 = 0.51D;
	
	public static final Color BUTTON_COLOR_0 = FxUtil.color(GRAY_VALUE_0, GRAY_VALUE_0, GRAY_VALUE_0, 1D);
	public static final Color BUTTON_COLOR_1 = FxUtil.color(GRAY_VALUE_1, GRAY_VALUE_1, GRAY_VALUE_1, 1D);
	public static final Color BUTTON_COLOR_2 = FxUtil.color(GRAY_VALUE_2, GRAY_VALUE_2, GRAY_VALUE_2, 1D);
	public static final Color BUTTON_COLOR_3 = FxUtil.color(GRAY_VALUE_3, GRAY_VALUE_3, GRAY_VALUE_3, 1D);
	public static final Color BUTTON_TEXT_COLOR = FxUtil.color(1D, 1D, 1D, 1D);
	
}
