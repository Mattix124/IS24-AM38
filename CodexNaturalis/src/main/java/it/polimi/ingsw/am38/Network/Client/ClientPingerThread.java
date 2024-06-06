package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import static it.polimi.ingsw.am38.Network.Packet.Scope.CONNECTION;

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
				throw new RuntimeException(e);
			}
			{
				tT.setStillConnected();
				if (connected)
				{
					try
					{
						inter.ping();
						//out.writeObject(new Message(CONNECTION, CONNECTION, nick, null));
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					System.out.println("The server is not responding, the app will be closed.\nIf the problem is client-side you can rejoin the previous game");
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
						inter.killer();
					}
					catch (RemoteException e)
					{
						throw new RuntimeException(e);
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
