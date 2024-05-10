package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the class for those clients who decide to connect via RMI
 */
public class ClientRMI extends UnicastRemoteObject implements ClientInterface {
    private String nickname;
    private String ip;
    private int port;
    private Registry reg;
    private InterfaceRMI intRMI;

    /**
     * Constructor of the ClientRMI
     * @param ip is the ip needed for the connection
     * @param port is the server port
     * @throws RemoteException
     */
    public ClientRMI(String ip, int port) throws RemoteException {
        this.ip = ip;
        this.port = port;
    }

    /**
     * This is the first method called, set the connection with the server
     */
    public void start(){
        try{
            reg = LocateRegistry.getRegistry(ip, port);
            this.intRMI = (InterfaceRMI) reg.lookup("server_RMI");
        }catch(RemoteException | NotBoundException e){
            System.out.println("Server unreachable");
        }
    }

    /**
     * This method communicates to the server the nickname of the player who wants to join a game
     * and the ID of the game
     * @param nickname is the player's nickname
     * @param gameID is the ID of the game that the player wants to join
     * @throws RemoteException
     * @throws NumOfPlayersException
     * @throws GameNotFoundException
     */
    public void join(String nickname, int gameID) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        intRMI.join(nickname, gameID);
    }

    /**
     * This method communicates to the sever the player who wants to create a game and the number of players
     * @param player is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    public int createGame(Player player, int numberOfPlayers) throws RemoteException, NumOfPlayersException {
        int game = this.intRMI.createGame(player, numberOfPlayers);
        return game;
    }

    /**
     * This method communicates to the server to create a player with the nickname given
     * @param player is the player nickname
     * @return the player created
     * @throws RemoteException
     */
    public Player login(String player) throws RemoteException{
        Player p = null;
        try{
            p = this.intRMI.login(player);
        }catch ( NicknameTakenException | NullNicknameException e){
            System.out.println("\nInvalid nickname :(");
        }
        return p;
    }

    /**
     * This method communicates to the server the parameters it needs to perform a draw for the player
     * @param player is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @param gameID is the ID of the current game, used to reach the decks from the Lobby Manager
     * @throws RemoteException
     * @throws EmptyDeckException
     * @throws GameNotFoundException
     */
    public void draw(Player player, String cardType, int card, int gameID) throws RemoteException, InvalidInputException, EmptyDeckException, GameNotFoundException {
        intRMI.draw(player, cardType, card, gameID);
    }

    /**
     * This method communicates to the server the parameters it needs to play a card for a
     * players in its field
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @throws NotPlaceableException
     * @throws RemoteException
     */
    public void playACard(int card, int x, int y, String face, int gameID) throws NotPlaceableException, RemoteException {
        intRMI.playACard(card, x, y, face, gameID);
    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }
}
