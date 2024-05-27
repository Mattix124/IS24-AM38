package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Client.ClientMessageSorter;
import it.polimi.ingsw.am38.Network.Client.ClientPingerThread;

public class TimerThread extends Thread
{
	private ClientMessageSorter cms;
	private ClientPingerThread cpt;

	public TimerThread(ClientMessageSorter cms, ClientPingerThread cpt)
	{
		this.cms = cms;
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
			System.out.println("interrotto");
			return;
		}
		cpt.setConnected(false);
		cms.setDisconnection();

	}

	public void setStillConnected()
	{
		this.interrupt();
	}

}

