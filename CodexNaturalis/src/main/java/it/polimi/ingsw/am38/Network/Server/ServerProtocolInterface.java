package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.io.IOException;

/**
 * Interface implemented from ImplementerRMI and ImplementerTCP to unify the action of the server with the clients
 */
public interface ServerProtocolInterface
{
	/**
	 * Method that send to the client the username if it is valid the one that it's been
	 * chosen
	 *
	 * @param s username validated
	 */
	void setClientUsername(String s);

	/**
	 * Method that send to the client messages for the login phase and get back
	 * the response
	 *
	 * @param s message to send to the client
	 * @return a string with the response
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	String loginRequest(String s) throws ClassNotFoundException, IOException;

	/**
	 * Method that add the player logged in to the game
	 *
	 * @param gt GameThread of the game
	 * @param p player to add
	 * @param reconnect parameter set to true if the player is reconnecting to his previous game
	 */
	void finalizeInitialization(GameThread gt, Player p, boolean reconnect);

	/**
	 * Method used to send to the client messages to display
	 *
	 * @param s string with the message
	 */
	void display(String s);

	/**
	 * Method that set the ServerPingThread to the implementations(TCP and RMI)
	 *
	 * @param spt the ServerPingThread
	 */
	void addPingThread(ServerPingThread spt);

	/**
	 * Method that tells the client to choose the face of the starter card
	 *
	 * @param gc the GameController of the game
	 */
	void starterCardSelection(GameController gc);

	/**
	 * Method that tells the client to choose the color of the pawn
	 *
	 * @param s string to display
	 */
	void colorSelection(String s);

	/**
	 * Method that send to the client info to allow it to choose the objective card
	 *
	 * @param gc the GameController of the game
	 * @param p the player to send the info
	 */
	void preObjChoiceViewUpdate(GameController gc, Player p);

	/**
	 * Method to send the client a string that says that the game it's waiting to go on
	 */
	void waitTextPlayers();

	/**
	 * Method that says to the client that the game is started (after the setup phase)
	 *
	 * @param s string to display
	 */
	void enterGame(String s);

	//Game Related

	/**
	 * Method to communicates whose turn it is
	 *
	 * @param s nickname of the player whose turn it is
	 */
	void turnShifter(String s);

	/**
	 * Method that communicates to the client that the spot chosen to play a card
	 * is not available
	 *
	 * @param s string to display
	 */
	void noPossiblePlacement(String s);

	/**
	 * Method that communicates to the client that the deck is empty
	 *
	 * @param s string to display
	 */
	void emptyDeck(String s);

	/**
	 * Method that communicates to the client some minor errors
	 *
	 * @param s string to display
	 */
	void lightError(String s);

	/**
	 * Method that says to the client to play
	 *
	 * @param s string to display
	 */
	void playCard(String s);

	/**
	 * Method that says to the client that the placement of a card has gone well
	 *
	 * @param nickname of the player that has played the card
	 * @param id id of the card placed
	 * @param x coordinates on player's field
	 * @param y coordinates on player's field
	 * @param face chosen for the card placed
	 * @param points given by the card placed
	 * @param symbolTab VisibleElements updated with the elements given by the card placed
	 */
	void confirmedPlacement(String nickname, int id, int x, int y, boolean face, int points, VisibleElements symbolTab);

	/**
	 * Method that says to the client to draw
	 *
	 * @param s string to display
	 */
	void drawCard(String s);

	/**
	 * Method that says to the client that the draw of a card has gone well
	 *
	 * @param gameController GameController of the game
	 */
	void confirmedDraw(GameController gameController);

	/**
	 * Method that says to the clients who the winner(s) is(/are)
	 *
	 * @param s string that contains the winner(s)
	 */
	void winnersMessage(String s);

	/**
	 * Method that draw a card for a client disconnected
	 *
	 * @param id of the card drawn
	 */
	void setDisconnectionHangingCard(int id);

	//------------------------

	/**
	 * Method that send to the client the chat message to print
	 *
	 * @param s chat message to display
	 */
	void chatMessage(String s);

	/**
	 * Method to exchange ping with the client
	 *
	 * @param b if true start a new ping communication, if false send a ping to the client
	 */
	void ping(boolean b);

	/**
	 * Method to send the information back to a client after a disconnection
	 *
	 * @param game the game to which the player has reconnected
	 */
	void resendInfo(Game game);

	/**
	 * Getter for the player (in the implementation of this interface we have an instance of the
	 * player ready to use)
	 *
	 * @return the player
	 */
	Player getPlayer();

	/**
	 * Method that send updated info about the game to the client after another player has drawn
	 *
	 * @param gameController GameController of the game
	 * @param s the kingdoms of the hand of the player who drawn
	 */
	void confirmedOtherDraw(GameController gameController, String[] s);
}
