package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NoPossiblePlacement;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface implemented by both TCPClient and ClientRMI
 */
public interface CommonClientInterface extends Remote
{
	/**
	 * This method says to the server to draw from the decks or from the cards exposed
	 *
	 * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
	 * @param card     is an integer that allows the controller to know which card draw
	 * @throws RemoteException
	 * @throws GameNotFoundException
	 * @throws EmptyDeckException
	 */
	void draw(String cardType, int card) throws RemoteException;

	/**
	 * This method says to the server to play a card and where on the player's field
	 *
	 * @param card is the card to play
	 * @param x    the x coordinates where to play the card
	 * @param y    the x coordinates where to play the card
	 * @param face is how the card has to be played, face up or face down
	 * @throws NoPossiblePlacement
	 * @throws RemoteException
	 */
	void playACard(int card, int x, int y, boolean face) throws RemoteException;

	/**
	 * Method that communicates to the server the chosen face for the starter card
	 *
	 * @param face is the face chosen
	 * @throws RemoteException
	 */
	void chooseFaceStarterCard(String face) throws RemoteException;

	/**
	 * Method that communicates to the server the chosen color
	 *
	 * @param color is the color chosen
	 * @throws RemoteException
	 */
	void chooseColor(String color) throws RemoteException;

	/**
	 * Method that communicates to the server the chosen objective
	 *
	 * @param choose is the objective chosen
	 * @throws RemoteException
	 */
	void chooseObjectiveCard(String choose) throws RemoteException;

	/**
	 * Method that send a message to the server in order for it to be sent to every player
	 *
	 * @param message the string that represent the message
	 * @throws RemoteException
	 */
	void broadcastMessage(StringBuilder message) throws RemoteException;

	/**
	 * Method that send a message to the server in order for it to be sent to a certain palyer
	 *
	 * @param message  the string that represent the message
	 * @param receiver is the nickname of the player to whom to send the message
	 * @throws RemoteException
	 */
	void privateMessage(String receiver, StringBuilder message) throws RemoteException;

	/**
	 * Method to send a ping to the server
	 *
	 * @throws RemoteException
	 */
	void ping() throws RemoteException;

	/**
	 * Method that confirm the ping has arrived
	 *
	 * @throws RemoteException
	 */
	void signalsPingArrived() throws RemoteException;

	/**
	 * Method to kill the client due to a disconnection
	 *
	 * @throws RemoteException
	 */
	void killer() throws RemoteException;

	/**
	 * Method that waits for a ping response
	 *
	 * @throws RemoteException
	 */
	void waitPingConfirm() throws RemoteException;

	/**
	 * Method to set the client to disconnected
	 *
	 * @throws RemoteException
	 */
	void setDisconnection() throws RemoteException;

	/**
	 * Method to set the nickname in the ClientData
	 *
	 * @param s the nickname
	 * @throws RemoteException
	 */
	void setNickname(String s) throws RemoteException;

	/**
	 * Getter for the nickname
	 *
	 * @return nickname
	 * @throws RemoteException
	 */
	String getNickname() throws RemoteException;

	/**
	 * Method to exchange messages in the login phase
	 *
	 * @param s is the message
	 * @throws RemoteException
	 */
	void sendStringLogin(String s)throws RemoteException;
}
