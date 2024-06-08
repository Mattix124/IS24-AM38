package it.polimi.ingsw.am38.Network.Server;

public class TimerWinner extends Thread
{

	private boolean reconnected = false;
	private GameThread gt;

	public TimerWinner(GameThread gt)
	{
		this.gt = gt;
	}

	public void run()
	{
		try
		{
			Thread.sleep(6000);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		if (!reconnected)
		{
			//tiemer scaduto. il vincitore è l'unico rimasto. (invio messaggio e chiusura)
		}

	}
}

