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

    public ServerRMI(int port) throws RemoteException {
        this.port = port;
        //gameThreadList = new LinkedList<>();   probabilmente va cambiato
    }

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

    public void join(String nickname, int gameID) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        LM.joinGame(gameID, LM.getPlayer(nickname)); //chiama sul lobby manager la join game
    }


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

    public Player login(String player) throws RemoteException, NicknameTakenException, NullNicknameException {
        Player p;
        synchronized (LM){
            p = LM.createPlayer(player);
            p.setIsPlaying(false);
        }
        return p;
    }

    public void draw(Player player, String cardType, int card, int gameID) throws RemoteException, GameNotFoundException, EmptyDeckException {
        switch (cardType){
            case "gold" :
                switch (card){
                    case 1 : LM.getGame(gameID).getGoldDeck().draw(player); break;
                    case 2 : LM.getGame(gameID).getGoldDeck().draw(player, 0); break;
                    case 3 : LM.getGame(gameID).getGoldDeck().draw(player, 1); break;
                }
            case "resource" :
                switch (card){
                    case 1 : LM.getGame(gameID).getResourceDeck().draw(player); break;
                    case 2 : LM.getGame(gameID).getResourceDeck().draw(player, 0); break;
                    case 3 : LM.getGame(gameID).getResourceDeck().draw(player, 1); break;
                }
        }
    }

    public void playACard(int card, int x, int y, String face) throws NotPlaceableException, RemoteException {

    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }
}
