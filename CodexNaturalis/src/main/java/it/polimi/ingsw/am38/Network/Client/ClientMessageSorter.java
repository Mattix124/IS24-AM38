package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Packet.Scope.BCHAT;

public class ClientMessageSorter extends Thread
{

	private final LinkedList <Message> queue;

	public ClientMessageSorter()
	{
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

					if (message.getHeader2() == BCHAT)
					{
						MSimpleString payload = (MSimpleString) message.getContent();
						System.out.println(message.getSender() + ": " + payload.getText());
					}
					else
					{
						MPrivateChat payload = (MPrivateChat) message.getContent();
						System.out.println(message.getSender() + " said to you: " + payload.getMessage());

					}

				}
				case GAME ->
				{

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