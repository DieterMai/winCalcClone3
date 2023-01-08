package dev.dietermai.wincalc.ui.fx.util;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class FxUtil {
	public static Color color(double red, double green, double blue, double alpha) {
		return new Color(red, green, blue, alpha);
	}
	
	public static BackgroundFill fill(Color color) {
		return new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
	}
	
	public static BackgroundFill fill(double red, double green, double blue, double alpha) {
		return fill(color(red, green, blue, alpha));
	}
	
	
	public static Background background(BackgroundFill fill) {
		return new Background(fill);
	}
	
	public static Background background(double red, double green, double blue, double alpha) {
		return background(fill(red, green, blue, alpha));
	}
	
	public static Background background(Color color) {
		return background(fill(color));
	}
	
	public static Color randomColor() {
		Random rand = new Random(System.nanoTime());
		return color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1D);
	}
	
	public static BackgroundFill randomColorFill() {
		return fill(randomColor());
	}
	
	public static Background randomColorBackground() {
		return background(randomColor());
	}
	
	public static Color oposite(Color color) {
		Double blue = color.getBlue() + 0.5;
		Double green = color.getGreen() + 0.5;
		Double red = color.getRed() + 0.5;
		blue = (blue > 1d) ? blue - 1 : blue;
		green = (green > 1d) ? green - 1 : green;
		red = (red > 1d) ? red - 1 : red;
		return new Color(red, green, blue, 1d);
	}
	
	public static void setDebugColors(Region r) {
		Color back = randomColor();
		Color front = oposite(back);
		r.setBackground(background(back));
		if(r instanceof Labeled l) {
			l.setTextFill(front);
		}
	}
}
