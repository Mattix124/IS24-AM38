package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static it.polimi.ingsw.am38.Network.Packet.Scope.CONNECTION;

public class ClientPingerThread extends Thread
{
	private final ObjectOutputStream out;
	private final ClientMessageSorter cms;
	private String nick;
	private final CNClient client;
	private boolean connected = true;

	ClientPingerThread(ObjectOutputStream out, ClientMessageSorter cms, CNClient client)
	{
		this.out = out;
		this.cms = cms;
		this.client = client;
	}

	public void setNick(String s)
	{
		this.nick = s;
	}

	@Override
	public void run()
	{
		TimerThread tT;
		while (connected)
		{
			tT = new TimerThread(cms, this);
			tT.setDaemon(true);
			tT.setName("Timer");
			tT.start();
			cms.waitPingConfirm();
			{
				tT.setStillConnected();
				if (connected)
				{
					try
					{
						out.writeObject(new Message(CONNECTION, CONNECTION, nick, null));
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
					client.killer();
				}
			}
		}

	}

	public void setConnected(boolean b)
	{
		connected = b;
	}

}
