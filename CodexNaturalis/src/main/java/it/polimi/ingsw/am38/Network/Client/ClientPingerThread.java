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
		while (connected)
		{

			cms.waitPingConfirm();
			{
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

	class timerThread extends Thread
	{
		private ClientMessageSorter cms;
		private boolean stillConnected;

		timerThread(ClientMessageSorter cms)
		{
			this.cms = cms;
			this.stillConnected = false;
		}

		public void run()
		{
			try
			{
				Thread.sleep(3500);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			if (!stillConnected)
			{
				cms.setDisconnection();
				connected = false;
			}

		}
	}
}
