package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.View.SceneController;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Thread that answer the ping of the thread
 */
public class ClientPingerThread extends Thread
{
	/**
	 * CommonClientInterface instance
	 */
	private final CommonClientInterface inter;
	/**
	 * Boolean that check if a client is connected
	 */
	private boolean connected = true;
	/**
	 * Viewable
	 */
	private final Viewable view;

	/**
	 * Constructor method that set CommonClientInterface and Viewable
	 *
	 * @param inter CommonClientInterface instance
	 * @param view Viewable instance
	 */
	ClientPingerThread(CommonClientInterface inter, Viewable view)
	{
		this.inter = inter;
		this.view = view;
	}

	/**
	 * Set the client to disconnected if a ping message doesn't come in time from the server
	 */
	@Override
	public void run()
	{
		TimerThread tT;
		while (connected)
		{
			tT = new TimerThread(inter, this);
			tT.setDaemon(true);
			tT.setName("Timer");
			tT.start();
			try
			{
				inter.waitPingConfirm();
			}
			catch (RemoteException e)
			{
				System.err.println("Disconnected");
			}
			{
				tT.setStillConnected();
				if (connected)
				{
					try
					{
						inter.ping();
					}
					catch (IOException e)
					{
						System.err.println("Disconnected");
					}
				}
				else
				{
					view.priorityString("Disconnection/The server is not responding, you can rejoin typing 'reconnect'. Otherwise, you can type 'exit' to close the app.");

					try
					{
						Thread.sleep(2500);
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}

					try
					{
						inter.killer(); //controllate che funzioni tutto (non sono certo che il cw (per cli) ) funzioni se chiudo tutto tranne CLIENTSTARTER (Main thread)
					}
					catch (RemoteException e)
					{
						System.err.println("Disconnected");
					}

				}
			}
		}
	}

	/**
	 * Setter for the attribute connected
	 *
	 * @param b true or false
	 */
	public void setConnected(boolean b)
	{
		connected = b;
	}

}
