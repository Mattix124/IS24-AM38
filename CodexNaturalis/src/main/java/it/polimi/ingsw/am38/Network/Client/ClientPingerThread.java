package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.*;

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
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		TimerThread     tT              = new TimerThread(cms);
		while (connected)
		{
			Future <Boolean> b = executorService.submit((Callable <Boolean>) tT);
			cms.waitPingConfirm();
			{
				tT.setStillConnected();
				try
				{
					b.get();
				}
				catch (InterruptedException | ExecutionException e)
				{
					throw new RuntimeException(e);
				}
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

	class TimerThread extends Thread implements Callable <Boolean>
	{
		private ClientMessageSorter cms;
		private boolean stillConnected;

		TimerThread(ClientMessageSorter cms)
		{
			this.cms = cms;
		}

		public void run()
		{
			stillConnected = false;
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				return;
			}
			if (!stillConnected)
			{
				cms.setDisconnection();
				connected = false;
			}
		}

		public void setStillConnected()
		{
			stillConnected = true;
		}

		@Override
		public Boolean call() throws Exception
		{
			return null;
		}
	}
}
