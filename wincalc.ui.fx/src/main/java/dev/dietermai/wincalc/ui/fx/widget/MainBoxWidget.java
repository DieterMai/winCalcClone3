package dev.dietermai.wincalc.ui.fx.widget;

import dev.dietermai.wincalc.ui.fx.action.RightBoxVisibilityAction;
import dev.dietermai.wincalc.ui.fx.util.FxUtil;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainBoxWidget extends ParentWidget<HBox> {

	private LeftBoxWidget leftBox;
	private RightBoxWidget rightBox;

	private HBox rootBox;

	public MainBoxWidget() {
		super();
		leftBox = new LeftBoxWidget();
		rightBox = new RightBoxWidget();

		addChild(leftBox);
		addChild(rightBox);
	}

	@Override
	protected void createNodes() {
		rootBox = new HBox(0);
		setNode(rootBox);

	}

	@Override
	protected void initializeNodes() {
		rootBox.setBackground(FxUtil.randomColorBackground());

		rootBox.getChildren().add(leftBox.getNode());
		rootBox.getChildren().add(rightBox.getNode());

		HBox.setHgrow(leftBox.getNode(), Priority.ALWAYS);
		HBox.setHgrow(rightBox.getNode(), Priority.NEVER);
	}

	@Override
	protected void register() {
	}

	@Override
	protected void connect() {
		RightBoxVisibilityAction rightBoxVisibilityAction = new RightBoxVisibilityAction(this, rightBox);
		rightBoxVisibilityAction.initialize();
	}

}
