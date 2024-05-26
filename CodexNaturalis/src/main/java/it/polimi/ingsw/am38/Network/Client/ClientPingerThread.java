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
	private CNClient client;
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
		while (connected)
		{
			if (cms.getPingConfirm())

			{
				//inter.sendPingBack
				try
				{
					out.writeObject(new Message(CONNECTION, CONNECTION, nick, null));
					Thread.sleep(4000);
				}
				catch (IOException | InterruptedException e)
				{
					throw new RuntimeException(e);
				}

			}
			else
			{
				connected = false;
			}
		}
		System.out.println("The server is not responding, the app will be closed.\nIf the problem is client-side you can rejoin the previous game");
		try
		{
			Thread.sleep(3500);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		client.killer();
	}
}
