package it.polimi.ingsw.am38.View;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuiSupportApp extends Application
{
	static SceneController sceneController;
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		sceneController.init(primaryStage);
	}
	public static void start()
	{
		launch();
	}
}
