package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Client.ClientMessageSorter;
import it.polimi.ingsw.am38.Network.Client.ClientPingerThread;

public class TimerThread extends Thread
{
	private ClientMessageSorter cms;
	private boolean stillConnected;
	private ClientPingerThread cpt;

	public TimerThread(ClientMessageSorter cms, ClientPingerThread cpt)
	{
		this.cms = cms;
		this.cpt = cpt;
		this.stillConnected = false;
	}

	public void run()
	{
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
			cpt.setConnected(false);
			cms.setDisconnection();
		}
	}

	public void setStillConnected()
	{
		stillConnected = true;
	}

}

