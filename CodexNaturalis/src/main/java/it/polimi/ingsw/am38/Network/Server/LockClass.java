package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;

import java.io.IOException;

/**
 * Class used to synchronise all the clients
 */
public class LockClass
{
	/**
	 *
	 */
	private boolean check = false;
	/**
	 * Object used to perform wait
	 */
	private final Object lock = new Object();
	/**
	 * Instance of GameController
	 */
	private GameController gc;
	/**
	 * Int to check ho many clients has performed an action requested
	 */
	private int passed;
	/**
	 * Number of player that has to do an action in order for the game to continue
	 */
	private int max;

	/**
	 * Constructor method
	 *
	 * @param gc is the GameController of the game
	 * @param max number of player that has to perform an action before the game could go on
	 */
	public LockClass(GameController gc, int max)
	{
		this.gc = gc;
		this.passed = 0;
		this.max = max;
	}

	/**
	 * Method that let the game go on only if all the players have performed a certain action
	 * @param inter interface of the server
	 * @throws IOException
	 */
	public void waitForPlayers(ServerProtocolInterface inter) throws IOException
	{
		synchronized (lock)
		{
			passed++;
			if (passed == max)
			{
				check = true;
				lock.notifyAll();
				passed--;
				return;
			}
			while (!check)
			{
				try
				{
					inter.waitTextPlayers();
					lock.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
			passed--;
			if (passed == 0)
				check = false;
		}
	}
}
