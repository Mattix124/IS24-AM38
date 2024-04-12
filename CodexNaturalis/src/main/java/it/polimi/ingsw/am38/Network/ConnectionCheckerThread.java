package it.polimi.ingsw.am38.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionCheckerThread implements Runnable
{
	final private ArrayList <Socket> sockets;
	final private LinkedList <Socket> trashSocket = new LinkedList <>();
	private int counter;

	ConnectionCheckerThread(ArrayList <Socket> sockets, int counter)
	{
		this.sockets = sockets;
	}

	@Override
	public void run()
	{
		Scanner     in;
		PrintWriter out;
		while (true)
		{
			for (Socket s : sockets)
			{
				try
				{
					in = new Scanner(s.getInputStream());
					out = new PrintWriter(s.getOutputStream());
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
				out.println("Ping");
				out.flush();
				try
				{
					in.nextLine();
					System.out.println(in);
				}
				catch (IllegalStateException e)
				{
					trashSocket.add(s);
				}

			}

			for (Socket ts : trashSocket)
			{
				sockets.remove(ts);
			}
			try
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
