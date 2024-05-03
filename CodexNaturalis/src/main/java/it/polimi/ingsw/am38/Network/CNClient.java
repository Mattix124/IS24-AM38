package it.polimi.ingsw.am38.Network;

import it.polimi.ingsw.am38.Network.Chat.ClientChatTransmitter;
import it.polimi.ingsw.am38.Network.TCP.Client.MessageInterpreterClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CNClient
{
	private final String ip;
	private final int port;
	private final Object lock;
	private MessageInterpreterClient msgInter;

	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;
		this.lock = new Object();

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
		this.msgInter = new MessageInterpreterClient(lock, cOut);
		msgInter.start();
		Thread clientWriter = new Thread(new ClientChatTransmitter(socket));
		clientWriter.start();

		System.out.println("Connection established!");

		received = sIn.nextLine();
		while (!received.equals("ends"))
		{

			try
			{

				System.out.println(received);
				cOut.println(sIn.nextLine());
				received = sIn.nextLine();
			}
			catch (NoSuchElementException e)
			{
				break;
			}

		}
		while (!received.equals("kill"))
		{
			synchronized (lock)
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
				lock.notifyAll();
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

