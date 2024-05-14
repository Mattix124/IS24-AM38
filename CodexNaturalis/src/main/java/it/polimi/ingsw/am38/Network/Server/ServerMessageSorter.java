package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

public class ServerMessageSorter extends Thread
{

	private final LinkedList <Message> queue;
	private final LinkedList <Message> chatQueue;
	private final GameList  gameQueue;
	private final LinkedList <Message> viewQueue;

	public ServerMessageSorter()
	{
		this.chatQueue = new LinkedList <>();
		this.gameQueue = new GameList();
		this.viewQueue = new LinkedList <>();
		queue = new LinkedList <>();

	}

	@Override
	public void run()
	{
		Message message;
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
			switch (message.getHeader1())
			{
				case CHAT:
				{
					synchronized (chatQueue)
					{
						chatQueue.add(message);
						chatQueue.notifyAll();
					}
					break;
				}
				case GAME:
				{
					synchronized (gameQueue)
					{
						gameQueue.add(message);
						gameQueue.notifyAll();
					}
					break;
				}
				case VIEWUPDATE:
				{
					synchronized (viewQueue)
					{
						viewQueue.add(message);
						viewQueue.notifyAll();
					}
				}
			}
		}
	}

	public void addMessage(Message message)
	{
		synchronized (queue)
		{
			queue.add(message);
			queue.notifyAll();
		}

	}

	public Message getChatMessage()
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

	public Message getGameMessage(String nickName)
	{
		Message m;
		synchronized (gameQueue)
		{
			while (gameQueue.isEmpty() || !gameQueue.contains(nickName) )
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
			for (Message message : gameQueue)
				if (message.getSender().equals(nickName))
				{
					m = message;
					gameQueue.remove(message);
					return m;
				}
			return null;
		}
	}
}