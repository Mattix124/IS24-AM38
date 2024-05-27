package it.polimi.ingsw.am38.Network.Server;

public class ServerPingThread extends Thread
{
	private final ServerProtocolInterface inter;
	private final ServerMessageSorter sms;
	private boolean dead = false;
	private final GameThread gt;

	public ServerPingThread(ServerProtocolInterface inter, ServerMessageSorter sms, GameThread gt)
	{
		this.inter = inter;
		this.sms = sms;
		this.gt = gt;
	}

	public void run()
	{
		TimerPostDisconnection tPD = new TimerPostDisconnection();
		tPD.setPriority(6);
		boolean connected = true;
		inter.ping(true);
		while (!dead)
		{
			try
			{
				inter.ping(false);
				Thread.sleep(4000);

				if (!sms.isStillConnected(inter.getPlayer().getNickname()))
				{
					if (connected)
					{
						System.out.println("missed ping from " + inter.getPlayer().getNickname());
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
		dead = true;
		gt.RemovePlayerData(inter.getPlayer().getNickname(), this);

	}

	class TimerPostDisconnection extends Thread
	{
		private boolean reconnected = false;

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
