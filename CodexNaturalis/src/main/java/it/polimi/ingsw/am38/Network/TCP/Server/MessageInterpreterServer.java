package it.polimi.ingsw.am38.Network.TCP.Server;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class MessageInterpreterServer extends Thread
{

	private final LinkedList <String> queue;
	private final LinkedList <String> chatQueue;
	private final LinkedList <String> gameQueue;

	public MessageInterpreterServer()
	{
		this.chatQueue = new LinkedList <>();
		this.gameQueue = new LinkedList <>();
		queue = new LinkedList <>();

	}

	@Override
	public void run()
	{
		String message;
		while (true)
		{
			synchronized (queue)
			{
				while (queue.isEmpty())
				{
					try
					{
						queue.wait();
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
				}
				message = queue.removeFirst();
			}
			if (message.regionMatches(0, "/chat/", 0, 5)) // the message will be chat/mode/sender/receiver/message (4/1/x/x/x)
			{
				synchronized (chatQueue)
				{
					chatQueue.add(message);
					chatQueue.notifyAll();
				}

			}
			else if (message.regionMatches(0, "game/", 0, 6))
			{
				synchronized (gameQueue)
				{
					gameQueue.add(message);
					gameQueue.notifyAll();
				}
			}
		}
	}

	public void addMessage(String message)
	{
		synchronized (queue)
		{
			queue.add(message);
			queue.notifyAll();
		}

	}

	public String getChatMessage()
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
			return chatQueue.removeFirst();
		}
	}

	public String getGameMessage()
	{
		synchronized (gameQueue)
		{
			while (gameQueue.isEmpty())
			{
				try
				{
					gameQueue.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
			return gameQueue.removeFirst();
		}
	}
}