package dev.dietermai.wincalc.ui.fx.widget;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.randomColorBackground;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RightBoxWidget extends ParentWidget<VBox>{
	private VBox root;
	private Label labelHistory;
	
	public RightBoxWidget() {
		super();
	}
	
	@Override
	protected void createNodes() {
		labelHistory = new Label(this.getClass().getSimpleName());
		
		root = new VBox(0);
		setNode(root);
	}

	@Override
	protected void initializeNodes() {
		root.setBackground(randomColorBackground());
		root.setPrefWidth(300);
		
		root.getChildren().add(labelHistory);
	}

	@Override
	protected void register() {
	}

	@Override
	protected void connect() {
	}

	public void setVisible(boolean value) {
		root.setVisible(value);
	}

}
