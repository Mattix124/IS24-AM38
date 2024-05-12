package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriter implements Runnable
{
	private ClientCommandInterpreter clientCommandInterpreter;
	private Scanner in;

	public ClientWriter(Socket socket)
	{
		try
		{
			this.clientCommandInterpreter = new ClientCommandInterpreter(new ObjectOutputStream(socket.getOutputStream()));
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
			try
			{
				clientCommandInterpreter.checkCommand(message);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
