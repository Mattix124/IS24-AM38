package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController
{

	private Stage stage;
	private Scene scene;
	private Parent root;
	protected static ClientCommandInterpreter cci;
	protected static GuiModel guiModel;

	SceneController()
	{
		guiModel = new GuiModel();
	}

	public void init(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
		Parent     root   = loader.load();
		Scene      scene  = new Scene(root);
		guiModel.setListener(loader.getController());
		primaryStage.setMinHeight(500.0);
		primaryStage.setMinWidth(750.0);
		primaryStage.setTitle("Login page");
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/icon.jpg"))));
		primaryStage.setScene(scene);
		primaryStage.show();
		stage = primaryStage;
	}

	/**
	 * Method that allows to change scene without the press of a button
	 *
	 * @param newScene the string that identify a scene
	 *
	 */

	public void changeScene(String newScene)
	{
		FXMLLoader loader = null;

		switch (newScene)
		{
			case "setUp" -> loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("SetUpScene.fxml")));
			case "objC" -> loader = new FXMLLoader(getClass().getResource("objChoice.fxml"));
			case "game" -> loader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
		}
			System.out.println(loader);
		try
		{
			root = loader.load();
			guiModel.setListener(loader.getController());
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}



	/**
	 * Setter for the CommandInterpreter that allows to send command to the server
	 *
	 * @param clientCommandInterpreter
	 */
	static void setCommandInterpreter(ClientCommandInterpreter clientCommandInterpreter)
	{
		cci = clientCommandInterpreter;
	}

}
