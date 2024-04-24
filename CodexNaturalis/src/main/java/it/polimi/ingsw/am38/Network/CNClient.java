package it.polimi.ingsw.am38.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
		//PrintWriter pingAnswerer;

		socket = new Socket(ip, port);
		sIn = new Scanner(socket.getInputStream());
		//pingAnswerer = new PrintWriter(socket.getOutputStream());

		received = sIn.nextLine();
		if (received.equals("OK"))
		{
			Thread clientWriter = new Thread(new ClientTransmitter(socket));
			clientWriter.start();
			System.out.println("Connection established!");
		}

		received = sIn.nextLine();
		while (!received.equals("KO"))
		{
			/*if(received.equals("Ping"))
			{
				pingAnswerer.println("Pong");
				pingAnswerer.flush();
				received = sIn.nextLine();
			}
*/

			System.out.println(received);
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

}

