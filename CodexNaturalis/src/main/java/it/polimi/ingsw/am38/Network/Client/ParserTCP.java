package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.LOGIN;
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
	 * The ping list is managed asynchronously
	 */
	private CommonClientInterface inter;
	private ClientPingerThread cpt;

	private ClientWriter cw;

	private final ClientDATA clientData = ClientDATA.getClientDATA();

	private final Viewable view;

	private ObjectOutputStream tempOut;

	private Scanner tempScan;

	/**
	 * Constructor of ClientMessageSorter
	 *
	 * @param cci the instance of ClientCommandInterpreter that also the ClientWriter have
	 */
	public ParserTCP(ClientCommandInterpreter cci, ObjectOutputStream out)
	{
		this.cci = cci;
		this.tempOut = out;
		this.tempScan = new Scanner(System.in);
		this.inter = cci.getInterface();
		this.view = cci.getViewInterface();
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
							MStringCard content = (MStringCard) message.getContent();
							clientData.setStarterCards(content.getStarterCards());
							clientData.setGGround(content.getGoldGround());
							clientData.setRGround(content.getResourceGround());
							clientData.setGTop(content.getGoldTop());
							clientData.setRTop(content.getResourceTop());
							view.starterCardFacingChoice(clientData.getStarterCard(clientData.getNickname()), clientData.getGTop(), clientData.getRTop(), clientData.getFaceUpGoldCard1(), clientData.getFaceUpGoldCard2(), clientData.getFaceUpResourceCard1(), clientData.getFaceUpResourceCard2());
						}
						case COLORCHOICE -> //choice of color available
						{
							view.sendString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(CHOOSE2);
						}
						case OBJECTIVECHOICE ->//pre objective setup
						{
							MClientFirstViewSetup content = (MClientFirstViewSetup) message.getContent();
							view.sendString(content.getString(0));
							clientData.setObjectives(content.getObjectives());
							clientData.setStarterCardsFacing(content.getStarterFacings());
							clientData.setStartingHand(content.getFirstHand());
							clientData.setHandCardsColors(content.getHandsColors());
							clientData.setPlayersColors(content.getPlayersColors());
							view.personalObjectiveChoice(clientData.getPlayersNickAndColor(), clientData.getHandCardsColors(), clientData.getStarters(), clientData.getHand(), clientData.getSharedObj1(), clientData.getSharedObj2(), clientData.getObjectiveChoice1(), clientData.getObjectiveChoice2());
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
							view.sendString(((MSimpleString) message.getContent()).getText());
						}
						case PLACEMENT -> //confirmed placement
						{
							MConfirmedPlacement content = (MConfirmedPlacement) message.getContent();
							String              user    = content.getNickname();
							if (user.equals(inter.getNickname()))
								view.sendString("Your card is placed correctly");
							clientData.addCardToPlayerField(user, content.getId(), content.getX(), content.getY(), content.isFace());
							view.setCardInField(user, clientData.getCardFromPlayerField(content.getX(), content.getY()), content.getX(), content.getY());
							//view.setSymbolsTab(inter.getNickname,cardData.getVisibleElements());
							view.updateScore(user, content.getPoints());
						}
						case NOPOSSIBLEPLACEMENT ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText(), 2);
							cci.setTurning(NOCTURN);
						}
						case NOTPLACEABLE ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText(), 1);
						}
						case DRAWCARD -> //The player can now draw a card
						{
							cci.setTurning(DRAWPHASE);
							view.sendString(((MSimpleString) message.getContent()).getText());
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

						}
						case EMPTYDECK ->
						{
							view.priorityString("The deck is now empty!", 1); //LAVORACI
							cci.removeFromAvailableDeck("");
						}
						case WINNER ->
						{
							view.displayString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(NOCTURN);//last message
						}

					}
				}

				case EXCEPTION -> //heavy exception (NoPossiblePlacement...)
				{
					switch (message.getHeader2())
					{
						case PLACEMENT ->
						{
							view.priorityString(((MSimpleString) message.getContent()).getText(), 2);
							cci.setTurning(NOCTURN);
						}
						case INFOMESSAGE -> view.priorityString(((MSimpleString) message.getContent()).getText(), 1);
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
						case EXCEPTION -> view.priorityString(content.getText(), 2);

					}
				}
				case CONNECTION -> //Ping related Message
				{
					switch (message.getHeader2())
					{
						case START -> cpt.start();
						case CONNECTION -> inter.signalsPingArrived();
						//case VIEWUPDATE -> //METODO CLIENT SOVRASCRIZIONE CLIENTDATA
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

	public void setThreads(ClientPingerThread cpt, ClientWriter cw)
	{
		this.cpt = cpt;
		this.cw = cw;
	}
}