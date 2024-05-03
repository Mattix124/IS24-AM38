package it.polimi.ingsw.am38.Network;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.TCP.Server.GameThread;
import it.polimi.ingsw.am38.Network.TCP.Server.SortPlayerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class CNServer
{
	final int port;
	private final LinkedList <Socket> sockets = new LinkedList <>();
	private static LinkedList <GameThread> gameThreadList = null;

	CNServer(int port)
	{
		this.port = port;
		gameThreadList = new LinkedList <>();
	}

	public static void main(String[] args)
	{

		CNServer server = new CNServer(5000);
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

		System.out.println("The server is listening on port: " + port);
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
