package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * ClientListener is a class that binds every tcp client to the TCPServer
 */
public class ClientListener implements Runnable
{
	/**
	 * Attributes that contains the input of the client
	 */
	final private ObjectInputStream clIn;
	/**
	 * Attributes that contains the ServerMessageSorter
	 */
	final private ServerMessageSorter msgIntSer;

	/**
	 * Constructor of ClientListener
	 *
	 * @param clIn the input of the client
	 * @param msgIntSer the ServerMessageSorter
	 */
	public ClientListener(ObjectInputStream clIn, ServerMessageSorter msgIntSer)
	{
		this.clIn = clIn;
		this.msgIntSer = msgIntSer;

	}

	/**
	 * The listening method
	 */
	@Override
	public void run()
	{
		Message message;
		while (true)
		{
			try
			{
				message = (Message) clIn.readObject();
			}
			catch (IOException | ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
			msgIntSer.addMessage(message);
		}
	}
}

