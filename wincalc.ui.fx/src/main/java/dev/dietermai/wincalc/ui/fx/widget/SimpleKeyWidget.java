package dev.dietermai.wincalc.ui.fx.widget;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SimpleKeyWidget extends Widget<VBox> {

	private Button mc;
	private Button mr;
	private Button mp;
	private Button mm;
	private Button ms;
	private HBox memoryBox;

	private Button percent;
	private Button clearEntry;
	private Button clear;
	private Button delete;
	private Button oneOver;
	private Button square;
	private Button root;
	private Button divide;
	private Button seven;
	private Button eight;
	private Button nine;
	private Button mulitply;
	private Button four;
	private Button five;
	private Button six;
	private Button minus;
	private Button one;
	private Button two;
	private Button tree;
	private Button plus;
	private Button plusMinus;
	private Button zero;
	private Button point;
	private Button equals;

	private HBox row1;
	private HBox row2;
	private HBox row3;
	private HBox row4;
	private HBox row5;
	private HBox row6;
	
	private VBox rootBox;

	@Override
	protected VBox createNodes() {
		mc = new Button();
		mr = new Button();
		mp = new Button();
		mm = new Button();
		ms = new Button();
		memoryBox = new HBox();

		percent = new Button();
		clearEntry = new Button();
		clear = new Button();
		delete = new Button();
		row1 = new HBox();
		
		oneOver = new Button();
		square = new Button();
		root = new Button();
		divide = new Button();
		row2 = new HBox();
		
		seven = new Button();
		eight = new Button();
		nine = new Button();
		mulitply = new Button();
		row3 = new HBox();
		
		four = new Button();
		five = new Button();
		six = new Button();
		minus = new Button();
		row4 = new HBox();
		
		one = new Button();
		two = new Button();
		tree = new Button();
		plus = new Button();
		row5 = new HBox();
		
		plusMinus = new Button();
		zero = new Button();
		point = new Button();
		equals = new Button();
		row6 = new HBox();


		return rootBox = new VBox();
	}

	@Override
	protected void initializeNodes() {
		initializeMemoryRow();
		initializeCalculatorButtons();
		intializeRoot();
	}
	
	
	private void initializeMemoryRow() {
		initializeMemoryButton(mc, "MC");
		initializeMemoryButton(mr, "MR");
		initializeMemoryButton(mp, "M+");
		initializeMemoryButton(mm, "M-");
		initializeMemoryButton(ms, "MS");
		
		memoryBox.setMaxHeight(70);
		memoryBox.setSpacing(Style.SPACING_1);
		memoryBox.getChildren().add(mc);
		memoryBox.getChildren().add(mr);
		memoryBox.getChildren().add(mp);
		memoryBox.getChildren().add(mm);
		memoryBox.getChildren().add(ms);
		
		HBox.setHgrow(mc, Priority.ALWAYS);
		HBox.setHgrow(mr, Priority.ALWAYS);
		HBox.setHgrow(mp, Priority.ALWAYS);
		HBox.setHgrow(mm, Priority.ALWAYS);
		HBox.setHgrow(ms, Priority.ALWAYS);
	}
	
	
	private void initializeMemoryButton(Button button, String text) {
		button.setText(text);
		button.setMaxHeight(Double.MAX_VALUE);
		button.setMaxWidth(78);
		
	}
	
	private void initializeCalculatorButtons() {
		initializeGridButtion(percent, "%");
		initializeGridButtion(clearEntry, "CE");
		initializeGridButtion(clear, "C");
		initializeGridButtion(delete, "<-");
		initializeCalculatorRow(row1, percent, clearEntry, clear, delete);
		
		initializeGridButtion(oneOver, "1/x");
		initializeGridButtion(square, "x²");
		initializeGridButtion(root, "root");
		initializeGridButtion(divide, "/");
		initializeCalculatorRow(row2, oneOver, square, root, divide);
		
		initializeGridButtion(seven, "7");
		initializeGridButtion(eight, "8");
		initializeGridButtion(nine, "9");
		initializeGridButtion(mulitply, "*");
		initializeCalculatorRow(row3, seven, eight, nine, mulitply);
		
		initializeGridButtion(four, "4");
		initializeGridButtion(five, "5");
		initializeGridButtion(six, "6");
		initializeGridButtion(minus, "-");
		initializeCalculatorRow(row4, four, five, six, minus);
		
		initializeGridButtion(one, "1");
		initializeGridButtion(two, "2");
		initializeGridButtion(tree, "3");
		initializeGridButtion(plus, "+");
		initializeCalculatorRow(row5, one, two, tree, plus);
		
		initializeGridButtion(plusMinus, "+/-");
		initializeGridButtion(zero, "0");
		initializeGridButtion(point, ".");
		initializeGridButtion(equals, "=");
		initializeCalculatorRow(row6, plusMinus, zero, point, equals);
	}
	
	private void initializeGridButtion(Button button, String text) {
		button.setText(text);
		button.setMaxHeight(Double.MAX_VALUE);
		button.setMaxWidth(Double.MAX_VALUE);
		button.setPrefWidth(1);
		button.setMinWidth(1);
	}
	
	private void initializeCalculatorRow(HBox row, Button...buttons) {
		row.setSpacing(Style.SPACING_1);
		row.setFillHeight(true);
		
		for(Button b : buttons) {
			row.getChildren().add(b);
			HBox.setHgrow(b, Priority.ALWAYS);
		}
	}
	
	private void intializeRoot() {
		rootBox.setFillWidth(true);
		rootBox.getChildren().add(memoryBox);
		rootBox.getChildren().add(row1);
		rootBox.getChildren().add(row2);
		rootBox.getChildren().add(row3);
		rootBox.getChildren().add(row4);
		rootBox.getChildren().add(row5);
		rootBox.getChildren().add(row6);
		VBox.setVgrow(memoryBox, Priority.ALWAYS);
		VBox.setVgrow(row1, Priority.ALWAYS);
		VBox.setVgrow(row2, Priority.ALWAYS);
		VBox.setVgrow(row3, Priority.ALWAYS);
		VBox.setVgrow(row4, Priority.ALWAYS);
		VBox.setVgrow(row5, Priority.ALWAYS);
		VBox.setVgrow(row6, Priority.ALWAYS);
	}

	
	@Override
	protected void register() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void connect() {
		// TODO Auto-generated method stub

	}

}
