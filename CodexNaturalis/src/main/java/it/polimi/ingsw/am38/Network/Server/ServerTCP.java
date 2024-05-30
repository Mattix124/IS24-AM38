package it.polimi.ingsw.am38.Network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * ServerTCP Class
 */
public class ServerTCP extends Thread
{
	/**
	 * Port at which every tcp client will be connected
	 */
	final int port;

	/**
	 * Constructor of TCP server
	 * @param port
	 */
	public ServerTCP(int port)
	{
		this.port = port;
	}

	/**
	 * Listener method
	 */
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
		while(true)
		{
			clSocket = null;
			try
			{
				clSocket = serverSocket.accept();
			}
			catch (IOException e)
			{
				System.err.println(e.getMessage());
			}

			Thread playerSorter = new Thread(new SortPlayerThread(clSocket));
			playerSorter.start();
		}
	}
}