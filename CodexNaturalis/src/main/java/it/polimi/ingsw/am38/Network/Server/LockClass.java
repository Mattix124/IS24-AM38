package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;

import java.io.IOException;

public class LockClass
{
	private boolean check = false;
	private final Object lock = new Object();
	private GameController gc;
	private int passed;
	private int max;

	public LockClass(GameController gc, int max)
	{
		this.gc = gc;
		this.passed = 0;
		this.max = max;
	}

	public void waitForPlayers(ServerProtocolInterface inter) throws IOException
	{
		synchronized (lock)
		{
			passed++;
			if (passed == max)
			{
				check = true;
				lock.notifyAll();
				passed=0;
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
		}
	}
}
