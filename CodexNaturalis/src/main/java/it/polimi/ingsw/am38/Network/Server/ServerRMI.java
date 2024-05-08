package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ServerRMI  implements InterfaceRMI, Serializable {

    private static final long serialVersionUID = -2905395065429128985L;

    private int port;
    private Registry reg;
    private LobbyManager LM;
    private static LinkedList <GameThread> gameThreadList = null;

    public ServerRMI(int port) throws RemoteException {
        this.port = port;
        gameThreadList = new LinkedList<>();
    }

    public void start() throws RemoteException{
        InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(this, this.port);
        reg = LocateRegistry.createRegistry(this.port);
        try {
            reg.bind("server_RMI", stub);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Server RMI ready");
    }

    public void setLobbyManager(LobbyManager LM){this.LM = LM;}


    public void join(String nickname, int gameID) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        Player p = new Player(nickname);
        LM.joinGame(gameID, p);
    }


    public int createGame(String nickname, int numberOfPlayers) throws RemoteException, NumOfPlayersException {
        int gameID;
        synchronized (LM){
            Player p = new Player(nickname);
            gameID = LM.createNewGame(numberOfPlayers, p);
            GameThread gt = new GameThread(p,gameID, numberOfPlayers);
            gt.start();
            System.out.println("You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameID);
        }
        return gameID;
    }
}
