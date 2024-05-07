package it.polimi.ingsw.am38.Network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerTCP
{
	final int port;
	private final LinkedList <Socket> sockets = new LinkedList <>();
	private static LinkedList <GameThread> gameThreadList = null;

	public ServerTCP(int port)
	{
		this.port = port;
		gameThreadList = new LinkedList <>();
	}

	public static void main(String[] args)
	{

		ServerTCP server = new ServerTCP(5000);
		//CNServer server = new CNServer(Integer.parseInt(args[0]));
		server.start();
	}

	public void start()
	{
		ServerSocket serverSocket;
		Socket       clSocket;

		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		System.out.println("Server TCP ready");
		while (true)
		{
			clSocket = null;
			do
			{
				try
				{
					clSocket = serverSocket.accept();
					sockets.add(clSocket);

				}
				catch (IOException e)
				{
					System.err.println(e.getMessage());
				}
			} while (clSocket == null);

			Thread playerSorter = new Thread(new SortPlayerThread(clSocket));
			playerSorter.start();
		}
	}

	public static void addGameThread(GameThread gameThread)
	{
		gameThreadList.add(gameThread);
	}

	public static LinkedList <GameThread> getGameThreadList()
	{
		return gameThreadList;
	}
}
