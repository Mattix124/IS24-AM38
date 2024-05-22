package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MCoords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

/**
 * This is the class that communicate with GameThread in order to perform the commands
 */
public class ServerRMI implements InterfaceRMI, Serializable {

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
            for (GameThread gt : LM.getGameThreadList())
            {
                if (gt.getGame().getGameID() == gameID)
                    gt.addEntry(p,ci);
            }
        }
    }

    /**
     *
     * @param nickname is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return the game id
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
            Thread gamethread = new Thread(gt);
            gamethread.start();
            gt.addEntry(p,ci);
        }
        return gameID;
    }

    /**
     * @param player is the player nickname
     * @return
     * @throws RemoteException
     * @throws NicknameTakenException
     * @throws NullNicknameException
     */
    public String login(String player) throws RemoteException, NicknameTakenException, NullNicknameException {
        Player p;
        synchronized (LM) {
            p = LM.createPlayer(player);
        }
        return p.getNickname();
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
    public void draw(String nickname, String cardType, int card, int gameID) throws RemoteException, InvalidInputException, EmptyDeckException {
        Message m = new Message(GAME, DRAWCARD, nickname, new MDrawCard(cardType, card));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(gameID).getServerInterpreter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(m);
        m = sms.getGameMessage(nickname);
        LM.getGameController(gameID).playerDraw(cardType, card);
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
    public void playACard(int card, int x, int y, boolean face, String nickname, int gameID) throws RemoteException, InvalidInputException, NoPossiblePlacement, NotPlaceableException {
        Message m = new Message(GAME, PLAYCARD, nickname, new MPlayCard(card, new Coords(x, y), face));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(gameID).getServerInterpreter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(m);
        m = sms.getGameMessage(nickname);
        LM.getGameController(gameID).playerPlay(card, x, y, face);
    }

    public void broadcastMessage(String message) throws RemoteException {
        //Message m = new Message()
    }

    public void privateMessage(String message, String player) throws RemoteException {

    }

    public void getSarterCard(String nickname, int gameID) throws RemoteException, GameNotFoundException {
        HashMap<String, Integer> starterCards = LM.getGame(gameID).getNicksAndStartersIDs();
        LM.getGameThread(gameID).getPlayerData(nickname).getInterface().setStarterCards(starterCards);
    }

    public void chooseFaceStarterCard(String nickname, String face, int gameID) throws RemoteException {
        Message message = new Message(GAME, STARTINGFACECHOICE, nickname, new MSimpleString(face));
        try {
            LM.getGameThread(gameID).getServerInterpreter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void chooseColor(String nickname, String color, int gameID) throws RemoteException {
        Message message = new Message(GAME, COLORCHOICE, nickname, new MSimpleString(color));
        try {
            LM.getGameThread(gameID).getServerInterpreter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void chooseObjectiveCard(String nickname, String choose, int gameID) throws RemoteException {
        Message message = new Message(GAME, OBJECTIVECHOICE, nickname, new MSimpleString(choose));
        try {
            LM.getGameThread(gameID).getServerInterpreter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCard(String nickname, int x, int y, int gameID) throws RemoteException {
        Message m = new Message(VIEWUPDATE, SHOWCARD, nickname, new MCoords(x, y));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(gameID).getServerInterpreter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(m);
        m = sms.getGameMessage(nickname);
        //aggiorna cli
    }

    public void showField(String nickname, String player, int gameID) throws RemoteException {
        Message message = new Message(VIEWUPDATE, SHOWFIELD, nickname, new MSimpleString(player));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(gameID).getServerInterpreter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(message);
        message = sms.getGameMessage(nickname);
        //aggiorna cli
    }

    public void placement() throws RemoteException {

    }
}
