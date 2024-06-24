package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;

import java.util.LinkedList;

public class TimerWinner extends Thread
{

	private GameThread gt;
	private GameController gc;
	private LinkedList <ServerProtocolInterface> interfaces;

	public TimerWinner(GameThread gt, GameController gc, LinkedList <ServerProtocolInterface> interfaces)
	{
		this.gt = gt;
		this.gc = gc;
		this.interfaces = interfaces;
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

		if (gt.getConnectedNow() < 2)
		{
			gt.interrupt();
			ServerProtocolInterface winner = null;
			for (ServerProtocolInterface pd : interfaces)
				if (pd.getPlayer().equals(gc.getGame().getCurrentPlayer()))
					winner = pd;

			winner.winnersMessage("(FORFEIT) The winner is: " + winner.getPlayer().getNickname());
			LobbyManager.getLobbyManager().getGameThreadList().remove(gt);

			//tiemer scaduto. il vincitore è l'unico rimasto. (invio messaggio e chiusura)
		}

	}
}

