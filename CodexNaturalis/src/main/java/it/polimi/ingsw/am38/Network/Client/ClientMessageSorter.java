package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayersData;
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
					System.out.println(((MSimpleString) message.getContent()).getText());

				}
				case GAME ->
				{
					MSimpleString content = (MSimpleString) message.getContent();
					System.out.println(content.getText());
					switch (message.getHeader2())
					{
						case PLAYCARD -> cci.setTurning(PLAYPHASE);

						case DRAWCARD -> cci.setTurning(DRAWPHASE);

						case STARTINGFACECHOICE -> cci.setTurning(CHOOSE1);

						case COLORCHOICE -> cci.setTurning(CHOOSE2);

						case OBJECTIVECHOICE -> cci.setTurning(CHOOSE3);

						case EXCEPTION -> cci.setTurning(NOCTURN);

						case INFOMESSAGE -> cci.setTurning(STANDBY);

						case WINNER -> cci.setTurning(NOCTURN);
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

						case PLAYERDATA -> {
							cci.getClientData().setNickname(((MPlayersData) message.getContent()).getNickName());
							cci.getClientData().setPlayersNicknames(((MPlayersData) message.getContent()).getPlayersNicknames());
						}
					}
				}
			}
		}
	}

	/**
	 * Message added by the TCP Client
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
	 * @return ClientCommandInterpreter
	 */
	public ClientCommandInterpreter getCCI(){
		return cci;
	}
}