package it.polimi.ingsw.am38.Network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerTCP extends Thread
{
	final int port;
	private final LinkedList <Socket> sockets = new LinkedList <>();
	private static LinkedList <GameThread> gameThreadList;

	public ServerTCP(int port,LinkedList<GameThread> gameThreadList)
	{
		this.port = port;
		ServerTCP.gameThreadList = gameThreadList;
	}


	public void run()
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

	public static LinkedList <GameThread> getGameThreadList()
	{
		return gameThreadList;
	}
}
