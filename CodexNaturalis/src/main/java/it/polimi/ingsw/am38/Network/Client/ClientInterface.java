package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This interface is implemented from the ClientRMI
 */
public interface ClientInterface extends Remote, Serializable {
    /**
     * This method let the server know when a player wants to join a game already created
     * @param nickname is the player's nickname
     * @param gameID is the ID of the game that the player wants to join
     * @throws RemoteException
     * @throws NumOfPlayersException
     * @throws GameNotFoundException
     */
    boolean join(String nickname, int gameID) throws RemoteException;

    /**
     * This method says to the server to create a new game
     * @param nickname is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return the ID of the game created
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    int createGame(String nickname, int numberOfPlayers, ClientInterface ci) throws RemoteException;

    /**
     * This method let the server know when a client wants to perform a login (i.e. create an instance of Player)
     *
     * @param player is the player nickname
     * @throws RemoteException
     * @throws NicknameTakenException
     * @throws NullNicknameException
     */
    String login(String player)throws RemoteException;

    /**
     * This method says to the server to draw from the decks or from the cards exposed
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException
     * @throws GameNotFoundException
     * @throws EmptyDeckException
     */
    void draw(String nickname, String cardType, int card, int gameID) throws RemoteException, EmptyDeckException, GameNotFoundException, InvalidInputException;

    /**
     * This method says to the server to play a card and where on the player's field
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card
     * @throws NoPossiblePlacement
     * @throws RemoteException
     */
    void playACard(int card, int x, int y, boolean face, String nickname, int gameID) throws NoPossiblePlacement, RemoteException, InvalidInputException, NotPlaceableException;

    /**
     * Method that send a message to the server in order for it to be sent to every player
     * @param message the string that represent the message
     * @throws RemoteException
     */
    void broadcastMessage(String message)throws RemoteException;

    /**
     * Method that send a message to the server in order for it to be sent to a certain palyer
     * @param message the string that represent the message
     * @param player is the nickname of the player to whom to send the message
     * @throws RemoteException
     */
    void privateMessage(String message, String player)throws RemoteException;

    /**
     * Method to set in the ClientDATA the id of the starter card drawn by each player
     * @param starterCards is the HashMap containing as key: the nickname of each player, as value: their starter cards
     * @throws RemoteException
     */
    void setStarterCards(HashMap<String, Integer> starterCards, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround)throws RemoteException;

    /**
     * Method that communicates to the server the chosen face for the starter card
     * @param nickname is the nickname of the player
     * @param face is the face chosen
     * @param gameID is the id of the game in which the player is playing
     * @throws RemoteException
     */
    void chooseFaceStarterCard(String nickname, String face, int gameID)throws RemoteException;

    /**
     * Method that communicates to the server the chosen color
     * @param nickname is the nickname of the player
     * @param color is the color chosen
     * @param gameID is the id of the game in which the player is playing
     * @throws RemoteException
     */
    void chooseColor(String nickname, String color, int gameID) throws RemoteException;

    /**
     * Method that communicates to the server the chosen objective
     * @param nickname is the nickname of the player
     * @param choose is the objective chosen
     * @param gameID is the id of the game in which the player is playing
     * @throws RemoteException
     */
    void chooseObjectiveCard(String nickname, String choose, int gameID) throws RemoteException;

    /**
     * Method that set in the ClientCommandInterpreter the phase of the game to let it elaborate only certain type of input
     * @param t is the phase to set (ENUM)
     * @throws RemoteException
     */
    void setPhase(Turnings t)throws RemoteException;

    /**
     * Method called from the server on the client to show the objectives from which to choose
     * @param obj1 is the first objective drawn
     * @param obj2 is the second objective drawn
     * @throws RemoteException
     */
    void setChoosingObjective(String obj1, String obj2)throws RemoteException;

    /**
     * Method to show the card selected
     * @param nickname is the player's nickname
     * @param x is the x coordinate of the card to show
     * @param y is the y coordinate of the card to show
     * @param gameID is the id of the game in which the player is playing
     * @throws RemoteException
     */
    void showCard(String nickname, int x, int y, int gameID)throws RemoteException;

    /**
     * Method called to show an enemy's field
     * @param nickname is the player's nickname
     * @param player is the player's nickname of which to show the field
     * @param gameID is the id of the game in which the player is playing
     * @throws RemoteException
     */
    void showField(String nickname, String player, int gameID)throws RemoteException;

    /**
     * Method to show a list of possible card placement
     * @throws RemoteException
     */
    void placement()throws RemoteException;

    /**
     * Method to print a certain message (called by the server)
     * @param message the string to print
     * @throws RemoteException
     */
    void printLine(String message)throws RemoteException;

    void setSort(ClientInterface ci) throws RemoteException;
}
