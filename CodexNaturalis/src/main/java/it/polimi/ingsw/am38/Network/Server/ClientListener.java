package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientListener implements Runnable
{
	final private Socket clSocket;
	final private ObjectInputStream clIn;

	final private MessageInterpreterServer msgIntSer;

	public ClientListener(Socket clSocket, ObjectInputStream clIn, MessageInterpreterServer msgIntSer)
	{
		this.clSocket = clSocket;
		this.clIn = clIn;
		this.msgIntSer = msgIntSer;

	}

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

