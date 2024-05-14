package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MCoords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

/**
 * This is the class that communicate with GameThread in order to perform the commands
 */
public class ServerRMI  implements InterfaceRMI, Serializable {

    private static final long serialVersionUID = 3708068332600924022L;

    private int port;
    private Registry reg;
    private final LobbyManager LM = LobbyManager.getLobbyManager();


    /**
     * Constructor of the server RMI
     * @param port is the port on which the connection take life
     * @throws RemoteException
     */
    public ServerRMI(int port) throws RemoteException
    {
        this.port = port;
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
    public void join(String nickname, int gameID, ClientInterface ci) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        Player p ;
        synchronized (LM){
            p = LM.getPlayer(nickname);
            LM.joinGame(gameID, p); //chiama sul lobby manager la join game
            GameThread gameThread = null;
            for (GameThread gt : LM.getGameThreadList())
            {
                if (gt.getGame().getGameID() == gameID) gameThread = gt;
            }
            gameThread.addEntry(null, null, p, false, ci);
            p.setIsPlaying(true);
        }
        System.out.println(p.getNickname());
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
    public int createGame(String nickname, int numberOfPlayers, ClientInterface ci) throws RemoteException, NumOfPlayersException {
        int gameID;
        Player p = LM.getPlayer(nickname);
        synchronized (LM) {
            gameID = LM.createNewGame(numberOfPlayers, p);
            GameThread gt;
            gt = new GameThread(p, gameID, numberOfPlayers);
            LM.getGameThreadList().add(gt);
            gt.start();
            gt.addEntry(null, null, p, false, ci);
        }
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
        Message m = new Message(GAME, DRAWCARD, nickname, new MDrawCard(cardType, card));
        Player p = LM.getPlayer(nickname);

    }

    /**
     *
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card, used to get the game id
     * @throws NoPossiblePlacement
     * @throws RemoteException
     * @throws InvalidInputException
     */
    public void playACard(int card, int x, int y, String face, String nickname) throws RemoteException
	{
        Message m = new Message(GAME, PLAYCARD, nickname, new MPlayCard(card, new Coords(x, y), face));
    }

    public void broadcastMessage(String message) throws RemoteException {
        //Message m = new Message()
    }

    public void privateMessage(String message, String player) throws RemoteException {

    }

    public int getSarterCard(String nickname) throws RemoteException {
        Player p = LM.getPlayer(nickname);
        StarterCard sc = p.getStarterCard();
        return sc.getCardID();
    }

    public void chooseFaceStarterCard(String nickname, String face) throws RemoteException {
        Player p = LM.getPlayer(nickname);
        if(face.equals("up"))
            p.chooseStarterCardFace(true);
        else if(face.equals("down"))
            p.chooseStarterCardFace(false);
    }

    public void chooseColor(String nickname, String color) throws RemoteException, ColorTakenException {
        Player p = LM.getPlayer(nickname);
        switch (color){
            case "red" : p.chooseColor(Color.RED);
            case "blue" : p.chooseColor(Color.BLUE);
            case "green" : p.chooseColor(Color.GREEN);
            case "yellow" : p.chooseColor(Color.YELLOW);
        }
    }

    public ArrayList<Integer> getObjecgtiveCards(String nickname) throws RemoteException {
        Player p = LM.getPlayer(nickname);
        p.drawPairObjectives(p.getGame().getObjectiveDeck());
        ArrayList<Integer> cardsID = null;
        cardsID.add(p.getPair().get(0).getCardID());
        cardsID.add(p.getPair().get(1).getCardID());
        return cardsID;
    }

    public void chooseObjectiveCard(String nickname, int choose) throws RemoteException, InvalidInputException {
        Player p = LM.getPlayer(nickname);
        p.chooseObjectiveCard(choose);
    }

    public void showCard(String nickname, int x, int y) throws RemoteException {
        Message m = new Message(VIEWUPDATE, SHOWCARD, nickname, new MCoords(x, y));
    }

    public void showField() throws RemoteException {

    }

    public void placement() throws RemoteException {

    }
}
