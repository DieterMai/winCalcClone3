package dev.dietermai.wincalc.ui.fx.widget;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.randomColorBackground;
import static dev.dietermai.wincalc.ui.fx.util.FxUtil.setDebugColors;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MenuBarWidget extends ParentWidget<HBox> {
	private HBox rootBox;
	private HBox leftAligneBox;
	private Label menuButton;
	private Label calculaterType;
	private Label onTop;
	private Label history;
	
	
	@Override
	protected void createNodes() {
		menuButton = new Label("rootBox"); // debugging
		calculaterType = new Label("calculaterType"); // debugging
		onTop = new Label("onTop"); // debugging
		history = new Label("history"); // debugging
		
		leftAligneBox = new HBox();
		
		rootBox = new HBox(0);
		setNode(rootBox);
	}

	@Override
	protected void initializeNodes() {
		setDebugColors(menuButton);
		setDebugColors(calculaterType);
		setDebugColors(onTop);
		setDebugColors(history);
		setDebugColors(leftAligneBox);
		
		
		leftAligneBox.setBackground(randomColorBackground());
		leftAligneBox.getChildren().add(menuButton);
		leftAligneBox.getChildren().add(calculaterType);
		leftAligneBox.getChildren().add(onTop);
		
		
		
		rootBox.setBackground(randomColorBackground());
		rootBox.getChildren().add(leftAligneBox);
		rootBox.getChildren().add(history);
		
		HBox.setHgrow(leftAligneBox, Priority.ALWAYS);
		HBox.setHgrow(history, Priority.NEVER);
	}
	
	@Override
	protected void register() {
	}

	@Override
	protected void connect() {
	}

}
