package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface implemented from ServerRMI
 */
public interface InterfaceRMI extends Remote, Serializable {
    /**
     * This method let a player join a game already created
     * @param nickname is the player's nickname
     * @param gameID is the ID of the game that the player wants to join
     * @throws RemoteException
     * @throws NumOfPlayersException
     * @throws GameNotFoundException
     */
    void join(String nickname, int gameID, ClientInterface ci) throws RemoteException, NumOfPlayersException, GameNotFoundException;

    /**
     * This method create a new game
     * @param numberOfPlayers is the number of players that the game will have
     * @return the ID of the game created
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    int createGame(String nickname, int numberOfPlayers, ClientInterface ci) throws RemoteException, NumOfPlayersException;

    /**
     * Method called by a client in order to login (i.e. create an instance of Player)
     *
     * @param player is the player nickname
     * @return an instance of Player created
     * @throws RemoteException
     * @throws NicknameTakenException
     * @throws NullNicknameException
     */
    String login(String player) throws RemoteException, NicknameTakenException, NullNicknameException;

    /**
     * This method let the player draw from the decks or from the cards exposed
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException
     * @throws GameNotFoundException
     * @throws EmptyDeckException
     */
    void draw(String nickname, String cardType, int card, int gameID) throws RemoteException, GameNotFoundException, EmptyDeckException, InvalidInputException;

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
    void playACard(int card, int x, int y, boolean face, String nickname, int gameID) throws NoPossiblePlacement, RemoteException, InvalidInputException, NotPlaceableException;
    void broadcastMessage(String message)throws RemoteException;
    void privateMessage(String message, String player)throws RemoteException;
    void getSarterCard(String nickname, int gameID) throws RemoteException, GameNotFoundException;
    void chooseFaceStarterCard(String nickname, String face, int gameID)throws RemoteException;
    void chooseColor(String nickname, String color, int gameID) throws RemoteException, ColorTakenException;
    ArrayList<String> getObjecgtiveCards(String nickname)throws RemoteException;
    void chooseObjectiveCard(String nickname, String choose, int gameID) throws Exception;
    void showCard(String nickname, int x, int y, int gameID)throws RemoteException;
    void showField(String nickname, String player, int gameID)throws RemoteException;
    void placement()throws RemoteException;
}
