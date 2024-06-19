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
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException
     * @throws GameNotFoundException
     * @throws EmptyDeckException
     */
    void draw(String nickname, String cardType, int card) throws RemoteException;

    /**
     * This method let the player play a card where he wants (and where is possible) on its field
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card
     * @throws NoPossiblePlacement
     * @throws RemoteException
     */
    void playACard(int card, int x, int y, boolean face, String nickname) throws RemoteException;
    void pingIn(String nickname) throws RemoteException;
    void broadcastMessage(StringBuilder message, String nickname)throws RemoteException;
    void privateMessage(StringBuilder message, String receiver, String nickname)throws RemoteException;
    void chooseFaceStarterCard(String nickname, String face)throws RemoteException;
    void chooseColor(String nickname, String color) throws RemoteException;
    void chooseObjectiveCard(String nickname, String choose) throws RemoteException;
    void placement()throws RemoteException;
    void setSort(ClientInterface ci) throws RemoteException;
}
