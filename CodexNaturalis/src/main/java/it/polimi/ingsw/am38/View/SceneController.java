package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController
{

	private Stage stage;
	private Scene scene;
	private Parent root;
	protected static ClientCommandInterpreter cci;

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

	static void setCommandInterpreter(ClientCommandInterpreter clientCommandInterpreter)
	{
		cci = clientCommandInterpreter;
	}

}
