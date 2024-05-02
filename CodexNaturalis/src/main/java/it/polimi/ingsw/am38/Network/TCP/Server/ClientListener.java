package it.polimi.ingsw.am38.Network.TCP.Server;

import java.net.Socket;
import java.util.Scanner;

public class ClientListener implements Runnable
{
	final private Socket clSocket;
	final private Scanner clIn;

	final private MessageInterpreterServer msgIntSer;
	final private Object lock;

	public ClientListener(Socket clSocket, Scanner clIn, MessageInterpreterServer msgIntSer)
	{
		this.clSocket = clSocket;
		this.clIn = clIn;
		this.msgIntSer = msgIntSer;
		msgIntSer.start();
		this.lock = msgIntSer.getLock();

	}

	@Override
	public void run()
	{
		String message;
		while (true)
		{
			message = clIn.nextLine();
			msgIntSer.addMessage(message);
			lock.notifyAll();
		}
	}

}
