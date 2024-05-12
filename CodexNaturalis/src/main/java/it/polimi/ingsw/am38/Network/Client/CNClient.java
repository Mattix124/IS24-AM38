package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.KILL;

public class CNClient
{
	private final String ip;
	private final int port;

	private ClientMessageSorter msgInter;

	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;

	}

	public void start()
	{
		Socket            socket;
		String            received;
		Message           receivedMessage;
		Scanner           sIn;
		ObjectInputStream objectIn;

		try
		{
			socket = new Socket(ip, port);
			sIn = new Scanner(socket.getInputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			this.msgInter = new ClientMessageSorter();
			msgInter.start();
			Thread clientWriter = new Thread(new ClientWriter(socket));
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
		sIn.close();
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

	public static void main(String[] args) throws InterruptedException
	{
		CNClient client = new CNClient("localhost", 5000);
		try
		{
			client.start();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

	}

}

