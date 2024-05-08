package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CNClient
{
	private final String ip;
	private final int port;

	private MessageInterpreterClient msgInter;

	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;

	}

	public void start() throws IOException, InterruptedException
	{
		Socket      socket;
		String      received;
		Scanner     sIn;
		PrintWriter cOut;

		socket = new Socket(ip, port);
		sIn = new Scanner(socket.getInputStream());
		cOut = new PrintWriter(socket.getOutputStream());
		this.msgInter = new MessageInterpreterClient();
		msgInter.start();
		Thread clientWriter = new Thread(new ClientTransmitter(socket));
		clientWriter.start();

		System.out.println("Connection established!");

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
		while (!received.equals("kill")) //game
		{

				try
				{
					received = sIn.nextLine();
				}
				catch (NoSuchElementException e)
				{
					break;
				}
				msgInter.addMessage(received);

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

