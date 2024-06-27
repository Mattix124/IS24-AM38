package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * ClientMessageSorter is a class that allow the division of the message came from the server
 */
public class ParserTCP
{
	/**
	 * Instance of ClientCommandInterpreter
	 */
	private final ClientCommandInterpreter cci;
	/**
	 * Instance of the interface of the client
	 */
	private CommonClientInterface inter;
	/**
	 * Instance of the thread that manage the ping
	 */
	private ClientPingerThread cpt;
	/**
	 * Instance of the thread that reads the command for the TUI
	 */
	private ClientWriter cw;
	/**
	 * Instance of ClientDATA
	 */
	private final ClientDATA clientData;
	/**
	 * Instance of the view interface
	 */
	private final Viewable view;


	/**
	 * Constructor of ClientMessageSorter
	 *
	 * @param cci the instance of ClientCommandInterpreter that also the ClientWriter have
	 */
	public ParserTCP(ClientCommandInterpreter cci)
	{
		this.cci = cci;
		this.inter = cci.getInterface();
		this.view = cci.getViewInterface();
		this.clientData = cci.getClientData();
	}

	/**
	 * Method allows the message to be interpreted by its content
	 */
	public void addMessage(Message message)
	{
		try
		{
			switch (message.getHeader1())
			{

				case CHAT ->
				{
					MSimpleString mess = (MSimpleString) message.getContent();
					view.receiveMessage(mess.getText());
				}

				case GAME ->
				{

					switch (message.getHeader2())
					{
						case STARTINGFACECHOICE -> //setup of view and start choice of the starter's face
						{
							if (cw != null)
								cw.removeLoginPhase();
							cci.setTurning(CHOOSE1);
							MStartSetup content = (MStartSetup) message.getContent();
							clientData.setStarterCards(content.getStarterCards());
							clientData.setGGround(content.getGoldGround());
							clientData.setRGround(content.getResourceGround());
							clientData.setGTop(content.getGoldTop());
							clientData.setRTop(content.getResourceTop());
							view.starterCardFacingChoice(clientData.getStarterCard(clientData.getNickname()), clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2());
						}
						case COLORCHOICE -> //choice of color available
						{
							view.priorityString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(CHOOSE2);
						}
						case OBJECTIVECHOICE ->//pre objective setup
						{
							MObjViewSetup content = (MObjViewSetup) message.getContent();
							view.sendString(content.getString(0));
							clientData.setObjectives(content.getObjectives());
							clientData.setStarterCardsFacing(content.getStarterFacings());
							clientData.setStartingHand(content.getFirstHand());
							clientData.setHandsCardsColors(content.getHandsColors());
							clientData.setPlayersColors(content.getPlayersColors());
							clientData.setGTop(content.getTopG());
							clientData.setRTop(content.getTopR());
							content.getPlayersVisibleElements().forEach(clientData::setSymbolTab);
							view.personalObjectiveChoice(clientData.getGTop(), clientData.getRTop(), clientData.getNickname(), clientData.getPlayersNickAndColor(), clientData.getHandCardsColors(), clientData.getStarters(), clientData.getHand(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getObjectiveChoice1(), clientData.getObjectiveChoice2(), content.getPlayersVisibleElements());
							view.sendString(content.getString(1));
							cci.setTurning(CHOOSE3);
						}
						case INFOMESSAGE ->
						{
							view.playersTurn(((MSimpleString) message.getContent()).getText());
							cci.setTurning(NOCTURN);
						}
						case PLAYCARD -> //The player can now place a card
						{
							cci.setTurning(PLAYPHASE);
							view.priorityString(((MSimpleString) message.getContent()).getText());
						}
						case PLACEMENT -> //confirmed placement
						{
							MConfirmedPlacement content = (MConfirmedPlacement) message.getContent();
							String              user    = content.getNickname();
							if (user.equals(inter.getNickname())) {
								view.sendString("Your card is placed correctly");
								clientData.cardPlayed(content.getId());
								view.setHandAfterPlacement(clientData.getHand());
							}
							clientData.addCardToPlayerField(user, content.getId(), content.getX(), content.getY(), content.isFace());
							clientData.setSymbolTab(user, content.getVisibleElements());
							clientData.setScore(user, content.getPoints());
							view.setCardInField(user, clientData.getCardFromPlayerField(user, content.getX(), content.getY()), content.getX(), content.getY());
							view.setSymbolsTab(user , clientData.getSymbolTab(user));
							view.updateScore(user, clientData.getScore(user));
							view.updateScreen();
						}
						case NOPOSSIBLEPLACEMENT ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(NOCTURN);
						}
						case NOTPLACEABLE ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText());
						}
						case DRAWCARD -> //The player can now draw a card
						{
							cci.setTurning(DRAWPHASE);
							view.priorityString(((MSimpleString) message.getContent()).getText());
						}

						case DRAWCONFIRMED ->
						{
							MConfirmedDraw content = (MConfirmedDraw) message.getContent();
							clientData.cardDrawn(content.getCardDrawnId());
							clientData.setGGround1(content.getGoldFaceUp1Id());
							clientData.setGGround2(content.getGoldFaceUp2Id());
							clientData.setRGround1(content.getResourceFaceUp1Id());
							clientData.setRGround2(content.getResourceFaceUp2Id());
							clientData.setGTop(content.getGoldTopCardSymbol());
							clientData.setRTop(content.getResourceTopCardSymbol());
							view.updateDraw(clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getHand());
							view.updateScreen();
						}
						case EMPTYDECK ->
						{
							view.priorityString("Empty/The deck is now empty!"); //LAVORACI

						}
						case WINNER ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(NOCTURN);//last message
						}

					}
				}

				case EXCEPTION -> //(NoPossiblePlacement...)
				{
					switch (message.getHeader2())
					{
						case PLACEMENT ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(NOCTURN);
						}
						case INFOMESSAGE -> view.priorityString(((MSimpleString) message.getContent()).getText());
					}
				}
				case LOGIN ->
				{
					MSimpleString content = (MSimpleString) message.getContent();

					switch (message.getHeader2())
					{
						case INFOMESSAGE -> //Login phase
						{
							view.displayStringLogin(content.getText());
						}
						case NICKNAME -> inter.setNickname(content.getText());

					}
				}
				case VIEWUPDATE -> //Update the view
				{
					switch (message.getHeader2())
					{
						case DRAWCONFIRMED ->
						{
							MConfirmedDraw content = (MConfirmedDraw) message.getContent();
							clientData.setGGround1(content.getGoldFaceUp1Id());
							clientData.setGGround2(content.getGoldFaceUp2Id());
							clientData.setRGround1(content.getResourceFaceUp1Id());
							clientData.setRGround2(content.getResourceFaceUp2Id());
							clientData.setGTop(content.getGoldTopCardSymbol());
							clientData.setRTop(content.getResourceTopCardSymbol());
							clientData.setPlayerHandCardColors(content.getNickname(), content.getPlayerHandCardColors());
							view.updateOtherPlayerDraw(content.getNickname(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getGTop(), clientData.getRTop(), clientData.getPlayerHandCardsColor(content.getNickname()));
							view.updateScreen();
						}
					}

				}
				case INFOMESSAGE -> //get the message (string from a server)
				{
					MSimpleString content = (MSimpleString) message.getContent();
					switch (message.getHeader2())
					{
						case START -> view.displayString(content.getText());

						case GAME -> //1 time only message (or reconnection), it updates the view
						{
							view.sendString(content.getText());
							view.showFirstScreen(clientData.getNickname());
							view.printHelp();
							cci.setTurning(NOCTURN);
						}
						case EXCEPTION -> view.priorityString(content.getText());
					}
				}

				case CONNECTION -> //Ping related Message
				{
					switch (message.getHeader2())
					{
						case START -> cpt.start();
						case CONNECTION -> inter.signalsPingArrived();
						case VIEWUPDATE -> {
							MReconnectionInfo c = (MReconnectionInfo) message.getContent();
							clientData.setNickname(c.getNickname());
							clientData.setGGround1(c.getUpGc1());
							clientData.setGGround2(c.getUpGc2());
							clientData.setRGround1(c.getUpRc1());
							clientData.setRGround2(c.getUpRc2());
							clientData.setGTop(c.getGoldTop());
							clientData.setRTop(c.getResourceTop());
							clientData.setStartingHand(c.getOwnHand());
							int[] sObj = new int[2];
							sObj[0] = c.getSharedObj1();
							sObj[1] = c.getSharedObj2();
							clientData.setObjectives(sObj);
							clientData.setPersonalObj(c.getPersonalObj());
							HashMap<String, Integer> starterCards = new HashMap<>();
							c.getPlayersInfo().forEach((k,v) -> {
								starterCards.put(k, v.getDisconnectionDataCard().removeFirst().getId());
								clientData.setScore(k, v.getPoints());
								clientData.setPlayerColor(k, v.getColor());
								if(!k.equals(clientData.getNickname()))
									clientData.setPlayerHandCardColors(k, v.getHandColor());
								clientData.setSymbolTab(k, v.getSymTab());
							});
							clientData.setStarterCards(starterCards);

							view.reconnectionInitialSetter(clientData.getNickname(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getPersonalObjective(), clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2(), clientData.getHand(), c.getPlayersInfo());
							c.getPlayersInfo().forEach((k, v) -> {
								//v.getDisconnectionDataCard().removeFirst();?starter cards
								v.getDisconnectionDataCard().forEach(x -> {
									clientData.addCardToPlayerField(k, x.getId(), x.getX(), x.getY(), x.isFace());
									view.reconnectionCardsToPlay(k, clientData.getCardFromPlayerField(k, x.getX(), x.getY()), x.getX(), x.getY());
								});
							});
							view.computeScreen();
							view.updateScreen();
						}
					}
				}
			}
		}
		catch (RemoteException e)
		{
			System.err.println("impossible");
		}
	}

	/**
	 * Getter of ClientCommandInterpreter
	 *
	 * @return ClientCommandInterpreter
	 */
	public ClientCommandInterpreter getCCI()
	{
		return cci;
	}

	/**
	 * Setter for the 2 threads used
	 *
	 * @param cpt pinger thread
	 * @param cw command reader thread
	 */
	public void setThreads(ClientPingerThread cpt, ClientWriter cw)
	{
		this.cpt = cpt;
		this.cw = cw;
	}
}