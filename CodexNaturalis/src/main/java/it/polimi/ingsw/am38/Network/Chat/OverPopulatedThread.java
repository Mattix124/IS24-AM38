package it.polimi.ingsw.am38.Network.Chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class OverPopulatedThread implements Runnable
{
	final private Socket socket;
	final private PrintWriter out;

	public OverPopulatedThread(Socket socket)
	{

		this.socket = socket;
		try
		{
			this.out = new PrintWriter(socket.getOutputStream());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	public void run()
	{
		out.println("O");
		out.flush();
		out.println("There are already 4 player connected. You will be disconnected in 3 seconds");
		for (int i = 3 ; i > 0 ; i--)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			out.println(i + "...");
			out.flush();
		}
		out.println("KO");
		out.flush();
		try
		{
			out.close();
			socket.close();
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}
}
