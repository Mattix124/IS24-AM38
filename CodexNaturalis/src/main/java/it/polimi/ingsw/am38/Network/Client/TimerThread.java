package it.polimi.ingsw.am38.Network.Client;

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

