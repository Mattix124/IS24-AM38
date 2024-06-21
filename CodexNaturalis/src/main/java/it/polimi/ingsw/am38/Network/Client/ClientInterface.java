package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import it.polimi.ingsw.am38.Enum.Color;

/**
 * This interface is implemented from the ClientRMI
 */
public interface ClientInterface extends Remote, Serializable
{

	/**
	 * This method let the server know when a client wants to perform a login (i.e. create an instance of Player)
	 *
	 * @throws RemoteException
	 */
	String getString() throws RemoteException;

	/**
	 * Method that set in the ClientCommandInterpreter the phase of the game to let it elaborate only certain type of input
	 *
	 * @param t is the phase to set (ENUM)
	 * @throws RemoteException
	 */
	void setPhase(Turnings t) throws RemoteException;

	/**
	 * Method to set in the ClientDATA the id of the starter card drawn by each player
	 *
	 * @param starterCards is the HashMap containing as key: the nickname of each player, as value: their starter cards
	 * @throws RemoteException
	 */
	void setStarterCards(HashMap <String, Integer> starterCards, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround) throws RemoteException;

	/**
	 * Method called from the server on the client to show the objectives from which to choose
	 *
	 * @throws RemoteException
	 */
	void setChoosingObjective(int[] obj,int[] hand,HashMap<String,Boolean> starterFacings,HashMap <String,Color> playersColors,HashMap <String,Symbol[]> handsColors,String[] phrases) throws RemoteException;

	/**
	 * Method to print a certain message (called by the server)
	 *
	 * @param message the string to print
	 * @throws RemoteException
	 */
	void sendLine(String message) throws RemoteException;

	void display(String s) throws RemoteException;

	void enterGame(String message) throws RemoteException;

	void turnShifter(String s) throws RemoteException;

	void noPossiblePlacement(String s) throws RemoteException;

	void confirmedDraw(int cardDrawnId, int goldFaceUp1id, int goldFaceUp2Id, int resFaceUp1Id, int resFaceUp2Id, Symbol goldTopCardSymbol, Symbol resTopCardSymbol) throws RemoteException;

	void emptyDeck(String s) throws RemoteException;

	void lightError(String s) throws RemoteException;

	void confirmedPlacement(String nickname, int id, int x, int y, boolean face, int points, VisibleElements visibleElements) throws RemoteException;

	void winnersMessage(String s) throws RemoteException;

	void setSort(ClientInterface ci) throws RemoteException;

	void sendNickname(String s) throws RemoteException;

	void startPing() throws RemoteException;

	void clientPing() throws RemoteException;

	void printChatMessage(String message) throws RemoteException;

	void reconnectionDataUpdate(HashMap <String, PlayerDisconnectionResendInfo> playersData, int hangingDrawnId) throws RemoteException;
}
