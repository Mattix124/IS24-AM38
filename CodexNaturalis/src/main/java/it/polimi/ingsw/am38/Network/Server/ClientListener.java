package it.polimi.ingsw.am38.Network.Server;

import java.net.Socket;
import java.util.Scanner;

public class ClientListener implements Runnable
{
	final private Socket clSocket;
	final private Scanner clIn;

	final private MessageInterpreterServer msgIntSer;


	public ClientListener(Socket clSocket, Scanner clIn, MessageInterpreterServer msgIntSer)
	{
		this.clSocket = clSocket;
		this.clIn = clIn;
		this.msgIntSer = msgIntSer;

	}

	@Override
	public void run()
	{
		String message;
		while (true)
		{
				message = clIn.nextLine();
				msgIntSer.addMessage(message);
		}
	}

}
