package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;
import it.polimi.ingsw.am38.Network.Server.Turnings;

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
		cmi.getCLI().printTitle();
		this.cpt = new ClientPingerThread(this);
	}

	/**
	 * This is the first method called, set the connection with the server
	 */
	public void start()
	{
		try
		{
			reg = LocateRegistry.getRegistry(ip, port);
			this.intRMI = (InterfaceRMI) reg.lookup("server_RMI");
		}
		catch (RemoteException | NotBoundException e)
		{
			System.out.println("Server unreachable");
		}
	}

	/**
	 * This method communicates to the server to create a player with the nickname given
	 *
	 * @throws RemoteException
	 */
	public String getString() throws RemoteException
	{
		Scanner s    = new Scanner(System.in);
		String  name = s.nextLine();
		return name;
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
		cmi.getCLI().printStarterCardChoice(cmi.getClientData().getStarterCard(cmi.getClientData().getNickname()), cmi.getClientData().getGTop(), cmi.getClientData().getRTop(), cmi.getClientData().getFaceUpGoldCard1(), cmi.getClientData().getFaceUpGoldCard2(), cmi.getClientData().getFaceUpResourceCard1(), cmi.getClientData().getFaceUpResourceCard2());
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

	}

	@Override
	public void privateMessage(String receiver, StringBuilder message) throws RemoteException
	{

	}

	public void setPhase(Turnings t)
	{
		cmi.setTurning(t);
	}

	public void setChoosingObjective(String obj1, String obj2)
	{
		cmi.setTurning(Turnings.CHOOSE3);
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
		cpt.setNick(s);
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
	public void killer() throws RemoteException
	{
//sa
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
	public void setDisconnection() throws RemoteException
	{
		synchronized (this)
		{
			disconnection = true;
			this.notifyAll();
		}
	}

	public void cping() throws RemoteException
	{
		ping();
	}
}
