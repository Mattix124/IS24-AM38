package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;

import java.util.LinkedList;

/**
 * Thread used to check if there is only one player left in the game that become automatically the winner
 */
public class TimerWinner extends Thread
{
	/**
	 * Instance of GameThread of the current game
	 */
	private GameThread gt;
	/**
	 * Instance of the controller of the current game
	 */
	private GameController gc;
	/**
	 * List of the interfaces server side of every player
	 */
	private LinkedList <ServerProtocolInterface> interfaces;

	/**
	 * Constructor method that set the attributes needed
	 *
	 * @param gt GameThread
	 * @param gc GameController
	 * @param interfaces list of interfaces server side
	 */
	public TimerWinner(GameThread gt, GameController gc, LinkedList <ServerProtocolInterface> interfaces)
	{
		this.gt = gt;
		this.gc = gc;
		this.interfaces = interfaces;
	}

	/**
	 * Every 6 seconds check if there is only ine player left, if so, says to him that he's the winner
	 */
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

