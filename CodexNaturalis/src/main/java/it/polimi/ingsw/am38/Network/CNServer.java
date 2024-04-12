package ServerClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CNServer
{
	final int port;
	int counter;

	CNServer(int port)
	{
		this.port = port;
		counter = 0;
	}

	public static void main(String[] args)
	{
		CNServer server = new CNServer(Integer.parseInt(args[1]));
		server.start();
	}

	public void start()
	{
		final ExecutorService executor     = Executors.newFixedThreadPool(5);

		ServerSocket          serverSocket = null;

		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		System.out.println("The server is listening on port: " + port);
		while (true)
		{
			try
			{
				final Socket socket = serverSocket.accept();
				counter++;
				if (counter < 5)
				{
					executor.submit(new ClientThread(socket));
				}
				else
				{
					executor.submit(new OverPopulatedThread(socket));
				}
			}
			catch (IOException e)
			{
				System.err.println(e.getMessage());
			}

			System.out.println("Client N" + counter + "connected");
		}

		//CHAT DA ESEMPIO---------------------------------


	}


}
