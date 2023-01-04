package dev.dietermai.wincalc.ui.fx.widget;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.setDebugColors;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SimpleKeyWidget extends Widget<GridPane> {

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

	private GridPane rootGrid;

	@Override
	protected GridPane createNodes() {
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
		oneOver = new Button();
		square = new Button();
		root = new Button();
		divide = new Button();
		seven = new Button();
		eight = new Button();
		nine = new Button();
		mulitply = new Button();
		four = new Button();
		five = new Button();
		six = new Button();
		minus = new Button();
		one = new Button();
		two = new Button();
		tree = new Button();
		plus = new Button();
		plusMinus = new Button();
		zero = new Button();
		point = new Button();
		equals = new Button();


		return rootGrid = new GridPane();
	}

	@Override
	protected void initializeNodes() {
		
		initializeMemoryRow();
		
		
		
		initializeGridButtion(percent, "%", 1, 0);
		initializeGridButtion(clearEntry, "CE", 1, 1);
		initializeGridButtion(clear, "C", 1, 2);
		initializeGridButtion(delete, "<-", 1, 3);
		initializeGridButtion(oneOver, "1/x", 2, 0);
		initializeGridButtion(square, "x²", 2, 1);
		initializeGridButtion(root, "root", 2, 2);
		initializeGridButtion(divide, "/", 2, 3);
		initializeGridButtion(seven, "7", 3, 0);
		initializeGridButtion(eight, "8", 3, 1);
		initializeGridButtion(nine, "9", 3, 2);
		initializeGridButtion(mulitply, "*", 3, 3);
		initializeGridButtion(four, "4", 4, 0);
		initializeGridButtion(five, "5", 4, 1);
		initializeGridButtion(six, "6", 4, 2);
		initializeGridButtion(minus, "-", 4, 3);
		initializeGridButtion(one, "1", 5, 0);
		initializeGridButtion(two, "2", 5, 1);
		initializeGridButtion(tree, "3", 5, 2);
		initializeGridButtion(plus, "+", 5, 3);
		initializeGridButtion(plusMinus, "+/-", 6, 0);
		initializeGridButtion(zero, "0", 6, 1);
		initializeGridButtion(point, ".", 6, 2);
		initializeGridButtion(equals, "=", 6, 3);
	}
	
	private void initializeMemoryRow() {
		initializeMemoryButton(mc, "MC");
		initializeMemoryButton(mr, "MR");
		initializeMemoryButton(mp, "M+");
		initializeMemoryButton(mm, "M-");
		initializeMemoryButton(ms, "MS");
		
		setDebugColors(memoryBox);
		memoryBox.setMaxHeight(70);
		
		memoryBox.setSpacing(2);
		
		rootGrid.add(memoryBox, 0, 0, 4, 1);
		GridPane.setHgrow(memoryBox, Priority.ALWAYS);
		GridPane.setVgrow(memoryBox, Priority.ALWAYS);
	}
	
	private void initializeMemoryButton(Button button, String text) {
		button.setText(text);
		button.setMaxHeight(Double.MAX_VALUE);
		button.setMaxWidth(78);
		memoryBox.getChildren().add(button);
		HBox.setHgrow(button, Priority.ALWAYS);
	}
	
	
	private void initializeGridButtion(Button button, String text, int col, int row) {
		button.setText(text);
		button.setMaxHeight(Double.MAX_VALUE);
		button.setMaxWidth(Double.MAX_VALUE);
		
		rootGrid.setHgap(2D);
		rootGrid.setVgap(2D);
		rootGrid.add(button, row, col);
		
		GridPane.setHgrow(button, Priority.ALWAYS);
		GridPane.setVgrow(button, Priority.ALWAYS);
		
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
