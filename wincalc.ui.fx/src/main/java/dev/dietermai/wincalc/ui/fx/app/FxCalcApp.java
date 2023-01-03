package dev.dietermai.wincalc.ui.fx.app;

import static dev.dietermai.wincalc.ui.fx.util.FxUtil.randomColorBackground;

import dev.dietermai.wincalc.ui.fx.widget.MainBoxWidget;
import dev.dietermai.wincalc.ui.fx.widget.NodeWidget;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
//		VBox leftBox = createLeftBox();
//		VBox rightBox = createRightBox();
//
//		HBox mainBox = new HBox(0);
//		mainBox.setBackground(randomColorBackground());
//		mainBox.getChildren().addAll(leftBox, rightBox);
//
//		mainBox.widthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				final int TRASHHOLD = 557;
//				int old = oldValue.intValue();
//				int neo = newValue.intValue();
//				
//				System.out.println("FxCalcApp.initializeStage(...).new ChangeListener() {...}.changed() old: "+old+", new: "+neo);
//				if (old >= TRASHHOLD && neo < TRASHHOLD && mainBox.getChildren().contains(rightBox)) {
//					System.out.println("visibility -> false");
//					rightBox.setVisible(false);
//					mainBox.getChildren().remove(rightBox);
//				} else if (old < TRASHHOLD && neo >= TRASHHOLD && !mainBox.getChildren().contains(rightBox)) {
//					System.out.println("visibility -> true");
//					rightBox.setVisible(true);
//					mainBox.getChildren().add(rightBox);
//				}
//			}
//		});
		
		MainBoxWidget mainBox = new MainBoxWidget();
		NodeWidget.initialize(mainBox);
		
		StackPane mainStackPane = new StackPane(mainBox.getNode());
		var scene = new Scene(mainStackPane, 640, 480);
		stage.setScene(scene);
		stage.show();
	}
}