package it.polimi.ingsw.am38.Network.Server;

public class ServerPingThread extends Thread
{
	private final ServerProtocolInterface inter;
	private final ServerMessageSorter sms;
	private boolean connected = true;
	private final GameThread gt;

	public ServerPingThread(ServerProtocolInterface inter, ServerMessageSorter sms, GameThread gt)
	{
		this.inter = inter;
		this.sms = sms;
		this.gt = gt;
	}

	public void run()
	{
		inter.ping(true);
		while (connected)
		{
			try
			{
				inter.ping(false);
				Thread.sleep(4000);

				if (!sms.getPingMessage(inter.getPlayer().getNickname()))
				{
					sms.setPlayerConnection(inter.getPlayer().getNickname(), false);
					inter.getPlayer().setIsPlaying(false);
					connected = false;
					removeInterface();
				}
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void removeInterface()
	{
		connected = false;
		gt.RemovePlayerData(inter);
	}


}
