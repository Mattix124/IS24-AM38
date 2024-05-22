package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayersData;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * ClientMessageSorter is a class that allow the division of the message came from the server
 */
public class ClientMessageSorter extends Thread
{
	/**
	 * Instance of ClientCommandInterpreter
	 */
	private final ClientCommandInterpreter cci;
	/**
	 * The queue used to divide the message came
	 */
	private final LinkedList <Message> queue;

	/**
	 * Constructor of ClientMessageSorter
	 *
	 * @param cci the instance of ClientCommandInterpreter that also the ClientWriter have
	 */
	public ClientMessageSorter(ClientCommandInterpreter cci)
	{
		this.cci = cci;
		this.queue = new LinkedList <>();
	}

	/**
	 * Method stand-alone that divides the message
	 */
	public void run()
	{
		Message message;
		while (true)
		{
			synchronized (queue)
			{
				while (queue.isEmpty())
				{
					try
					{
						queue.wait();
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
				}
				message = queue.removeFirst();
			}
			switch (message.getHeader1())
			{

				case CHAT ->
				{
					//Cli integration

				}
				case GAME ->
				{

					switch (message.getHeader2())
					{
						case PLAYCARD ->
						{
							cci.setTurning(PLAYPHASE);
							System.out.println(((MSimpleString) message.getContent()).getText());
						}

						case DRAWCARD ->
						{
							cci.setTurning(DRAWPHASE);
							System.out.println(((MSimpleString) message.getContent()).getText());
						}

						case STARTINGFACECHOICE ->
						{
							cci.setTurning(CHOOSE1);
							MStringCard content = (MStringCard) message.getContent();
							cci.getClientData().setStarterCards(content.getStarterCards());
							cci.getClientData().setGGround(content.getGoldGrounud());
							cci.getClientData().setRGround(content.getResourceGround());
							cci.getClientData().setGTop(content.getGoldTop());
							cci.getClientData().setRTop(content.getResourceTop());
							cci.getCLI().printStarterCardChoice(cci.getClientData().getStarterCard(cci.getClientData().getNickname()),
									cci.getClientData().getGTop(),
									cci.getClientData().getRTop(),
									cci.getClientData().getFaceUpGoldCard1(),
									cci.getClientData().getFaceUpGoldCard2(),
									cci.getClientData().getFaceUpResourceCard1(),
									cci.getClientData().getFaceUpResourceCard2());
						}

						case COLORCHOICE ->
						{
							System.out.println(((MSimpleString) message.getContent()).getText());
							cci.setTurning(CHOOSE2);
						}

						case EXCEPTION ->
						{
							cci.setTurning(NOCTURN);
							System.out.println(((MSimpleString) message.getContent()).getText());
						}

						case WINNER -> cci.setTurning(NOCTURN);
						case START ->
						{
							cci.getCLI().printHelpBox();
						}
					}
				}

				case VIEWUPDATE ->
				{
					switch (message.getHeader2())
					{
						case PLACEMENT ->
						{
							//CLI
						}
						case SHOWCARD ->
						{
							//CLI
						}
						case SHOWFIELD ->
						{
							//CLI (CHEMMANDO?)
						}

						case PLAYERDATA ->
						{
							cci.getClientData().setNickname(((MPlayersData) message.getContent()).getNickName());
							cci.getClientData().setPlayersNicknames(((MPlayersData) message.getContent()).getPlayersNicknames());
						}
						case OBJECTIVECHOICE ->
						{
							MClientFirstViewSetup content = (MClientFirstViewSetup) message.getContent();
							System.out.println(content.getString(0));
							cci.getClientData().setObjectives(content.getObjectives());
							cci.getClientData().setStarterCardsFacing(content.getStarterFacings());
							cci.getClientData().setHand(content.getFirstHand());
							cci.getClientData().setHandCardsColors(content.getHandsColors());
							cci.getClientData().setPlayersColors(content.getPlayersColors());
							cci.getCLI().postFacingSelectionPrint(cci.getClientData().getPlayersNickAndColor(),
									cci.getClientData().getHandCardsColors(),
									cci.getClientData().getStarters(),
									cci.getClientData().getHand(),
									cci.getClientData().getSharedObj1(),
									cci.getClientData().getSharedObj2(),
									cci.getClientData().getObjectiveChoice1(),
									cci.getClientData().getObjectiveChoice2());
							System.out.println(content.getString(1));
							cci.setTurning(CHOOSE3);
						}

					}
				} case INFOMESSAGE ->
			{
				MSimpleString content = (MSimpleString) message.getContent();
				System.out.println(content.getText());
				switch (message.getHeader2())
				{
					case START -> cci.setTurning(NOCTURN);
					case GAME -> cci.setTurning(NOCTURN);
					default -> cci.setTurning(STANDBY);
				}
			}
			}
		}
	}

	/**
	 * Message added by the TCP Client
	 *
	 * @param m message
	 */
	public void addMessage(Message m)
	{
		synchronized (queue)
		{
			queue.add(m);
			queue.notifyAll();
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
}