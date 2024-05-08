package it.polimi.ingsw.am38.Network.Server;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote, Serializable {
    public void join(String nickname, int gameID) throws RemoteException, NumOfPlayersException, GameNotFoundException;
    public int createGame(String nickname, int numberOfPlayers) throws RemoteException, NumOfPlayersException;


}
