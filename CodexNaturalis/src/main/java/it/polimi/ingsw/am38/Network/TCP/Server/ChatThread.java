package it.polimi.ingsw.am38.Network.TCP.Server;

import it.polimi.ingsw.am38.Model.Player;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class ChatThread extends Thread
{
	private final LinkedList <String> chatQueue;
	private final HashMap <Player, PrintWriter> communicationMap;

	public ChatThread(LinkedList <String> chatQueue, HashMap <Player, PrintWriter> communicationMap)
	{
		this.chatQueue = chatQueue;
		this.communicationMap = communicationMap;
	}

	public void run()
	{
		String message;
		while (true)
		{
			synchronized (chatQueue)
			{

				while (chatQueue.isEmpty())
				{
					try
					{
						chatQueue.wait();
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
				}

				message = chatQueue.removeFirst();
				if (message.regionMatches(0, "b", 0, 1))
				{
					for (Player p : communicationMap.keySet())
					{
						if (!message.regionMatches(2, p.getNickname(), 0, p.getNickname().length()))
						{
							communicationMap.get(p).println(message);
						}
					}
				}
				else
				{
					for (Player p : communicationMap.keySet())
					{
						if (message.regionMatches(2, p.getNickname(), 0, p.getNickname().length()))
						{
							communicationMap.get(p).println(message);
						}
					}
				}
			}
		}
	}
}
