package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 * This is the class that communicate with GameThread in order to perform the commands
 */
public class ServerRMI  implements InterfaceRMI, Serializable {

    private static final long serialVersionUID = 3708068332600924022L;

    private int port;
    private Registry reg;
    private final LobbyManager LM = LobbyManager.getLobbyManager();
    private static LinkedList <GameThread> gameThreadList = null;

    //DISCLAIMER: probabilmente le implementazioni andranno cambiate per poter far si
    //            che i comandi vengano eseguiti dal GameThread, per tanto per ora non faccio la javadoc

    /**
     * Constructor of the server RMI
     * @param port is the port on which the connection take life
     * @throws RemoteException
     */
    public ServerRMI(int port) throws RemoteException {
        this.port = port;
        //gameThreadList = new LinkedList<>();   probabilmente va cambiato
    }

    /**
     *
     * @throws RemoteException
     */
    public void start() throws RemoteException{ //crea la stub per la connessione col client
        InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(this, this.port);
        reg = LocateRegistry.createRegistry(this.port);
        try {
            reg.bind("server_RMI", stub);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        System.out.println("Server RMI ready");
    }

    /**
     *
     * @param nickname is the player's nickname
     * @param gameID is the ID of the game that the player wants to join
     * @throws RemoteException
     * @throws NumOfPlayersException
     * @throws GameNotFoundException
     */
    public void join(String nickname, int gameID) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        Player p = LM.getPlayer(nickname);
        LM.joinGame(gameID, p); //chiama sul lobby manager la join game
    }

    /**
     *
     * @param p is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    @Override
    public int createGame(Player p, int numberOfPlayers) throws RemoteException, NumOfPlayersException {
        int gameID;
        synchronized (LM){
            gameID = LM.createNewGame(numberOfPlayers, p);
            System.out.println(gameID+p.getNickname()+numberOfPlayers);
        }
        GameThread gt;
        gt = new GameThread(p, gameID, numberOfPlayers);
        //gameThreadList.add(gt);
        gt.start();
        return gameID;
    }

    /**
     *
     * @param player is the player nickname
     * @return
     * @throws RemoteException
     * @throws NicknameTakenException
     * @throws NullNicknameException
     */
    public Player login(String player) throws RemoteException, NicknameTakenException, NullNicknameException {
        Player p;
        synchronized (LM){
            p = LM.createPlayer(player);
            p.setIsPlaying(false);
        }
        return p;
    }

    /**
     *
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException
     * @throws InvalidInputException
     * @throws EmptyDeckException
     */
    public void draw(String nickname, String cardType, int card) throws RemoteException, InvalidInputException, EmptyDeckException {
        Player p = LM.getPlayer(nickname);
        LM.getGameController(p.getGame().getGameID()).playerDraw(cardType, card);
    }

    /**
     *
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card, used to get the game id
     * @throws NotPlaceableException
     * @throws RemoteException
     * @throws InvalidInputException
     */
    public void playACard(int card, int x, int y, String face, String nickname) throws NotPlaceableException, RemoteException, InvalidInputException {
        Player p = LM.getPlayer(nickname);
        LM.getGameController(p.getGame().getGameID()).playerPlay(card, x, y, face);
    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }
}
