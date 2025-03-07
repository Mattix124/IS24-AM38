package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface implemented from ServerRMI
 */
public interface InterfaceRMI extends Remote, Serializable {
     /**
     * This method let the player draw from the decks or from the cards exposed
     *
      * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException -
     */
    void draw(String nickname, String cardType, int card) throws RemoteException;

    /**
     * This method let the player play a card where he wants (and where is possible) on its field
     *
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card
     * @throws RemoteException -
     */
    void playACard(int card, int x, int y, boolean face, String nickname) throws RemoteException;

    /**
     * Method that adds to the server message queue the ping sent from the client
     *
     * @param nickname of the player who has sent the ping
     * @throws RemoteException
     */
    void pingIn(String nickname) throws RemoteException;

    /**
     * Method that adds the chat message to the queue of chat messages
     *
     * @param message sent
     * @param nickname of the player who ha sent the chat message
     * @throws RemoteException
     */
    void broadcastMessage(StringBuilder message, String nickname)throws RemoteException;

    /**
     * Method that adds the chat message to the queue of chat messages
     *
     * @param message sent
     * @param receiver the player to send the message
     * @param nickname the player who has sent the message
     * @throws RemoteException
     */
    void privateMessage(StringBuilder message, String receiver, String nickname)throws RemoteException;

    /**
     * Method that communicates to the server the face chosen for the starter card
     *
     * @param nickname of the player
     * @param face chosen
     * @throws RemoteException
     */
    void chooseFaceStarterCard(String nickname, String face)throws RemoteException;

    /**
     * Method that communicates to the server the color chosen for the pawn
     *
     * @param nickname of the player
     * @param color chosen
     * @throws RemoteException
     */
    void chooseColor(String nickname, String color) throws RemoteException;

    /**
     * Method that communicates to the server the objective card chosen
     *
     * @param nickname
     * @param choose indicates which one of the 2 objective the player has chosen
     * @throws RemoteException
     */
    void chooseObjectiveCard(String nickname, String choose) throws RemoteException;

    /**
     * Method that starts the SortPlayerThread for the client
     *
     * @param ci the interface of the clientRMI
     * @throws RemoteException
     */
    void setSort(ClientInterface ci) throws RemoteException;
}
