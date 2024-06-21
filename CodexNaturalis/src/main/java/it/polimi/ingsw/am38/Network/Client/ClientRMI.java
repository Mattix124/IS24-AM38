package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;
import it.polimi.ingsw.am38.Network.Server.Turnings;
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
	private final ClientDATA clientData = ClientDATA.getClientDATA();
	private Viewable viewInterface;

	/**
	 * Constructor of the ClientRMI
	 *
	 * @param ip   is the ip needed for the connection
	 * @param port is the server port
	 * @throws RemoteException
	 */
	public ClientRMI(String ip, int port, Viewable viewInterface) throws RemoteException
	{
		this.ip = ip;
		this.port = port;
		this.viewInterface = viewInterface;
		cmi = new ClientCommandInterpreter(this, viewInterface);
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
		viewInterface.startView();
	}

	/**
	 * getter method for the Viewable interface
	 *
	 * @return the Viewable instance in this.viewInterface
	 */
	@Override
	public Viewable getViewableInterface() throws RemoteException
	{
		return this.viewInterface;
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
		clientData.setStarterCards(starters);
		clientData.setGGround(goldGround);
		clientData.setRGround(resourceGround);
		clientData.setGTop(goldTop);
		clientData.setRTop(resourceTop);
		viewInterface.starterCardFacingChoice(clientData.getStarterCard(clientData.getNickname()), clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2());
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
	public void printChatMessage(String message) throws RemoteException
	{
		viewInterface.receiveMessage(message);
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

	public void setChoosingObjective(int[] obj, int[] hand, HashMap<String,Boolean> starterFacings, HashMap <String, Color> playersColors, HashMap <String,Symbol[]> handsColors, String[] phrases)
	{
		cmi.setTurning(Turnings.CHOOSE3);
		viewInterface.sendString(phrases[0]);
		clientData.setObjectives(obj);
		clientData.setStarterCardsFacing(starterFacings);
		clientData.setStartingHand(hand);
		clientData.setHandCardsColors(handsColors);
		clientData.setPlayersColors(playersColors);
		viewInterface.personalObjectiveChoice(clientData.getPlayersNickAndColor(), clientData.getHandCardsColors(), clientData.getStarters(), clientData.getHand(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getObjectiveChoice1(), clientData.getObjectiveChoice2());
		viewInterface.sendString(phrases[1]);
	}

	public void sendLine(String message)
	{
		viewInterface.sendString(message);
	}

	@Override
	public void display(String s) throws RemoteException
	{
		viewInterface.displayString(s);
	}

	@Override
	public void enterGame(String s)
	{
		viewInterface.sendString(s);
		viewInterface.showFirstScreen(this.nickname);
		viewInterface.printHelp();
	}

	@Override
	public void turnShifter(String s) throws RemoteException
	{
		viewInterface.priorityString(s, 2);
	}

	@Override
	public void noPossiblePlacement(String s) throws RemoteException
	{
		viewInterface.priorityString(s,2);

	}

	@Override
	public void emptyDeck(String s) throws RemoteException
	{
		viewInterface.priorityString("The deck is now empty!",1);
		cmi.removeFromAvailableDeck(""); //OKKIO
	}

	@Override
	public void lightError(String s) throws RemoteException
	{
		viewInterface.priorityString(s,1);
	}

	@Override
	public void confirmedPlacement(int id, int x, int y, boolean face, int points, VisibleElements visibleElements) throws RemoteException
	{
		viewInterface.sendString("Your card is placed correctly");
		clientData.addCardToPlayerField(nickname, id, x, y, face);
		viewInterface.setCardInField(nickname, clientData.getCardFromPlayerField(x, y), x, y);
		//view.setSymbolsTab(nickname,visibleElements);
		viewInterface.updateScore(nickname,points);
	}

	@Override
	public void confirmedDraw(int cardDrawnId,int goldFaceUp1Id, int goldFaceUp2Id,int resFaceUp1Id, int resFaceUp2Id,Symbol goldTopCardSymbol, Symbol resTopCardSymbol) throws RemoteException
	{
		clientData.cardDrawn(cardDrawnId);
		clientData.setGGround1(goldFaceUp1Id);
		clientData.setGGround2(goldFaceUp2Id);
		clientData.setRGround1(resFaceUp1Id);
		clientData.setRGround2(resFaceUp2Id);
		clientData.setGTop(goldTopCardSymbol);
		clientData.setRTop(resTopCardSymbol);
		viewInterface.updateDraw(clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getHand());
	}

	@Override
	public void winnersMessage(String s) throws RemoteException
	{
		viewInterface.displayString(s);
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
		clientData.setNickname(s);
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

	public void clientPing() throws RemoteException
	{
		signalsPingArrived();
	}

	@Override
	public String getNickname()
	{
		return nickname;
	}

	@Override
	public void reconnectionDataUpdate(HashMap <String, PlayerDisconnectionResendInfo> playersData, int hangingDrawnId) throws RemoteException
	{

	}
}
