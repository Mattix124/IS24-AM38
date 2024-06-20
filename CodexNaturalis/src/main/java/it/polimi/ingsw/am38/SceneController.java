package it.polimi.ingsw.am38;

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
		switch (((Node) event.getSource()).getId()){
			case "okButton": {
				System.out.println(((Node) event.getSource()).getId());
				System.out.println();
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.setMinWidth(1500);
				stage.setMinHeight(1050);
				stage.setX(0);
				stage.setY(0);
				stage.show();
			}
			case "backButton": {
				System.out.println(((Node) event.getSource()).getId());
				System.out.println();
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.setMinWidth(669);  // it's "important" to keep this width since 669 % 3 == 0 and cards are shown with width equals to 221
				stage.setMinHeight(500);
				stage.show();
			}
		}
	}
}
