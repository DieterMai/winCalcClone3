package dev.dietermai.wincalc.ui.fx.app;

import dev.dietermai.wincalc.ui.fx.widget.MainBoxWidget;
import dev.dietermai.wincalc.ui.fx.widget.Widget;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class FxCalcApp extends Application {

	@Override
	public void start(Stage stage) {
		initializeStage(stage);
	}

	public static void main(String[] args) {
		launch();
	}

	private void initializeStage(Stage stage) {
		
		MainBoxWidget mainBox = new MainBoxWidget();
		Widget.initialize(mainBox);
		
		StackPane mainStackPane = new StackPane(mainBox.getNode());
		var scene = new Scene(mainStackPane, 640, 480);
		stage.setScene(scene);
		stage.show();
	}
}