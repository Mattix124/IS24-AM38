package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MConfirmedDraw;
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
	private ClientWriter cw;
	private boolean arrivedPing;
	private boolean disconnection = false;
	private boolean waitLogin;
	private final ClientCommandInterpreter cci;
	private ClientPingerThread cpt;
	private final ClientDATA clientData = ClientDATA.getClientDATA();
	private Viewable viewInterface;
	private String loginString;


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
		cci = new ClientCommandInterpreter(this, viewInterface);
		this.cpt = new ClientPingerThread(this, viewInterface);
		cpt.setDaemon(true);
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

		this.cw = viewInterface.startView(cci);
		if (cw != null)
			cw.start();
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
		if (cw != null)
			cw.removeLoginPhase();
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
		cci.setTurning(t);
	}

	public void setChoosingObjective(int[] obj, int[] hand, Symbol topG, Symbol topR, HashMap <String, Boolean> starterFacings, HashMap <String, Color> playersColors, HashMap <String, Symbol[]> handsColors, String[] phrases, HashMap<String, VisibleElements> pve)
	{
		cci.setTurning(Turnings.CHOOSE3);
		viewInterface.sendString(phrases[0]);
		clientData.setObjectives(obj);
		clientData.setStarterCardsFacing(starterFacings);
		clientData.setStartingHand(hand);
		clientData.setHandCardsColors(handsColors);
		clientData.setPlayersColors(playersColors);
		clientData.setGTop(topG);
		clientData.setRTop(topR);
		pve.forEach(clientData::setSymbolTab);
		viewInterface.personalObjectiveChoice(clientData.getGTop(), clientData.getRTop(), clientData.getNickname(), clientData.getPlayersNickAndColor(), clientData.getHandCardsColors(), clientData.getStarters(), clientData.getHand(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getObjectiveChoice1(), clientData.getObjectiveChoice2(), pve);
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
	public void displayStringLogin(String s)
	{
		viewInterface.displayStringLogin(s);
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
		viewInterface.priorityString(s);
	}

	@Override
	public void noPossiblePlacement(String s) throws RemoteException
	{
		viewInterface.priorityString(s);

	}

	@Override
	public void emptyDeck(String s) throws RemoteException
	{
		viewInterface.priorityString("The deck is now empty!");
		//cci.removeFromAvailableDeck(""); //OKKIO
	}

	@Override
	public void lightError(String s) throws RemoteException
	{
		viewInterface.priorityString(s);
	}

	@Override
	public void confirmedPlacement(String user, int id, int x, int y, boolean face, int points, VisibleElements visibleElements) throws RemoteException
	{
		if (nickname.equals(user))
			viewInterface.sendString("Your card is placed correctly");
		clientData.addCardToPlayerField(user, id, x, y, face);
		clientData.cardPlayed(id);
		clientData.setSymbolTab(user, visibleElements);
		clientData.setScore(user, points);
		viewInterface.setCardInField(user, clientData.getCardFromPlayerField(user, x, y), x, y);
		viewInterface.setHandAfterPlacement(clientData.getHand());
		viewInterface.setSymbolsTab(user, clientData.getSymbolTab(user));
		viewInterface.updateScore(user, clientData.getScore(user));
	}

	@Override
	public void confirmedDraw(int cardDrawnId, int goldFaceUp1Id, int goldFaceUp2Id, int resFaceUp1Id, int resFaceUp2Id, Symbol goldTopCardSymbol, Symbol resTopCardSymbol) throws RemoteException
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

	public void otherDrawUpdate(String nickname, int resourceFaceUp1Id,int  resourceFaceUp2Id, int goldFaceUp1Id,int goldFaceUp2Id,Symbol resourceTopCardSymbol,Symbol goldTopCardSymbol,int cardDrawnId, Symbol[] playerHandCardColors ) throws RemoteException{
		clientData.setGGround1(goldFaceUp1Id);
		clientData.setGGround2(goldFaceUp2Id);
		clientData.setRGround1(resourceFaceUp1Id);
		clientData.setRGround2(resourceFaceUp2Id);
		clientData.setGTop(goldTopCardSymbol);
		clientData.setRTop(resourceTopCardSymbol);
		clientData.setPlayerHandCardColors(nickname, playerHandCardColors);
		viewInterface.updateOtherPlayerDraw(nickname, clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getGTop(), clientData.getRTop(), clientData.getPlayerHandCardsColor(nickname));
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
	public void sendStringLogin(String s) throws RemoteException
	{
		loginString = s;
		synchronized (this)
		{
			waitLogin = false;
			this.notifyAll();
		}
	}

	@Override
	public String getStringLogin() throws RemoteException
	{
		waitLogin = true;
		synchronized (this)
		{
			while (waitLogin)
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
		}
		return loginString;
	}

	@Override
	public void reconnectionDataUpdate(HashMap <String, PlayerDisconnectionResendInfo> playersData, int hangingDrawnId) throws RemoteException
	{

	}
}
