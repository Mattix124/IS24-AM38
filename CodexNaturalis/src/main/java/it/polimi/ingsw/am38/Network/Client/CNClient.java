package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.KILL;

public class CNClient extends Thread
{
	private final String ip;
	private final int port;

	private ClientMessageSorter msgInter;

	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;

	}

	public void run()
	{
		Socket            socket;
		String            received;
		Message           receivedMessage;
		Scanner           sIn;
		ObjectInputStream objectIn;
		ClientWriter      cw;

		try
		{
			socket = new Socket(ip, port);
			sIn = new Scanner(socket.getInputStream());
			ObjectOutputStream sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			ClientCommandInterpreter cci = new ClientCommandInterpreter(sOut);
			this.msgInter = new ClientMessageSorter(cci);
			msgInter.start();
			cw = new ClientWriter(socket,cci);
			Thread clientWriter = new Thread(cw);
			clientWriter.start();
			System.out.println("Connection established!");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		received = sIn.nextLine();
		while (!received.equals("ends")) //login phase
		{
			try
			{
				System.out.println(received);
				received = sIn.nextLine();
			}
			catch (NoSuchElementException e)
			{
				break;
			}
		}
		cw.setPhaseClientWriter(false);
		try
		{
			receivedMessage = (Message) objectIn.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		while (!receivedMessage.getHeader1().equals(KILL)) //game
		{
			try
			{
				msgInter.addMessage(receivedMessage);
				receivedMessage = (Message) objectIn.readObject();
			}
			catch (ClassNotFoundException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}

	}

	public void starte()
	{
		CNClient client = new CNClient("localhost", 5000);
		try
		{
			client.run();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

	}

}

