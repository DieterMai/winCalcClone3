package dev.dietermai.wincalc.ui.fx.widget;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.setDebugColors;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LeftBoxWidget extends ParentWidget<VBox>{
	private final MenuBarWidget menuBar;
	
	private VBox rootBox;
	private Label numberField;
	private Label buttons;

	
	public LeftBoxWidget() {
		super();
		menuBar = new MenuBarWidget();
		
		addChild(menuBar);
	}
	
	
	@Override
	protected void createNodes() {
		numberField = new Label("numberField"); // debugging
		buttons = new Label("buttons"); // debugging
		
		
		rootBox = new VBox(0);
		setNode(rootBox);
	}

	@Override
	protected void initializeNodes() {
		setDebugColors(numberField);
		setDebugColors(buttons);
		setDebugColors(rootBox);
		
		numberField.setMaxWidth(Double.MAX_VALUE);
		buttons.setMaxWidth(Double.MAX_VALUE);
		
		
		rootBox.setFillWidth(true);
		
		rootBox.getChildren().add(menuBar.getNode());
		rootBox.getChildren().add(numberField);
		rootBox.getChildren().add(buttons);
		
		
	}

	@Override
	protected void register() {
	}

	@Override
	protected void connect() {
	}

}
