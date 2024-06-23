package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
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

	public void changeScene(ActionEvent event)
	{
		try
		{
			switch (((Node) event.getSource()).getId())
			{
				case "okButton" ->
						root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GameScene.fxml")));
				case "backButton" ->
						root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SetUpScene.fxml")));
			}
		}
		catch (IOException ex)
		{
			System.err.println(ex.getMessage());
		}
		System.out.println(((Node) event.getSource()).getId());
		System.out.println();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Method that allows to change scene without the press of a button
	 * @param loader the root of the fxml file
	 * @param n a node from the current scene
	 */
	public void changeScene(FXMLLoader loader, Node n) throws IOException //passargli come parametro Node un elemento della scena che è visibile al momento della chiamata
	{
		Parent root = loader.load();
		guiModel.setListener(loader.getController());
		stage = (Stage) n.getScene().getWindow();
		stage.close();
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Setter for the CommandInterpreter that allows to send command to the server
	 * @param clientCommandInterpreter
	 */
	static void setCommandInterpreter(ClientCommandInterpreter clientCommandInterpreter)
	{
		cci = clientCommandInterpreter;
	}

}
