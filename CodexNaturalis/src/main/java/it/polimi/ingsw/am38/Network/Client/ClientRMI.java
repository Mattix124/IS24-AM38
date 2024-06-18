package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;
import it.polimi.ingsw.am38.Network.Server.Turnings;
import it.polimi.ingsw.am38.View.CLI;
import it.polimi.ingsw.am38.View.Viewable;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This is the class for those clients who decide to connect via RMI
 */
public class ClientRMI extends UnicastRemoteObject implements ClientInterface, CommonClientInterface
{

	private String nickname;
	private String ip;
	private int port;
	private Registry reg;
	private InterfaceRMI intRMI;
	private final ClientWriter cw;
	private boolean arrivedPing;
	private boolean disconnection = false;
	private final ClientCommandInterpreter cmi;
	private ClientPingerThread cpt;
	private Viewable viewInterface;

	/**
	 * Constructor of the ClientRMI
	 *
	 * @param ip   is the ip needed for the connection
	 * @param port is the server port
	 * @throws RemoteException
	 */
	public ClientRMI(String ip, int port) throws RemoteException
	{
		this.ip = ip;
		this.port = port;
		cmi = new ClientCommandInterpreter(this);
		cw = new ClientWriter(cmi);
		cw.setDaemon(true);
		this.cpt = new ClientPingerThread(this);
		cpt.setDaemon(true);
	}

	/**
	 * This is the first method called, set the connection with the server
	 */
	public void start()
	{
		do
		{
			try
			{
				reg = LocateRegistry.getRegistry(ip, port);
				this.intRMI = (InterfaceRMI) reg.lookup("server_RMI");
			}
			catch (RemoteException | NotBoundException e)
			{
				System.err.println("Trying to connect...");
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException w)
				{
					throw new RuntimeException(w);
				}
			}

		} while (intRMI == null || reg == null);
		cmi.getCLI().printTitle();
	}

	/**
	 * getter method for the Viewable interface
	 * @return the Viewable instance in this.viewInterface
	 */
	@Override
	public Viewable getViewableInterface() throws RemoteException{
		return this.viewInterface;
	}

	/**
	 * setter method for the View: can be set to CLI or GUI
	 * @param typeOfView a String containing either "CLI" or "GUI"
	 */
	@Override
	public void setView(String typeOfView) throws RemoteException {
		if(typeOfView.equals("CLI"))
			this.viewInterface = new CLI();
		//else if (typeOfView.equals("GUI"))
		//	this.viewInterface = new GUI();
	}

	/**
	 * This method communicates to the server to create a player with the nickname given
	 *
	 * @throws RemoteException
	 */
	public String getString() throws RemoteException
	{
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	/**
	 * This method communicates to the server the parameters it needs to perform a draw for the player
	 *
	 * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
	 * @param card     is an integer that allows the controller to know which card draw
	 * @throws RemoteException
	 */
	public void draw(String cardType, int card) throws RemoteException
	{
		intRMI.draw(nickname, cardType, card);
	}

	/**
	 * This method communicates to the server the parameters it needs to play a card for a
	 * players in its field
	 *
	 * @param card is the card to play
	 * @param x    the x coordinates where to play the card
	 * @param y    the x coordinates where to play the card
	 * @param face is how the card has to be played, face up or face down
	 * @throws RemoteException
	 */
	public void playACard(int card, int x, int y, boolean face) throws RemoteException
	{
		intRMI.playACard(card, x, y, face, nickname);
	}

	public void setStarterCards(HashMap <String, Integer> starters, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround)
	{
		cw.start();
		cmi.getClientData().setStarterCards(starters);
		cmi.getClientData().setGGround(goldGround);
		cmi.getClientData().setRGround(resourceGround);
		cmi.getClientData().setGTop(goldTop);
		cmi.getClientData().setRTop(resourceTop);
		cmi.getViewInterface().starterCardFacingChoice(cmi.getClientData().getStarterCard(cmi.getClientData().getNickname()), cmi.getClientData().getGTop(), cmi.getClientData().getRTop(), cmi.getClientData().getFaceUpGoldCard1(), cmi.getClientData().getFaceUpGoldCard2(), cmi.getClientData().getFaceUpResourceCard1(), cmi.getClientData().getFaceUpResourceCard2());
	}

	public void chooseFaceStarterCard(String face) throws RemoteException
	{
		intRMI.chooseFaceStarterCard(nickname, face);
	}

	public void chooseColor(String color) throws RemoteException
	{
		intRMI.chooseColor(nickname, color);
	}

	public void chooseObjectiveCard(String choose) throws RemoteException
	{
		intRMI.chooseObjectiveCard(nickname, choose);
	}

	@Override
	public void broadcastMessage(StringBuilder message) throws RemoteException
	{
		intRMI.broadcastMessage(message, nickname);
	}

	@Override
	public void printChatMessage(String message) throws RemoteException {
		cmi.getViewInterface().receiveMessage(message);
		cmi.getCLI().printChat();
	}

	@Override
	public void privateMessage(String receiver, StringBuilder message) throws RemoteException
	{
		intRMI.privateMessage(message, receiver, nickname);
	}

	public void setPhase(Turnings t)
	{
		cmi.setTurning(t);
	}

	public void setChoosingObjective(String obj1, String obj2, int[] obj)
	{
		cmi.setTurning(Turnings.CHOOSE3);
		cmi.getClientData().setObjectives(obj);
		System.out.println(obj1);
		System.out.println(obj2);
		//show obj cards
	}

	public void showCard(int x, int y) throws RemoteException
	{
		intRMI.showCard(nickname, x, y);
	}

	public void showField(String player) throws RemoteException
	{
		intRMI.showField(nickname, player);
	}

	public void placement() throws RemoteException
	{

	}

	public void printLine(String message)
	{
		System.out.println(message);
	}

	public void setSort(ClientInterface ci) throws RemoteException
	{
		intRMI.setSort(ci);
	}

	public void sendNickname(String s)
	{
		setNickname(s);
	}

	@Override
	public void setNickname(String s)
	{
		cmi.getClientData().setNickname(s);
		this.nickname = s;
	}

	public void startPing()
	{
		cpt.start();
	}

	@Override
	public void ping() throws RemoteException
	{
		intRMI.pingIn(nickname);
	}

	@Override
	public void signalsPingArrived() throws RemoteException
	{
		synchronized (this)
		{
			arrivedPing = true;
			this.notifyAll();
		}
	}

	@Override
	public void killer(int code) throws RemoteException
	{
		UnicastRemoteObject.unexportObject(intRMI, true);
		intRMI = null;
	}

	@Override
	public void waitPingConfirm() throws RemoteException
	{
		synchronized (this)
		{
			while (!arrivedPing && !disconnection)
			{
				try
				{
					this.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
			arrivedPing = false;
		}
	}

	@Override
	public void setDisconnection()
	{
		synchronized (this)
		{
			disconnection = true;
			this.notifyAll();
		}
	}

	public void cping() throws RemoteException
	{
		signalsPingArrived();
	}
}
