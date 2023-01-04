package dev.dietermai.wincalc.ui.fx.widget;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.setDebugColors;

import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LeftBoxWidget extends Widget<VBox>{
	private final MenuBarWidget menuBar;
	private final SimpleKeyWidget simpleKey;
	
	private VBox rootBox;
	private Label numberField;

	
	public LeftBoxWidget() {
		super();
		menuBar = new MenuBarWidget();
		simpleKey = new SimpleKeyWidget();
		
		addChild(menuBar);
		addChild(simpleKey);
	}
	
	
	@Override
	protected VBox createNodes() {
		numberField = new Label("numberField"); // debugging
		return rootBox = new VBox(0);
	}

	@Override
	protected void initializeNodes() {
		setDebugColors(numberField);
		setDebugColors(rootBox);
		
		numberField.setMaxWidth(Double.MAX_VALUE);
		
		
		rootBox.setFillWidth(true);
		rootBox.getChildren().add(menuBar.getNode());
		rootBox.getChildren().add (numberField);
		rootBox.getChildren().add(simpleKey.getNode());
		
		VBox.setVgrow(simpleKey.getNode(), Priority.ALWAYS);
		
	}

	@Override
	protected void register() {
	}

	@Override
	protected void connect() {
	}

}
