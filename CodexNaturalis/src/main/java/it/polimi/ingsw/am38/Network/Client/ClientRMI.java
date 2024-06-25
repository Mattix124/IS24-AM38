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
	/**
	 * Nickname of the player
	 */
	private String nickname;
	/**
	 * Ip of the server
	 */
	private String ip;
	/**
	 * Port of the server
	 */
	private int port;
	/**
	 * Registry of the server to set the connection
	 */
	private Registry reg;
	/**
	 * Interface of the serverRMI
	 */
	private InterfaceRMI intRMI;
	/**
	 * Instance of ClientWriter
	 */
	private ClientWriter cw;
	/**
	 * Boolean to check if the ping has arrived from the server
	 */
	private boolean arrivedPing;
	/**
	 * Boolean to check if the player is disconnected
	 */
	private boolean disconnection = false;
	/**
	 *
	 */
	private boolean waitLogin;
	/**
	 * Instance of the ClientCommandInterpreter
	 */
	private final ClientCommandInterpreter cci;
	/**
	 * Instance of the ClientPingerThread
	 */
	private ClientPingerThread cpt;
	/**
	 * Instance of the ClientDATA
	 */
	private final ClientDATA clientData = ClientDATA.getClientDATA();
	/**
	 * Instance of the interface of the view
	 */
	private Viewable viewInterface;
	/**
	 *
	 */
	private String loginString;

	/**
	 * Constructor of the ClientRMI
	 *
	 * @param ip            is the ip needed for the connection
	 * @param port          is the server port
	 * @param viewInterface interface of the view
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

	/**
	 * Method to set and display the information to choose the starter card face
	 *
	 * @param starters       the HashMap containing as key: the nickname of each player, as value: their starter cards
	 * @param goldTop        the kingdom of the card on top of the gold deck
	 * @param resourceTop    the kingdom of the card on top of the resource deck
	 * @param goldGround     the id of the gold cards on the ground
	 * @param resourceGround the id of the resource cards on the ground
	 */
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

	/**
	 * Method that communicate to the server the face chosen for the starter card
	 *
	 * @param face is the face chosen
	 * @throws RemoteException
	 */
	public void chooseFaceStarterCard(String face) throws RemoteException
	{
		intRMI.chooseFaceStarterCard(nickname, face);
	}

	/**
	 * Method that communicate to the server the color chosen for the pawn
	 *
	 * @param color is the color chosen
	 * @throws RemoteException
	 */
	public void chooseColor(String color) throws RemoteException
	{
		intRMI.chooseColor(nickname, color);
	}

	/**
	 * Method that communicate to the server the objective card chosen
	 *
	 * @param choose is the objective chosen
	 * @throws RemoteException
	 */
	public void chooseObjectiveCard(String choose) throws RemoteException
	{
		intRMI.chooseObjectiveCard(nickname, choose);
	}

	/**
	 * Method to send a message to every player
	 *
	 * @param message the string that represent the message
	 * @throws RemoteException
	 */
	@Override
	public void broadcastMessage(StringBuilder message) throws RemoteException
	{
		intRMI.broadcastMessage(message, nickname);
	}

	/**
	 * Method to show a chat message
	 *
	 * @param message the string that represent the message
	 * @throws RemoteException
	 */
	@Override
	public void printChatMessage(String message) throws RemoteException
	{
		viewInterface.receiveMessage(message);
	}

	/**
	 * Method to send a message to a certain player
	 *
	 * @param receiver is the nickname of the player to whom to send the message
	 * @param message  the string that represent the message
	 * @throws RemoteException
	 */
	@Override
	public void privateMessage(String receiver, StringBuilder message) throws RemoteException
	{
		intRMI.privateMessage(message, receiver, nickname);
	}

	/**
	 * Method to set the game phase of a player
	 *
	 * @param t is the phase to set (ENUM)
	 */
	public void setPhase(Turnings t)
	{
		cci.setTurning(t);
	}

	/**
	 * Method to set and show the information to choose the objective card
	 *
	 * @param obj            array with the id of the two objectives from which to choose and the two common objective cards
	 * @param hand           the hand of the player
	 * @param topG           the kingdom of the card on top of the gold deck
	 * @param topR           the kingdom of the card on top of the resource deck
	 * @param starterFacings the faces of the starters of every player
	 * @param playersColors  the color of the pawns of every player
	 * @param handsColors    the kingdoms of the cards in the hand of every player
	 * @param phrases        message to display
	 * @param pve            VisibleElements of every player
	 */
	public void setChoosingObjective(int[] obj, int[] hand, Symbol topG, Symbol topR, HashMap <String, Boolean> starterFacings, HashMap <String, Color> playersColors, HashMap <String, String[]> handsColors, String[] phrases, HashMap <String, VisibleElements> pve)
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
		System.out.println("dsadas");
		viewInterface.personalObjectiveChoice(clientData.getGTop(), clientData.getRTop(), clientData.getNickname(), clientData.getPlayersNickAndColor(), clientData.getHandCardsColors(), clientData.getStarters(), clientData.getHand(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getObjectiveChoice1(), clientData.getObjectiveChoice2(), pve);
		viewInterface.sendString(phrases[1]);
	}

	/**
	 * Method called by the server to send a string to the clientRMI
	 *
	 * @param message the string to print
	 */
	public void sendLine(String message)
	{
		viewInterface.priorityString(message);
	}

	/**
	 * Method to send to the client a message to display
	 *
	 * @param s the message to display
	 * @throws RemoteException
	 */
	@Override
	public void display(String s) throws RemoteException
	{
		viewInterface.displayString(s);
	}

	/**
	 * @param s
	 */
	@Override
	public void displayStringLogin(String s)
	{
		viewInterface.displayStringLogin(s);
	}

	/**
	 * Method to display the actual game view after the setup phase
	 *
	 * @param s to display
	 */
	@Override
	public void enterGame(String s)
	{
		viewInterface.sendString(s);
		viewInterface.showFirstScreen(this.nickname);
		viewInterface.printHelp();
	}

	/**
	 * Method that communicates the player whose turn it is
	 *
	 * @param s nickname of the player whose turn it is
	 * @throws RemoteException
	 */
	@Override
	public void turnShifter(String s) throws RemoteException
	{
		viewInterface.priorityString(s);
	}

	/**
	 * Method that communicates that a card cannot be placed in the spot selected
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	@Override
	public void noPossiblePlacement(String s) throws RemoteException
	{
		viewInterface.priorityString(s);

	}

	/**
	 * Method that communicates that a deck is empty
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	@Override
	public void emptyDeck(String s) throws RemoteException
	{
		viewInterface.priorityString("The deck is now empty!");
		//cci.removeFromAvailableDeck(""); //OKKIO
	}

	/**
	 * Method to display a minor error message
	 *
	 * @param s error message
	 * @throws RemoteException
	 */
	@Override
	public void lightError(String s) throws RemoteException
	{
		viewInterface.priorityString(s);
	}

	/**
	 * Method to update the client's info after any player has placed
	 *
	 * @param user            of the player that has placed a card
	 * @param id              of the card placed
	 * @param x               coordinates on the player's field
	 * @param y               coordinates on the player's field
	 * @param face            chosen for the card placed
	 * @param points          given by the card placed
	 * @param visibleElements elements on the field updated after the card placed
	 * @throws RemoteException
	 */
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

	/**
	 * Method to update the client's info after a draw gone well
	 *
	 * @param cardDrawnId       id of the card drawn
	 * @param goldFaceUp1Id     id of one of the gold card on the ground
	 * @param goldFaceUp2Id     id of one of the gold card on the ground
	 * @param resFaceUp1Id      id of one of the resource card on the ground
	 * @param resFaceUp2Id      id of one of the resource card on the ground
	 * @param goldTopCardSymbol id of the gold card on top of the deck
	 * @param resTopCardSymbol  id of the resource card on top of the deck
	 * @throws RemoteException
	 */
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

	/**
	 * Method to notify other player's draw
	 *
	 * @param nickname              of the player who has drawn
	 * @param resourceFaceUp1Id     id of one of the resource cards on the ground
	 * @param resourceFaceUp2Id     id of one of the resource cards on the ground
	 * @param goldFaceUp1Id         id of one of the gold cards on the ground
	 * @param goldFaceUp2Id         id of one of the gold cards on the ground
	 * @param resourceTopCardSymbol id of the resource card on top of the deck
	 * @param goldTopCardSymbol     id of the gold cards on top of the deck
	 * @param cardDrawnId           id of the card drawn
	 * @param playerHandCardColors  hashMap of the players and their cards in the hands
	 * @throws RemoteException
	 */
	public void otherDrawUpdate(String nickname, int resourceFaceUp1Id, int resourceFaceUp2Id, int goldFaceUp1Id, int goldFaceUp2Id, Symbol resourceTopCardSymbol, Symbol goldTopCardSymbol, int cardDrawnId, String[] playerHandCardColors) throws RemoteException
	{
		clientData.setGGround1(goldFaceUp1Id);
		clientData.setGGround2(goldFaceUp2Id);
		clientData.setRGround1(resourceFaceUp1Id);
		clientData.setRGround2(resourceFaceUp2Id);
		clientData.setGTop(goldTopCardSymbol);
		clientData.setRTop(resourceTopCardSymbol);
		clientData.setPlayerHandCardColors(nickname, playerHandCardColors);
		viewInterface.updateOtherPlayerDraw(nickname, clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getGTop(), clientData.getRTop(), clientData.getPlayerHandCardsColor(nickname));
	}

	/**
	 * Method to show the winners
	 *
	 * @param s string with the winners
	 * @throws RemoteException
	 */
	@Override
	public void winnersMessage(String s) throws RemoteException
	{
		viewInterface.displayString(s);
	}

	/**
	 * Method to start the SortPlayerThread
	 *
	 * @param ci this interface
	 * @throws RemoteException
	 */
	public void setSort(ClientInterface ci) throws RemoteException
	{
		intRMI.setSort(ci);
	}

	/**
	 * Method to send the nickname to the client and set it in the ClientData (called by the server)
	 *
	 * @param s the nickname
	 */
	public void sendNickname(String s)
	{
		setNickname(s);
	}

	/**
	 * Method to set the nickname in the ClientData
	 *
	 * @param s the nickname
	 */
	@Override
	public void setNickname(String s)
	{
		clientData.setNickname(s);
		this.nickname = s;
	}

	/**
	 * Method to start the ping with the server
	 */
	public void startPing()
	{
		cpt.start();
	}

	/**
	 * Method to send a ping to the server
	 *
	 * @throws RemoteException
	 */
	@Override
	public void ping() throws RemoteException
	{
		intRMI.pingIn(nickname);
	}

	/**
	 * Method that confirm the ping has arrived
	 *
	 * @throws RemoteException
	 */
	@Override
	public void signalsPingArrived() throws RemoteException
	{
		synchronized (this)
		{
			arrivedPing = true;
			this.notifyAll();
		}
	}

	/**
	 * Method to kill the client due to a disconnection
	 *
	 * @param code
	 * @throws RemoteException
	 */
	@Override
	public void killer(int code) throws RemoteException
	{
		UnicastRemoteObject.unexportObject(intRMI, true);
		intRMI = null;
	}

	/**
	 * Method that waits for a ping response
	 *
	 * @throws RemoteException
	 */
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

	/**
	 * Method to set the client to disconnected
	 */
	@Override
	public void setDisconnection()
	{
		synchronized (this)
		{
			disconnection = true;
			this.notifyAll();
		}
	}

	/**
	 * Method to check the ping has arrived from the server
	 *
	 * @throws RemoteException
	 */
	public void clientPing() throws RemoteException
	{
		signalsPingArrived();
	}

	/**
	 * Getter for the nickname
	 *
	 * @return
	 */
	@Override
	public String getNickname()
	{
		return nickname;
	}

	/**
	 * @param s
	 * @throws RemoteException
	 */
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

	/**
	 * Method for the client to wait the player to insert a nickname
	 *
	 * @return
	 * @throws RemoteException
	 */
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

	/**
	 * Method to get the information after a disconnection
	 *
	 * @param playersData
	 * @param hangingDrawnId
	 * @throws RemoteException
	 */
	@Override
	public void reconnectionDataUpdate(HashMap <String, PlayerDisconnectionResendInfo> playersData, int hangingDrawnId) throws RemoteException
	{

	}
}
