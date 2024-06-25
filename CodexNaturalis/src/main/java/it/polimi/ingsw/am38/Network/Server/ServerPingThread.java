package it.polimi.ingsw.am38.Network.Server;

/**
 * Thread that starts the ping interaction and remove the connection of the client if it doesn't
 * get a ping response
 */
public class ServerPingThread extends Thread
{
	/**
	 * Instance of the interface of the server
	 */
	private final ServerProtocolInterface inter;
	/**
	 * Instance of the message sorter of the game
	 */
	private final ServerMessageSorter sms;
	/**
	 * Boolean to check if a client is still connected
	 */
	private boolean connected = true;
	/**
	 * Instance of the GameThread of the game
	 */
	private final GameThread gt;

	/**
	 * Constructor method that set the attribute needed
	 *
	 * @param inter the interface of the server
	 * @param sms the message sorter
	 * @param gt the game thread
	 */
	public ServerPingThread(ServerProtocolInterface inter, ServerMessageSorter sms, GameThread gt)
	{
		this.inter = inter;
		this.sms = sms;
		this.gt = gt;
	}

	/**
	 * The run method send a ping to the client and waits for a response, if it does get one then proceed
	 * to check the connection, if it does not get a ping back then it shut down the connection
	 */
	public void run()
	{
		inter.ping(true);
		while (connected)
		{
			try
			{
				inter.ping(false);
				Thread.sleep(4000);

				if (!sms.getPingMessage(inter.getPlayer().getNickname()))
				{
					sms.setPlayerConnection(inter.getPlayer().getNickname(), false);
					inter.getPlayer().setIsPlaying(false);
					connected = false;
					removeInterface();
				}
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Method to remove the connection from the game
	 */
	public void removeInterface()
	{
		connected = false;
		gt.RemovePlayerData(inter);
	}
}
