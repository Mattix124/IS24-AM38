package it.polimi.ingsw.am38.Network.Server;

public class ServerPingThread extends Thread
{
	private final ServerProtocolInterface inter;
	private final ServerMessageSorter sms;
	private boolean dead;
	private final GameThread gt;

	public ServerPingThread(ServerProtocolInterface inter, ServerMessageSorter sms, GameThread gt)
	{
		this.inter = inter;
		this.sms = sms;
		this.gt = gt;
	}

	public void run()
	{
		TimerPostDisconnection tPD       = new TimerPostDisconnection();
		boolean                connected = true;
		while (!dead)
		{
			try
			{
				inter.ping();
				Thread.sleep(4000);

				if (!sms.isStillConnected(inter.getPlayer().getNickname()))
				{
					if (!connected)
					{
						tPD.start();
					}
					connected = false;
				}
				else
				{
					if (!connected)
						tPD.reconnected = true;
					connected = true;
				}

			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void setDead()
	{
		gt.RemovePlayerData(inter.getPlayer().getNickname(), this);
		dead = true;
	}

	class TimerPostDisconnection extends Thread
	{
		private ServerPingThread spT;
		private boolean reconnected;

		public void run()
		{
			try
			{
				Thread.sleep(20000);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			if (!reconnected)
				setDead();
		}
	}
}
