package it.polimi.ingsw.am38.Network;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientReceiver implements Runnable
{
	final private Socket socket;
	private Scanner in;

	ClientReceiver(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		try
		{
			this.in = new Scanner(socket.getInputStream());
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		String received;

		while (true)
		{
			try
			{
				received = in.nextLine();
			}
			catch (NoSuchElementException e)
			{
				break;
			}
			if (received != null) System.out.println(received);

		}
		in.close();
	}
}
