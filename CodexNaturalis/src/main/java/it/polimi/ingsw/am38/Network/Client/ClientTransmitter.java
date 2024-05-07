package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTransmitter implements Runnable
{
	final private Socket socket;
	private PrintWriter out;
	private Scanner in;

	public ClientTransmitter(Socket socket)
	{
		this.socket = socket;
		try
		{
			this.out = new PrintWriter(socket.getOutputStream());
			this.in = new Scanner(System.in);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void run()
	{
		String message;
		while (true)
		{
			message = in.nextLine();
			out.println(message);
			out.flush();
		}
	}
}
