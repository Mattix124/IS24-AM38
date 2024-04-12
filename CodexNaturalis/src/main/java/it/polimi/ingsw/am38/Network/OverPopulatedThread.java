package ServerClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class OverPopulatedThread implements Runnable
{
	final private Socket socket;
	public OverPopulatedThread(Socket socket)
	{
		this.socket = socket;
	}
	public void run()
	{
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(socket.getOutputStream());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		out.println("There are already 4 player connected. You will be disconnected in 3 seconds");

		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}

	}
}
