package it.polimi.ingsw.am38.Network.Client;

import java.rmi.RemoteException;

/**
 * Thread used as a timer to check if the client is still connected
 */
public class TimerThread extends Thread
{
	/**
	 * Instance of the interface of the client
	 */
	private CommonClientInterface inter;
	/**
	 * Instance of the ClientPingerThread
	 */
	private ClientPingerThread cpt;

	/**
	 * Constructor method that set the client interface and the thread that manages the ping
	 *
	 * @param inter the CommonClientInterface
	 * @param cpt the ClientPingerThread
	 */
	public TimerThread(CommonClientInterface inter, ClientPingerThread cpt)
	{
		this.inter = inter;
		this.cpt = cpt;
	}

	/**
	 *
	 */
	public void run()
	{
		try
		{
			Thread.sleep(6000);
		}
		catch (InterruptedException e)
		{
			return;
		}
		cpt.setConnected(false);
		try
		{
			inter.setDisconnection();
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 *
	 */
	public void setStillConnected()
	{
		this.interrupt();
	}

}

