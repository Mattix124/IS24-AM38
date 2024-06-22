package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class GUI extends Application implements Viewable
{
	private SceneController sceneController;
	private SetUpSceneController setUpSceneController;
	private PropertyChangeListener listener;
	private String outcome;


	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("login-view.fxml"));
		Parent root  = fxmlLoader.load();
		LoginController controller = (LoginController) fxmlLoader.getController();
		Scene  scene = new Scene(root);
		primaryStage.setMinHeight(500.0);
		primaryStage.setMinWidth(750.0);

		primaryStage.setTitle("Login page");
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/icon.jpg"))));
		primaryStage.setScene(scene);
		primaryStage.show();
		this.setListener(controller);
		}

	@Override
	public void receiveOwnMessage(String s)
	{

	}

	@Override
	public void playersTurn(String name)
	{

	}

	@Override
	public void showPlayerField(String nickname)
	{

	}

	@Override
	public void updateScore(String nickname, int score)
	{

	}

	@Override
	public void updateEnemiesHandColors(String nick, Symbol[] handColors)
	{

	}

	@Override
	public void updateScreen()
	{

	}

	@Override
	public void printHelp()
	{

	}

	@Override
	public void personalObjectiveChoice(HashMap <String, Color> pc, HashMap <String, Symbol[]> hcc, HashMap <String, StarterCard> psc, LinkedList <PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2)
	{
		setUpSceneController.personalObjectiveChoice(objChoice1, objChoice2);
	}

	@Override
	public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2)
	{
		int id;

		id = sc.getCardID();
		//HelloApplication.setStarterCard(id);
		// setUpSceneController.setId(id);
	}

	@Override
	public void setSymbolsTab(HashMap <Symbol, Integer> sym)
	{

	}

	@Override
	public void setCardInField(String nick, PlayableCard card, int x, int y)
	{

	}

	@Override
	public void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList <PlayableCard> card)
	{

	}

	@Override
	public void showFirstScreen(String thisNick)
	{

	}

	@Override
	public void setPersonalObjective(ObjectiveCard objective)
	{

	}

	@Override
	public void receiveMessage(String messageReceived)
	{

	}

	@Override
	public void sendString(String s)
	{

	}

	@Override
	public void priorityString(String s, int scale)
	{

	}

	@Override
	public void displayString(String s)
	{

	}

	@Override
	public void setCardDisplay(PlayableCard card, int x, int y)
	{

	}

	@Override
	public void displayStringLogin(String s)
	{
		String head = s.split(" ")[0];
		PropertyChangeEvent event = new PropertyChangeEvent(this,"login",outcome,head);
		outcome = head;
		listener.propertyChange(event);
	}

	@Override
	public ClientWriter startView(ClientCommandInterpreter cci)
	{
		this.sceneController = new SceneController();
		SceneController.setCommandInterpreter(cci);
		launch();
		return null;
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	private void setListener(PropertyChangeListener listener)
	{
		this.listener = listener;
	}
}
