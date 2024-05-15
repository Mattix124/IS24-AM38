package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;

/**
 * ServerMessageSorter is a queue that allows the player to send message (if they are allowed) and then the server can use this message for a specific task
 */
public class ServerMessageSorter extends Thread
{
	/**
	 * Attribute that contains the main queue
	 */
	private final LinkedList <Message> queue;
	/**
	 * Attribute that contains the chatQueue
	 */
	private final LinkedList <Message> chatQueue;
	/**
	 * Attribute that contains the gameQueue
	 */
	private final GameList gameQueue;
	/**
	 * Attributes that contains the view queue
	 */
	private final LinkedList <Message> viewQueue;

	/**
	 * Constructor of ServerMessageSorter
	 */
	public ServerMessageSorter()
	{
		this.chatQueue = new LinkedList <>();
		this.gameQueue = new GameList();
		this.viewQueue = new LinkedList <>();
		queue = new LinkedList <>();

	}

	/**
	 * The always ready method to separate the various message incoming
	 */
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

	/**
	 * Method used to add the message on the general Queue
	 *
	 * @param message the message
	 */
	public void addMessage(Message message)
	{
		synchronized (queue)
		{
			queue.add(message);
			queue.notifyAll();
		}

	}

	/**
	 * Get the first chat message, no control needed
	 *
	 * @return the first chat message
	 */
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

	/**
	 * Getter for GameMessage based on the nickname needed
	 *
	 * @param nickName nickname needed
	 * @return the message of the corresponding player
	 */
	public Message getGameMessage(String nickName)
	{
		Message m;
		synchronized (gameQueue)
		{
			while (gameQueue.isEmpty() || !messageFromNick(nickName))
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

	private boolean messageFromNick(String nickName)
	{
		if (gameQueue.contains(nickName))
		{
			gameQueue.notifyAll();
			return true;
		}
		return false;

	}
}