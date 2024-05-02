package it.polimi.ingsw.am38.Network;

import it.polimi.ingsw.am38.Network.Chat.ClientChatTransmitter;

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

	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;
	}

	public void start() throws IOException, InterruptedException
	{
		Socket  socket;
		String  received;
		Scanner sIn;

		socket = new Socket(ip, port);
		sIn = new Scanner(socket.getInputStream());
		Thread clientWriter = new Thread(new ClientChatTransmitter(socket));
		clientWriter.start();
		//Thread clLab = new Thread();
		//clLab.start();

		System.out.println("Connection established!");

		received = sIn.nextLine();
		while (!received.equals("Kill"))
		{
			try
			{
				received = sIn.nextLine();
			}
			catch (NoSuchElementException e)
			{
				break;
			}

		}

	}

	public static void main(String[] args) throws InterruptedException
	{
		CNClient client = new CNClient(args[1], Integer.parseInt(args[0]));
		try
		{
			client.start();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

	}

	public static class MessageInterpreterServer implements Runnable
	{
		private final Scanner input;
		private final PrintWriter output;
		private final LinkedList <String> queue;

		public MessageInterpreterServer(Scanner input, PrintWriter output)
		{
			this.input = input;
			this.output = output;
			this.queue = new LinkedList <>();
		}

		public synchronized void messageIn(String message)
		{

			//json parsing

		}

		public void toClient(String message) //json
		{

		}
		@Override
		public void run()
		{
			while (true)
			{


				//switch
			}

		}

	}
}

