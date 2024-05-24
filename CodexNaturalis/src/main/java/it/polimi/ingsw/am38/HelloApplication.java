package it.polimi.ingsw.am38;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
		Scene scene = new Scene(root);
		primaryStage.setMinHeight(500.0);
		primaryStage.setMinWidth(750.0);
		primaryStage.setTitle("Login page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
