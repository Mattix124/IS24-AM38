package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientPingerThread extends Thread
{
	private final CommonClientInterface inter;
	private boolean connected = true;

	ClientPingerThread(CommonClientInterface inter)
	{
		this.inter = inter;
	}

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
					System.out.println("The server is not responding, you can rejoin typing 'reconnect'. Otherwise, you can type 'exit' to close the app.");
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
						inter.killer(1);
					}
					catch (RemoteException e)
					{
						System.err.println("Disconnected");
					}

				}
			}
		}
	}

	public void setConnected(boolean b)
	{
		connected = b;
	}

}
