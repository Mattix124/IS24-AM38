package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Exception.DisconnectedException;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.HashMap;
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
	private final ContainsList gameQueue;
	/**
	 * Attributes that contains the view queue
	 */
	private final ContainsList connectionQueue;
	/**
	 * HashMap that indicates if a player is still connected
	 */
	private final HashMap <String, Boolean> playersList;
	/**
	 * GameThread correlated to this ServerMessageSorter
	 */
	private final GameThread gt;

	/**
	 * Constructor of ServerMessageSorter
	 */
	public ServerMessageSorter(GameThread gt)
	{
		this.chatQueue = new LinkedList <>();
		this.gameQueue = new ContainsList();
		this.connectionQueue = new ContainsList();
		queue = new LinkedList <>();
		this.playersList = new HashMap <>();
		this.gt = gt;
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
					catch (InterruptedException ignored)
					{

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
				case CONNECTION:
				{
					synchronized (connectionQueue)
					{
						connectionQueue.add(message);
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
	public Message getGameMessage(String nickName) throws DisconnectedException, InterruptedException
	{
		Message m;
		synchronized (gameQueue)
		{
			while ((gameQueue.isEmpty() || !gameMessageFromNick(nickName)) && isConnected(nickName))
			{
				try
				{
					gameQueue.wait();
				}
				catch (InterruptedException e)
				{
					throw new InterruptedException();
				}
			}
			if (!isConnected(nickName))
				throw new DisconnectedException("Disconnected");
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

	/**
	 * Takes from the queue a message from a specific player
	 *
	 * @param nickName of the player
	 * @return
	 */
	private boolean gameMessageFromNick(String nickName)
	{
		if (gameQueue.contains(nickName))
		{
			gameQueue.notifyAll();
			return true;
		}
		return false;

	}

	/**
	 * Getter for the ping received from a player
	 *
	 * @param nickName of the player who has sent the ping
	 * @return true if the player has actually sent a ping, false otherwise
	 */
	public boolean getPingMessage(String nickName)
	{
		synchronized (connectionQueue)
		{
			if (connectionQueue.contains(nickName))
			{
				connectionQueue.retrieve(nickName);
				return true;
			}
			return false;
		}
	}

	/**
	 * Setter for a player connection
	 *
	 * @param nickname of the player
	 * @param c true if the player is connected, false otherwise
	 */
	public void setPlayerConnection(String nickname, boolean c)
	{
		synchronized (gameQueue)
		{
			playersList.put(nickname, c);
			gt.changeGameThreadConnectionCount(c);
			gameQueue.notifyAll();
		}
	}

	/**
	 * Method to know ifa a player is connected
	 *
	 * @param nickname
	 * @return
	 */
	public boolean isConnected(String nickname)
	{
		return playersList.get(nickname);
	}

	/**
	 * Method to add a player between the connected
	 *
	 * @param nickname
	 */
	public void addPlayer(String nickname)
	{
		playersList.put(nickname, true);
		gt.changeGameThreadConnectionCount(true);
	}

	/**
	 * Class created to simplify syntax in ServerMessageSorter
	 */
	class ContainsList extends LinkedList <Message>
	{
		/**
		 * Contains is a method that accept a string (player's nickname) to find his message
		 *
		 * @param nickname player's nickname
		 * @return true if found, false if not
		 */
		public boolean contains(String nickname)
		{
			for (Message m : this)
				if (m.getSender().equals(nickname))
					return true;
			return false;
		}

		/**
		 * Take out the message from the queue
		 *
		 * @param nickname
		 */
		public void retrieve(String nickname)
		{
			Message found = null;
			for (Message m : this)
				if (m.getSender().equals(nickname))
					found = m;
			this.remove(found);

		}
	}

}