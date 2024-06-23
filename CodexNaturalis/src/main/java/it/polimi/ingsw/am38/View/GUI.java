package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;

public class GUI extends Application implements Viewable
{
	private SceneController sceneController;
	private SetUpSceneController setUpSceneController;
	private LoginController loginController;
	private String outcome;
	private PropertyChangeListener listener;
	private Thread threadView;

	public GUI()
	{
		this.sceneController = new SceneController();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		sceneController.init(primaryStage);
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
		//setUpSceneController.personalObjectiveChoice(objChoice1, objChoice2);
	}

	@Override
	public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2)
	{

	}

	@Override
	public void setSymbolsTab(String nickname, VisibleElements symTab)
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
		displayStringLogin(s);
	}

	@Override
	public void setCardDisplay(PlayableCard card, int x, int y)
	{

	}

	@Override
	public void displayStringLogin(String s)
	{
		if(!s.contains("Insert"))
			SceneController.guiModel.change(s);
	}

	@Override
	public void setHandAfterPlacement(LinkedList<PlayableCard> cardsInHand) {

	}

	@Override
	public ClientWriter startView(ClientCommandInterpreter cci)
	{
		this.sceneController = new SceneController();
		SceneController.setCommandInterpreter(cci);
		threadView = new Thread(Application::launch);
		threadView.setPriority(7);
		threadView.start();
		return null;
	}

}
