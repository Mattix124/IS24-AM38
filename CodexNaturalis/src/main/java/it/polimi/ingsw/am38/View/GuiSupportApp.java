package it.polimi.ingsw.am38.View;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Application class that allow the GUI to run
 */
public class GuiSupportApp extends Application
{
	/**
	 * SceneController instance
	 */
	static SceneController sceneController;

	/**
	 * Start method that calls the SceneController to display the first scene
	 *
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		sceneController.init(primaryStage);
	}

	/**
	 * Method that launch the application
	 */
	public static void start()
	{
		launch();
	}
}
