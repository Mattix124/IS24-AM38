package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Network.Client.ClientMessageSorter;
import it.polimi.ingsw.am38.Network.Client.ClientPingerThread;

import java.util.concurrent.Callable;

public class TimerThread extends Thread implements Callable <Boolean>
{
	private ClientMessageSorter cms;
	private boolean stillConnected;
	private ClientPingerThread cpt;

	public TimerThread(ClientMessageSorter cms, ClientPingerThread cpt)
	{
		this.cms = cms;
		this.cpt = cpt;
	}

	public void run()
	{
		stillConnected = false;
		try
		{
			System.out.println("sjsaj");
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

	@Override
	public Boolean call() throws Exception
	{
		return null;
	}
}

