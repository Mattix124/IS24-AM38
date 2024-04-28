package it.polimi.ingsw.am38.Network.Chat;

import it.polimi.ingsw.am38.Controller.LobbyManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerChat
{
	final int port;
	private int counter;
	final private LobbyManager lobbyManager;

	private final ArrayList <Socket> sockets = new ArrayList <>();

	ServerChat(int port)
	{
		this.lobbyManager = new LobbyManager();
		this.port = port;
		counter = 0;
	}

	public static void main(String[] args)
	{
		ServerChat server = new ServerChat(Integer.parseInt(args[0]));
		server.start();
	}

	public void start()
	{
		final ExecutorService executor = Executors.newCachedThreadPool();

		ServerSocket serverSocket = null;

		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		System.out.println("The server is listening on port: " + port);
		while (true)
		{
			try
			{

				Socket socket = serverSocket.accept();
				sockets.add(socket);

				if (counter < 4)
				{
					executor.submit(new ClientHandler(socket));
					counter++;
				}
				else
				{
					executor.submit(new OverPopulatedThread(socket));
				}

				sockets.stream().forEach(s -> System.out.println(s.getPort()));

			}
			catch (IOException e)
			{
				System.err.println(e.getMessage());
			}

			System.out.println("Client N* " + counter + " connected");
		}

	}

}
