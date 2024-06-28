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

import static it.polimi.ingsw.am38.View.GUI.guiData;

/**
 * Class that change the scenes during the game
 */
public class SceneController
{

	private Stage stage;
	private Scene scene;
	private Parent root;
	/**
	 * Instance of ClientCommandInterpreted used in all Scene Controllers
	 */
	protected static ClientCommandInterpreter cci;
	/**
	 * Instance of GuiListenerHolder
	 */
	protected static GuiListenerHolder guiListenerHolder;

	/**
	 * Constructor of SceneController
	 */
	public SceneController()
	{
		guiListenerHolder = new GuiListenerHolder();

	}

	/**
	 * Initialize the starting of graphics view
	 *
	 * @param primaryStage
	 * @throws IOException
	 */
	public void init(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
		Parent     root   = loader.load();
		Scene      scene  = new Scene(root);
		guiListenerHolder.setListener(loader.getController());
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
		String title = "";
		FXMLLoader loader = null;
		switch (newScene)
		{
			case "setUp" ->
			{
				loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("SetUpScene.fxml")));
				title = "Set Up";
			}

			case "objC" ->
			{
				loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("objChoice.fxml")));
				title = "Objective Choice";
			}

			case "game" ->
			{
				loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("GameScene.fxml")));
				title = "Game: "+guiData.getNickname();
			}

		}
		try
		{
			root = loader.load();
			guiListenerHolder.setListener(loader.getController());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		String finalTitle = title;
		Platform.runLater(() -> {
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setMaximized(true);
			stage.setTitle(finalTitle);
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
