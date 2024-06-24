package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
	protected static GuiListenerHolder guiModel;

	SceneController()
	{
		guiModel = new GuiListenerHolder();

	}

	public void init(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
		Parent     root   = loader.load();
		Scene      scene  = new Scene(root);
		guiModel.setListener(loader.getController());
		this.stage = primaryStage;
		primaryStage.setMinHeight(500.0);
		primaryStage.setMinWidth(750.0);
		primaryStage.setTitle("Login page");
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/icon.jpg"))));
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Method that allows to change scene without the press of a button
	 *
	 * @param newScene the string that identify a scene
	 */

	public void changeScene(String newScene)
	{

			FXMLLoader loader = null;
			switch (newScene)
			{
				case "setUp" ->
						loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("SetUpScene.fxml")));
				case "objC" ->
						loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("objChoice.fxml")));
				case "game" ->
						loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("GameScene.fxml")));
			}
			try
			{
				root = loader.load();
				guiModel.setListener(loader.getController());
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		Platform.runLater(() -> {
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		});
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
