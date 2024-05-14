package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

public class ClientMessageSorter extends Thread
{
	private final ClientCommandInterpreter cci;
	private final LinkedList <Message> queue;

	public ClientMessageSorter(ClientCommandInterpreter cci)
	{
		this.cci = cci;
		this.queue = new LinkedList <>();
	}

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
					}
				}
			}
		}
	}

	public void addMessage(Message m)
	{
		synchronized (queue)
		{
			queue.add(m);
			queue.notifyAll();
		}
	}
}