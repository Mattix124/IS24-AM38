package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import it.polimi.ingsw.am38.Enum.Color;

/**
 * This interface is implemented from the ClientRMI
 */
public interface ClientInterface extends Remote, Serializable
{

	/**
	 * This method let the server know when a client wants to perform a login (i.e. create an instance of Player)
	 *
	 * @throws RemoteException
	 */
	String getString() throws RemoteException;

	/**
	 * Method that set in the ClientCommandInterpreter the phase of the game to let it elaborate only certain type of input
	 *
	 * @param t is the phase to set (ENUM)
	 * @throws RemoteException
	 */
	void setPhase(Turnings t) throws RemoteException;

	/**
	 * Method to set in the ClientDATA the id of the starter card drawn by each player
	 *
	 * @param starterCards is the HashMap containing as key: the nickname of each player, as value: their starter cards
	 * @throws RemoteException
	 */
	void setStarterCards(HashMap <String, Integer> starterCards, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround) throws RemoteException;

	/**
	 * Method called from the server on the client to show the objectives from which to choose
	 *
	 * @throws RemoteException
	 */
	void setChoosingObjective(int[] obj,int[] hand, Symbol topG, Symbol topR, HashMap<String,Boolean> starterFacings,HashMap <String,Color> playersColors,HashMap <String,String[]> handsColors,String[] phrases, HashMap<String, VisibleElements> pve) throws RemoteException;

	/**
	 * Method to print a certain message (called by the server)
	 *
	 * @param message the string to print
	 * @throws RemoteException
	 */
	void sendLine(String message) throws RemoteException;

	/**
	 * Method to display to the client certain error messages
	 *
	 * @param s the message to display
	 * @throws RemoteException
	 */
	void display(String s) throws RemoteException;

	/**
	 * Method to display the actual game view after the setup phase
	 *
	 * @param message to display
	 * @throws RemoteException
	 */
	void enterGame(String message) throws RemoteException;

	/**
	 * Method to communicate which player has to play
	 *
	 * @param s nickname of the player whose turn it is
	 * @throws RemoteException
	 */
	void turnShifter(String s) throws RemoteException;

	/**
	 * Method to communicate that a card cannot be placed in the spot selected
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	void noPossiblePlacement(String s) throws RemoteException;

	/**
	 * Method to update the client's info after a draw gone well
	 *
	 * @param cardDrawnId id of the card drawn
	 * @param goldFaceUp1id id of one of the gold card on the ground
	 * @param goldFaceUp2Id id of one of the gold card on the ground
	 * @param resFaceUp1Id id of one of the resource card on the ground
	 * @param resFaceUp2Id id of one of the resource card on the ground
	 * @param goldTopCardSymbol id of the gold card on top of the deck
	 * @param resTopCardSymbol id of the resource card on top of the deck
	 * @throws RemoteException
	 */
	void confirmedDraw(int cardDrawnId, int goldFaceUp1id, int goldFaceUp2Id, int resFaceUp1Id, int resFaceUp2Id, Symbol goldTopCardSymbol, Symbol resTopCardSymbol) throws RemoteException;

	/**
	 * Method to communicate that a deck is empty
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	void emptyDeck(String s) throws RemoteException;

	/**
	 * Method to display a minor error message
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	void lightError(String s) throws RemoteException;

	/**
	 * Method to update the client's info after any player has placed
	 *
	 * @param nickname of the player that has placed a card
	 * @param id of the card placed
	 * @param x coordinates on the player's field
	 * @param y coordinates on the player's field
	 * @param face chosen for the card placed
	 * @param points given by the card placed
	 * @param visibleElements elements on the field updated after the card placed
	 * @throws RemoteException
	 */
	void confirmedPlacement(String nickname, int id, int x, int y, boolean face, int points, VisibleElements visibleElements) throws RemoteException;

	/**
	 * Method for the client to wait the player to insert a nickname
	 *
	 * @return the nickname inserted
	 * @throws RemoteException
	 */
	String getStringLogin() throws RemoteException;

	/**
	 * Method that helps display() to show certain messages
	 *
	 * @param s
	 * @throws RemoteException
	 */
	void displayStringLogin(String s) throws RemoteException;

	/**
	 * Method to show the winners
	 *
	 * @param s string with the winners
	 * @throws RemoteException
	 */
	void winnersMessage(String s) throws RemoteException;

	/**
	 * Method to start the SortPlayerThread
	 *
	 * @param ci this interface
	 * @throws RemoteException
	 */
	void setSort(ClientInterface ci) throws RemoteException;

	/**
	 * Method to send the nickname to the client (called by the server)
	 *
	 * @param s the nickname
	 * @throws RemoteException
	 */
	void sendNickname(String s) throws RemoteException;

	/**
	 * Method to start the ping with the server
	 *
	 * @throws RemoteException
	 */
	void startPing() throws RemoteException;

	/**
	 * Method to check the ping has arrived from the server
	 *
	 * @throws RemoteException
	 */
	void clientPing() throws RemoteException;

	/**
	 * Method to print a chat message
	 *
	 * @param message
	 * @throws RemoteException
	 */
	void printChatMessage(String message) throws RemoteException;

	/**
	 * Method to notify other player's draw
	 *
	 * @param nickname of the player who has drawn
	 * @param resourceFaceUp1Id id of one of the resource cards on the ground
	 * @param resourceFaceUp2Id id of one of the resource cards on the ground
	 * @param goldFaceUp1Id id of one of the gold cards on the ground
	 * @param goldFaceUp2Id id of one of the gold cards on the ground
	 * @param resourceTopCardSymbol id of the resource card on top of the deck
	 * @param goldTopCardSymbol id of the gold cards on top of the deck
	 * @param cardDrawnId id of the card drawn
	 * @param playerHandCardColors hashMap of the players and their cards in the hands
	 * @throws RemoteException
	 */
	void otherDrawUpdate(String nickname, int resourceFaceUp1Id, int  resourceFaceUp2Id, int goldFaceUp1Id, int goldFaceUp2Id, Symbol resourceTopCardSymbol, Symbol goldTopCardSymbol, int cardDrawnId, String[] playerHandCardColors ) throws RemoteException;

	/**
	 * Method to get the information after a disconnection
	 * @param playersInfo HashMap<String, PlayerDisconnectionResendInfo>, key: nickname of all players, value: all available information about each Player
	 * @param ownHand ArrayList<PlayableCard> containing all 3 cards in the Player's hand
	 * @param nickname String containing the name of the Player
	 * @param upGc1 GoldCard face up n1
	 * @param upGc2 GoldCard face up n2
	 * @param upRc1 ResourceCard face up n1
	 * @param upRc2 ResourceCard face up n2
	 * @param goldTop Symbol representing the top of the gold deck
	 * @param resourceTop Symbol representing the top of the resource deck
	 * @throws RemoteException ignored
	 */
	void reconnectionDataUpdate(HashMap <String, PlayerDisconnectionResendInfo> playersInfo, ArrayList<PlayableCard> ownHand, String nickname, int upGc1, int upGc2, int upRc1, int upRc2, Symbol goldTop, Symbol resourceTop, int sharedObj1, int sharedObj2, int personalObj) throws RemoteException;
}
