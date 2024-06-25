package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.DisconnectedException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;

/**
 * SetUpPhaseThread is the class that make all the player choose the starter card facing, the color of the pawn
 * and the personal objective card simultaneously
 */
public class SetUpPhaseThread extends Thread
{

	/**
	 * ServerMessageSorter Instance
	 */
	private final ServerMessageSorter sms;
	/**
	 * Instance of player
	 */
	private final Player p;
	/**
	 * Instance of the interface server side for the RMI or TCP connection
	 */
	private final ServerProtocolInterface inter;
	/**
	 * GameController instance
	 */
	private final GameController gc;

	/**
	 * Control class for synchronization
	 */
	private LockClass lock;

	/**
	 * Constructor method
	 *
	 * @param pd the server interface
	 * @param gC the GameController of the game
	 * @param mIS the ServerMessageSorter of the game
	 * @param locker of the game
	 */
	SetUpPhaseThread(ServerProtocolInterface pd, GameController gC, ServerMessageSorter mIS, LockClass locker)
	{
		this.sms = mIS;
		this.p = pd.getPlayer();
		this.gc = gC;
		this.inter = pd;
		this.lock = locker;
	}

	/**
	 * Running method that every client has to pass and allow to make chooses such as the face of the starter card,
	 * the color of the pawn and the objective card
	 */
	@Override
	public void run()
	{
		Message message;
		Color   c;
		boolean errorColor;

		try
		{
			inter.starterCardSelection(gc);
			inter.getPlayer().setIsPlaying(true);
			try
			{
				message = sms.getGameMessage(p.getNickname());
			}
			catch (DisconnectedException e)
			{
				System.out.println("disconnected");
				message = new Message(null, null, new MSimpleString("false"));
			}
			gc.chooseStarterCardFacing(p, Boolean.parseBoolean(((MSimpleString) message.getContent()).getText()));
			inter.colorSelection("Color/Choose a color for your pawn: type 'color' and a color: (\u001B[1;34mBLUE\u001B[0m, \u001B[1;31mRED\u001B[0m, \u001B[1;33mYELLOW\u001B[0m, \u001B[1;32mGREEN\u001B[0m)");
			do
			{
				try
				{
					message = sms.getGameMessage(p.getNickname());

					c = switch (((MSimpleString) message.getContent()).getText())
					{
						case "blue" -> Color.BLUE;
						case "red" -> Color.RED;
						case "yellow" -> Color.YELLOW;
						case "green" -> Color.GREEN;
						default -> null;
					};
					gc.chooseColor(p, c);
					errorColor = false;

				}
				catch (DisconnectedException e)
				{
					randColor(p);
					errorColor = false;
				}
				catch (ColorTakenException e)
				{
					errorColor = true;
					inter.lightError("Chosen/Unfortunately the color you chose was taken by another player, try another one");
				}

			} while (errorColor);
			lock.waitForPlayers(inter);
			inter.preObjChoiceViewUpdate(gc, p);
			try
			{
				message = sms.getGameMessage(p.getNickname());
			}
			catch (DisconnectedException e)
			{
				message = new Message(null, null, new MSimpleString("1"));
			}

			gc.choosePersonalObjectiveCard(p, Integer.parseInt(((MSimpleString) message.getContent()).getText()));
			lock.waitForPlayers(inter);
		}
		catch (Exception e)
		{
			return;
		}
	}

	/**
	 * Method to assign a random color to a player that has disconnected during the setup phase
	 *
	 * @param p
	 */
	private void randColor(Player p)
	{
		for (Color c : Color.values())
		{
			try
			{
				gc.chooseColor(p, c);
				break;
			}
			catch (ColorTakenException e)
			{
				continue;
			}
		}
	}
}