package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.*;

import java.util.HashMap;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.View.SceneController.guiModel;

public class GUI implements Viewable
{
	private final SceneController sceneController;
	private Thread threadView;
	protected static GuiData guiData;

	public GUI()
	{
		this.sceneController = new SceneController();
		guiData = new GuiData();
	}

	@Override
	public void receiveOwnMessage(String s) //non bisogno
	{

	}

	@Override
	public void playersTurn(String name)
	{
		guiModel.changeProperty("TurnName", name);
	}

	@Override
	public void showPlayerField(String nickname) //non bisogno
	{

	}

	@Override
	public void updateScore(String nickname, int score)
	{
		ScorePlayers sp = new ScorePlayers(score, nickname);
		guiModel.changeProperty("Score", sp);
	}

	@Override
	public void updateEnemiesHandColors(String nick, String[] handColors) //? non bisogno
	{

	}

	@Override
	public void updateScreen() //non bisogno
	{

	}

	@Override
	public void printHelp()//non bisogno
	{

	}

	@Override
	public void personalObjectiveChoice(Symbol topG, Symbol topR, String nickname, HashMap <String, Color> pc, HashMap <String, String[]> hcc, HashMap <String, StarterCard> psc, LinkedList <PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap <String, VisibleElements> pve)
	{
		guiModel.changeProperty("RemoveLabel", "");
		sceneController.changeScene("objC");
		ObjChoiceData objChoiceData = new ObjChoiceData(nickname, pc, hcc, psc, ownHand, sharedObj1, sharedObj2, objChoice1, objChoice2, pve, topR, topG);
		guiData.setObjd(objChoiceData);
		guiModel.changeProperty("Start", objChoiceData);
	}

	@Override
	public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2)
	{
		sceneController.changeScene("setUp");
		StarterChoiceData starterChoiceData = new StarterChoiceData(sc, gt, rt, g1, g2, r1, r2);
		guiModel.changeProperty("Start", starterChoiceData);
	}

	@Override
	public void setSymbolsTab(String nickname, VisibleElements symTab) //non bisogno (abbiamo deciso)
	{

	}

	@Override
	public void setCardInField(String nick, PlayableCard card, int x, int y)
	{
		GuiPlacedConfirm gpc = new GuiPlacedConfirm(nick, card, x, y);
		guiModel.changeProperty("Placed", gpc);
	}

	@Override
	public void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList <PlayableCard> card)
	{
		DeckandHand daD = new DeckandHand(colorG, colorR, gc1, gc2, rc1, rc2, card);
		guiModel.changeProperty("DrawConf", daD);
	}

	@Override
	public void showFirstScreen(String thisNick)
	{
		guiModel.changeProperty("RemoveLabel", "");
		sceneController.changeScene("game");
		guiModel.changeProperty("Start", "");
	}

	@Override
	public void setPersonalObjective(ObjectiveCard objective)
	{
		guiModel.changeProperty("Wait", objective);
	}

	@Override
	public void receiveMessage(String messageReceived)
	{
		guiModel.changeProperty("ChatIn", messageReceived);
	}

	@Override
	public void sendString(String s) //non bisogno
	{

	}

	@Override
	public void priorityString(String s)
	{
		String[] tokens = s.split("/");
		guiModel.changeProperty(tokens[0], tokens[1]);
	}

	@Override
	public void displayString(String s)
	{
		displayStringLogin(s);
	}

	@Override
	public void updateCardDisplay(PlayableCard card, int x, int y) //non bisogno
	{

	}

	@Override
	public void displayStringLogin(String s)
	{
		if (!s.contains("Insert"))
			guiModel.changeProperty("Login", s);
	}

	@Override
	public void setHandAfterPlacement(LinkedList <PlayableCard> cardsInHand) //non bisogno
	{

	}

	@Override
	public void updateOtherPlayerDraw(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, String[] hcc)
	{
		DeckandHand deckandHand = new DeckandHand(nickname, gfu1, gfu2, rfu1, rfu2, gtc, rtc, hcc);
		guiModel.changeProperty("OtherDraw", deckandHand);
	}

	@Override
	public ClientWriter startView(ClientCommandInterpreter cci)
	{
		SceneController.setCommandInterpreter(cci);
		GuiSupportApp.sceneController = sceneController;
		threadView = new Thread(GuiSupportApp::start);
		threadView.setPriority(7);
		threadView.start();
		return null;
	}

}
