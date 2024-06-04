package it.polimi.ingsw.am38.Network.Client;

import java.rmi.RemoteException;

public class TimerThread extends Thread
{
	private CommonClientInterface inter;
	private ClientPingerThread cpt;

	public TimerThread(CommonClientInterface inter, ClientPingerThread cpt)
	{
		this.inter = inter;
		this.cpt = cpt;
	}

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

	public void setStillConnected()
	{
		this.interrupt();
	}

}

