package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MConfirmedPlacement;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.LOGIN;
import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * ClientMessageSorter is a class that allow the division of the message came from the server
 */
public class SorterTCP
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

	private ObjectOutputStream tempOut;

	private Scanner tempScan;

	/**
	 * Constructor of ClientMessageSorter
	 *
	 * @param cci the instance of ClientCommandInterpreter that also the ClientWriter have
	 */
	public SorterTCP(ClientCommandInterpreter cci, ObjectOutputStream out)
	{
		this.cci = cci;
		this.tempOut = out;
		this.tempScan = new Scanner(System.in);
		this.inter = cci.getInterface();

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
					cci.getViewInterface().receiveMessage(mess.getText());
				}
				case GAME ->
				{

					switch (message.getHeader2())
					{
						case PLAYCARD ->
						{
							cci.setTurning(PLAYPHASE);
							cci.getViewInterface().sendString(((MSimpleString) message.getContent()).getText());
						}

						case DRAWCARD ->
						{
							cci.setTurning(DRAWPHASE);
							cci.getViewInterface().sendString(((MSimpleString) message.getContent()).getText());
						}

						case STARTINGFACECHOICE ->
						{
							cw.start();
							cci.setTurning(CHOOSE1);
							MStringCard content = (MStringCard) message.getContent();
							cci.getClientData().setStarterCards(content.getStarterCards());
							cci.getClientData().setGGround(content.getGoldGround());
							cci.getClientData().setRGround(content.getResourceGround());
							cci.getClientData().setGTop(content.getGoldTop());
							cci.getClientData().setRTop(content.getResourceTop());
							cci.getViewInterface().starterCardFacingChoice(cci.getClientData().getStarterCard(cci.getClientData().getNickname()), cci.getClientData().getGTop(), cci.getClientData().getRTop(), cci.getClientData().getFaceUpGoldCard1(), cci.getClientData().getFaceUpGoldCard2(), cci.getClientData().getFaceUpResourceCard1(), cci.getClientData().getFaceUpResourceCard2());
						}

						case COLORCHOICE ->
						{
							cci.getViewInterface().sendString(((MSimpleString) message.getContent()).getText());
							cci.setTurning(CHOOSE2);
						}

						case EXCEPTION ->
						{
							cci.setTurning(NOCTURN);
							cci.getViewInterface().errorString(((MSimpleString) message.getContent()).getText(), 2);
						}

						case PLACEMENT ->
						{
							MConfirmedPlacement cardData = (MConfirmedPlacement) message.getContent();
							cci.getViewInterface().sendString("Your card is placed correctly");
						//	cci.getViewInterface().setCardInField(inter.getNickname(), cardData.getId(), cardData.getX(), cardData.getY(), cardData.isFace());
							//conferma piazzamento.
						}
						case WINNER -> cci.setTurning(NOCTURN);

					}
				}
				case LOGIN ->
				{
					MSimpleString content = (MSimpleString) message.getContent();
					switch (message.getHeader2())
					{
						case INFOMESSAGE ->
						{

							cci.getViewInterface().errorString(content.getText(), 1);
							try
							{
								tempOut.writeObject(new Message(LOGIN, LOGIN, new MSimpleString(tempScan.nextLine())));
							}
							catch (IOException e)
							{
								System.err.println("Error sending login commands");
							}
						}
						case NICKNAME -> inter.setNickname(content.getText());
					}
				}
				case VIEWUPDATE ->
				{
					switch (message.getHeader2())
					{

						case OBJECTIVECHOICE ->
						{
							MClientFirstViewSetup content = (MClientFirstViewSetup) message.getContent();
							cci.getViewInterface().sendString(content.getString(0));
							cci.getClientData().setObjectives(content.getObjectives());
							cci.getClientData().setStarterCardsFacing(content.getStarterFacings());
							cci.getClientData().setStartingHand(content.getFirstHand());
							cci.getClientData().setHandCardsColors(content.getHandsColors());
							cci.getClientData().setPlayersColors(content.getPlayersColors());
							cci.getViewInterface().personalObjectiveChoice(cci.getClientData().getPlayersNickAndColor(), cci.getClientData().getHandCardsColors(), cci.getClientData().getStarters(), cci.getClientData().getHand(), cci.getClientData().getSharedObj1(), cci.getClientData().getSharedObj2(), cci.getClientData().getObjectiveChoice1(), cci.getClientData().getObjectiveChoice2());
							cci.getViewInterface().sendString(content.getString(1));
							cci.setTurning(CHOOSE3);
						}

					}
				}
				case INFOMESSAGE ->
				{
					MSimpleString content = (MSimpleString) message.getContent();
					cci.getViewInterface().sendString(content.getText());
					switch (message.getHeader2())
					{
						case START:
						{
							cci.getCLI().printHelpBox();
							cci.getViewInterface().updateScreen();
						}
						case GAME:
						{
							cci.setTurning(NOCTURN);
							break;
						}
						default:
						{

						}
					}
				}
				case CONNECTION ->
				{
					switch (message.getHeader2())
					{
						case START -> cpt.start();

						case CONNECTION -> inter.signalsPingArrived();

					}

				}

			}
		}
		catch (RemoteException e)
		{
			System.out.println("impossible");
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