package dev.dietermai.wincalc.ui.fx.action;

import java.util.List;

import dev.dietermai.wincalc.ui.fx.widget.MainBoxWidget;
import dev.dietermai.wincalc.ui.fx.widget.RightBoxWidget;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public class RightBoxVisibilityAction {
	private final int THRASHOLD = 557;

	private final MainBoxWidget mainBox;
	private final RightBoxWidget rightBox;

	public RightBoxVisibilityAction(MainBoxWidget mainBox, RightBoxWidget rightBox) {
		this.mainBox = mainBox;
		this.rightBox = rightBox;
	}
	
	public void initialize() {
		mainBox.getNode().widthProperty().addListener(this::mainBoxWidthChanged);
	}
	
	private void mainBoxWidthChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		act(oldValue.intValue() , newValue.intValue());
	}

	public void act(int old, int neo) {
		if (isFallingFlank(old, neo) && isRightBoxInRoot()) {
			rightBox.setVisible(false);
			getMainBoxChildren().remove(rightBox.getNode());
		} else if (isRisingFlank(old, neo) && !isRightBoxInRoot()) {
			rightBox.setVisible(true);
			getMainBoxChildren().add(rightBox.getNode());
		}
	}

	private boolean isFallingFlank(int old, int neo) {
		return old >= THRASHOLD && neo < THRASHOLD;
	}

	private boolean isRisingFlank(int old, int neo) {
		return old < THRASHOLD && neo >= THRASHOLD;
	}

	private boolean isRightBoxInRoot() {
		return getMainBoxChildren().contains(rightBox.getNode());
	}

	private List<Node> getMainBoxChildren() {
		return mainBox.getNode().getChildren();
	}
}
