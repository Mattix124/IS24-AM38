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
    String join(String nickname, int gameID) throws RemoteException;

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
     * @throws RemoteException
     * @throws NicknameTakenException
     * @throws NullNicknameException
     */
    String getString()throws RemoteException;

    /**
     * Method that set in the ClientCommandInterpreter the phase of the game to let it elaborate only certain type of input
     * @param t is the phase to set (ENUM)
     * @throws RemoteException
     */
    void setPhase(Turnings t)throws RemoteException;

    /**
     * Method to set in the ClientDATA the id of the starter card drawn by each player
     * @param starterCards is the HashMap containing as key: the nickname of each player, as value: their starter cards
     * @throws RemoteException
     */
    void setStarterCards(HashMap<String, Integer> starterCards, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround)throws RemoteException;



    /**
     * Method called from the server on the client to show the objectives from which to choose
     * @param obj1 is the first objective drawn
     * @param obj2 is the second objective drawn
     * @throws RemoteException
     */
    void setChoosingObjective(String obj1, String obj2)throws RemoteException;



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

    void setNickname(String s) throws RemoteException;
}
