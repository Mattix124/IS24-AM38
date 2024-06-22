package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NoPossiblePlacement;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

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

	void ping() throws RemoteException;

	void signalsPingArrived() throws RemoteException;

	void killer(int code) throws RemoteException;

	void waitPingConfirm() throws RemoteException;

	void setDisconnection() throws RemoteException;

	void setNickname(String s) throws RemoteException;

	String getNickname() throws RemoteException;

	void sendStringLogin(String s)throws RemoteException;
	Viewable getViewableInterface() throws RemoteException;
	ClientCommandInterpreter getCommandIntepreter() throws RemoteException;
}
