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
	 * Instance of ServerMessageSorter of the current game
	 */
	private ServerMessageSorter sms;
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
	 * @param gt         GameThread
	 * @param gc         GameController
	 * @param interfaces list of interfaces server side
	 */
	public TimerWinner(GameThread gt, GameController gc, LinkedList <ServerProtocolInterface> interfaces, ServerMessageSorter sms)
	{
		this.sms = sms;
		this.gt = gt;
		this.gc = gc;
		this.interfaces = interfaces;
	}

	/**
	 * Every 6 seconds check if there is only one player left, if so, says to him that he's the winner
	 */
	public void run()
	{
		try
		{
			Thread.sleep(6000);
		}
		catch (InterruptedException ignored)
		{

		}

		if (gt.getConnectedNow() < 2)
		{
			ServerProtocolInterface winner;
			interfaces.stream().forEach(x -> System.out.println(x.getPlayer().getNickname()));
			winner = interfaces.getFirst();

			winner.winnersMessage("Winner/(FORFEIT) The winner is: " + winner.getPlayer().getNickname());
			LobbyManager.getLobbyManager().getGameThreadList().remove(gt);
			gt.setClose();
			gt.interrupt();
			sms.interrupt();
		}

	}
}

