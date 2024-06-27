package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.*;

import java.util.HashMap;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.View.SceneController.guiModel;

/**
 * Class that represents the GUI and implements the method of the Viewable interface
 */
public class GUI implements Viewable
{
	/**
	 * SceneController instance
	 */
	private final SceneController sceneController;
	/**
	 * Thread on which the GUI take life
	 */
	private Thread threadView;
	/**
	 * GuiData instance
	 */
	protected static GuiData guiData;

	/**
	 * Constructor method for the GUI, create a new SceneController and a new GuiData
	 */
	public GUI()
	{
		this.sceneController = new SceneController();
		guiData = new GuiData();
	}

	/**
	 * Method to print in the chat a message sent by the client itself
	 *
	 * @param s a String containing the message sent (already formatted correctly)
	 */
	@Override
	public void receiveOwnMessage(String s)
	{
		guiModel.changeProperty("ChatOut",s);
	}

	/**
	 * Method that prints the messages for each player's start of turn
	 *
	 * @param name a String containing the nickname of the Player whose turn is starting
	 */
	@Override
	public void playersTurn(String name)
	{
		guiModel.changeProperty("TurnName", name);
	}

	/**
	 * Only for CLI
	 *
	 * @param nickname
	 */
	@Override
	public void showPlayerField(String nickname) //non bisogno
	{

	}

	/**
	 * Updates the score of the Player who just ended their turn (4 times each round)
	 *
	 * @param nickname a String containing the name of the Player whose turn just ended
	 * @param score an int representing the Score of the given Player
	 */
	@Override
	public void updateScore(String nickname, int score)
	{
		ScorePlayers sp = new ScorePlayers(score, nickname);
		guiModel.changeProperty("Score", sp);
	}

	/**
	 * Only for CLI
	 *
	 * @param nick
	 * @param handColors
	 */
	@Override
	public void updateEnemiesHandColors(String nick, String[] handColors) //? non bisogno
	{

	}

	/**
	 * Only for CLI
	 */
	@Override
	public void updateScreen() //non bisogno
	{

	}

	/**
	 * Only for CLI
	 */
	@Override
	public void printHelp()//non bisogno
	{

	}

	/**
	 * Shows all the existing and available information while this Player has to choose his personal Objective between the 2 possible choices
	 *
	 * @param pc an HashMap of all Players' nicknames (key-String) and their Color (value)
	 * @param hcc an HashMap of all Players' nicknames (key-String) and the colors of the cards in their Hand (value-Symbol[])
	 * @param psc an HashMap of all Players' nicknames (key-String) and their choice of facing for their StarterCard (value-StarterCard)
	 * @param ownHand a LinkedList containing the PlayableCard in this Player's Hand
	 * @param sharedObj1 an ObjectiveCard representing the first shared Objective
	 * @param sharedObj2 an ObjectiveCard representing the second shared Objective
	 * @param objChoice1 an ObjectiveCard representing the first choice for this Player's personal Objective
	 * @param objChoice2 an ObjectiveCard representing the second choice for this Player's personal Objective
	 */
	@Override
	public void personalObjectiveChoice(Symbol topG, Symbol topR, String nickname, HashMap <String, Color> pc, HashMap <String, String[]> hcc, HashMap <String, StarterCard> psc, LinkedList <PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap <String, VisibleElements> pve)
	{
		guiModel.changeProperty("RemoveLabel", "");
		sceneController.changeScene("objC");
		ObjChoiceData objChoiceData = new ObjChoiceData(nickname, pc, hcc, psc, ownHand, sharedObj1, sharedObj2, objChoice1, objChoice2, pve, topR, topG);
		guiData.setObjd(objChoiceData);
		guiModel.changeProperty("Start", objChoiceData);
	}

	/**
	 * Shows all the existing and available information while this Player has to choose his StarterCard facing
	 *
	 * @param sc the StarterCard this Player drew
	 * @param gt a Symbol variable representing the color of the card on top of the gold deck
	 * @param rt a Symbol variable representing the color of the card on top of the resource deck
	 * @param g1 the first face-up GoldCard available to be drawn
	 * @param g2 the second face-up GoldCard available to be drawn
	 * @param r1 the first face-up ResourceCard available to be drawn
	 * @param r2 the second face-up ResourceCard available to be drawn
	 */
	@Override
	public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2)
	{
		sceneController.changeScene("setUp");
		StarterChoiceData starterChoiceData = new StarterChoiceData(sc, gt, rt, g1, g2, r1, r2);
		guiModel.changeProperty("Start", starterChoiceData);
	}

	/**
	 * Only for CLI
	 *
	 * @param nickname
	 * @param symTab
	 */
	@Override
	public void setSymbolsTab(String nickname, VisibleElements symTab) //non bisogno (abbiamo deciso)
	{

	}

	/**
	 * Method that tells all the clients that a player has successfully placed a card
	 *
	 * @param nick of the player who has placed
	 * @param card placed
	 * @param x coordinates of the card placed
	 * @param y coordinates of the card placed
	 */
	@Override
	public void setCardInField(String nick, PlayableCard card, int x, int y)
	{
		GuiPlacedConfirm gpc = new GuiPlacedConfirm(nick, card, x, y);
		guiModel.changeProperty("Placed", gpc);
	}

	/**
	 * Shows the deck and the hand updated after a draw
	 *
	 * @param colorG kingdom of the card on top of the gold deck
	 * @param colorR kingdom of the card on top of the resource deck
	 * @param gc1 the first face-up GoldCard
	 * @param gc2 the second face-up GoldCard
	 * @param rc1 the first face-up ResourceCard
	 * @param rc2 the second face-up ResourceCard
	 * @param card drawn
	 */
	@Override
	public void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList <PlayableCard> card)
	{
		DeckandHand daD = new DeckandHand(colorG, colorR, gc1, gc2, rc1, rc2, card);
		guiModel.changeProperty("DrawConf", daD);
	}

	/**
	 * Shows the screen in which the game actually start after the setup phase
	 *
	 * @param thisNick nickname of the player
	 */
	@Override
	public void showFirstScreen(String thisNick)
	{
		guiModel.changeProperty("RemoveLabel", "");
		sceneController.changeScene("game");
		guiModel.changeProperty("Start", "");
	}

	/**
	 * Setter method for this Player's chosen personal Objective
	 *
	 * @param objective the ObjectiveCard chosen by this Player
	 */
	@Override
	public void setPersonalObjective(ObjectiveCard objective)
	{
		guiModel.changeProperty("Wait", objective);
	}

	/**
	 * Updates the chat by adding the new message received
	 *
	 * @param messageReceived a String containing the message received
	 */
	@Override
	public void receiveMessage(String messageReceived)
	{
		guiModel.changeProperty("ChatIn", messageReceived);
	}

	/**
	 * Only for CLI
	 *
	 * @param s
	 */
	@Override
	public void sendString(String s) //non bisogno
	{

	}

	/**
	 * Method used to communicate errors to the clients
	 *
	 * @param s
	 */
	@Override
	public void priorityString(String s)
	{
		String[] tokens = s.split("/");
		guiModel.changeProperty(tokens[0], tokens[1]);

	}

	/**
	 * Method to display messages
	 *
	 * @param s
	 */
	@Override
	public void displayString(String s)
	{
		displayStringLogin(s);
	}

	/**
	 * Method used to set the Card Display and update the game screen (used only by CLI)
	 *
	 * @param card the PlayableCard to display
	 * @param x its x coordinate
	 * @param y its y coordinate
	 */
	@Override
	public void updateCardDisplay(PlayableCard card, int x, int y) //non bisogno
	{

	}

	/**
	 *
	 *
	 * @param s
	 */
	@Override
	public void displayStringLogin(String s)
	{
		if (!s.contains("Insert"))
			guiModel.changeProperty("Login", s);
	}

	/**
	 * Only for CLI
	 *
	 * @param cardsInHand
	 */
	@Override
	public void setHandAfterPlacement(LinkedList <PlayableCard> cardsInHand) //non bisogno
	{

	}

	/**
	 * Method used to update the game screen with all the information changed after an opponent draws a card
	 *
	 * @param nickname a String containing the name of the Player who drew a card
	 * @param gfu1 GoldCard face up 1
	 * @param gfu2 GoldCard face up 2
	 * @param rfu1 ResourceCard face up 1
	 * @param rfu2 ResourceCard face up 2
	 * @param gtc Symbol of the card on top of the GoldDeck
	 * @param rtc Symbol of the card on top of the ResourceDeck
	 * @param hcc array of Strings containing the Hand cards colors
	 */
	@Override
	public void updateOtherPlayerDraw(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, String[] hcc)
	{
		DeckandHand deckandHand = new DeckandHand(nickname, gfu1, gfu2, rfu1, rfu2, gtc, rtc, hcc);
		guiModel.changeProperty("OtherDraw", deckandHand);
	}

	@Override
	public void reconnectionInitialSetter(String ownNick, ObjectiveCard shObj1, ObjectiveCard shObj2, ObjectiveCard pObj, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2, LinkedList<PlayableCard> cardsInHand, HashMap<String, PlayerDisconnectionResendInfo> pdr) {

	}

	@Override
	public void reconnectionCardsToPlay(String nick, PlayableCard cardToPlay, int x, int y) {

	}

	@Override
	public void computeScreen() {

	}

	/**
	 * Method that start the view chosen (GUI or CLI)
	 *
	 * @param cci to let the 2 types of view to send the command to the server
	 * @return ClientWriter if the view chosen is TUI, null otherwise
	 */
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
