package it.polimi.ingsw.am38.Network.TCP;

import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Game;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientListener implements Runnable
{
	final private Socket clSocket;
	final private Scanner clIn;
	final private PrintWriter clOut;

	final private MessageInterpreter msgInt;

	public ClientListener(Socket clSocket, Scanner clIn, PrintWriter clOut, MessageInterpreter msgInt)
	{
		this.clSocket = clSocket;
		this.clIn = clIn;
		this.clOut = clOut;
		this.msgInt = msgInt;
	}

	@Override
	public void run()
	{
		String message;
		while (true)
		{
			try
			{
				synchronized (msgInt)
				{
					message = clIn.nextLine();
				}
			}
			catch (NoSuchElementException e)
			{
				break;
			}
			msgInt.messageIn(message);

		}
	}
}
