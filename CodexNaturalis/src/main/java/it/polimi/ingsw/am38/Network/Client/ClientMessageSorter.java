package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * ClientMessageSorter is a class that allow the division of the message came from the server
 */
public class ClientMessageSorter
{
	/**
	 * Instance of ClientCommandInterpreter
	 */
	private final ClientCommandInterpreter cci;
	/**
	 * The ping list is managed asynchronously
	 */
	private boolean arrivedPing;

	private boolean disconnection = false;

	private ClientPingerThread cpt;

	/**
	 * Constructor of ClientMessageSorter
	 *
	 * @param cci the instance of ClientCommandInterpreter that also the ClientWriter have
	 */
	public ClientMessageSorter(ClientCommandInterpreter cci)
	{
		this.cci = cci;
		this.arrivedPing = true;

	}

	/**
	 * Method allows the message to be interpreted by its content
	 */
	public void addMessage(Message message)
	{
		switch (message.getHeader1())
		{
			case CHAT ->
			{
				MSimpleString mess = (MSimpleString) message.getContent();
				cci.getCLI().receiveMessage(mess.getText());
				cci.getCLI().printChat();
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
						cci.getClientData().setGGround(content.getGoldGround());
						cci.getClientData().setRGround(content.getResourceGround());
						cci.getClientData().setGTop(content.getGoldTop());
						cci.getClientData().setRTop(content.getResourceTop());
						cci.getCLI().printStarterCardChoice(cci.getClientData().getStarterCard(cci.getClientData().getNickname()), cci.getClientData().getGTop(), cci.getClientData().getRTop(), cci.getClientData().getFaceUpGoldCard1(), cci.getClientData().getFaceUpGoldCard2(), cci.getClientData().getFaceUpResourceCard1(), cci.getClientData().getFaceUpResourceCard2());
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

					case PLACEMENT ->
					{

					}
					case WINNER -> cci.setTurning(NOCTURN);
					case START ->
					{
						cci.getCLI().printHelpBox();
					}
				}
			}
			case LOGIN ->
			{
				MSimpleString content = (MSimpleString) message.getContent();
				switch (message.getHeader2())
				{
					case INFOMESSAGE -> System.out.println(content.getText());

					case NICKNAME -> cci.getClientData().setNickname(content.getText());
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

					case OBJECTIVECHOICE ->
					{
						MClientFirstViewSetup content = (MClientFirstViewSetup) message.getContent();
						System.out.println(content.getString(0));
						cci.getClientData().setObjectives(content.getObjectives());
						cci.getClientData().setStarterCardsFacing(content.getStarterFacings());
						cci.getClientData().setHand(content.getFirstHand());
						cci.getClientData().setHandCardsColors(content.getHandsColors());
						cci.getClientData().setPlayersColors(content.getPlayersColors());
						cci.getCLI().postFacingSelectionPrint(cci.getClientData().getPlayersNickAndColor(), cci.getClientData().getHandCardsColors(), cci.getClientData().getStarters(), cci.getClientData().getHand(), cci.getClientData().getSharedObj1(), cci.getClientData().getSharedObj2(), cci.getClientData().getObjectiveChoice1(), cci.getClientData().getObjectiveChoice2());
						System.out.println(content.getString(1));
						cci.setTurning(CHOOSE3);
					}

				}
			}
			case INFOMESSAGE ->
			{
				MSimpleString content = (MSimpleString) message.getContent();
				System.out.println(content.getText());
				switch (message.getHeader2())
				{
					case START, GAME -> cci.setTurning(NOCTURN);
					default ->
					{
					}
				}
			}
			case CONNECTION ->
			{
				switch (message.getHeader2())
				{
					case START -> cpt.start();

					case CONNECTION ->
					{
						synchronized (cci)
						{
							arrivedPing = true;
							cci.notifyAll();
						}
					}
				}

			}

		}
	}

	public void waitPingConfirm()
	{
		synchronized (cci)
		{
			while (!arrivedPing && !disconnection)
			{
				try
				{
					cci.wait();
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
	 * Getter of ClientCommandInterpreter
	 *
	 * @return ClientCommandInterpreter
	 */
	public ClientCommandInterpreter getCCI()
	{
		return cci;
	}

	public void setDisconnection()
	{
		synchronized (cci)
		{
			disconnection = true;
			cci.notifyAll();
		}

	}

	public void setCpt(ClientPingerThread cpt)
	{
		this.cpt = cpt;
	}
}