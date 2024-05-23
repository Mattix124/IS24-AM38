package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;

import java.rmi.RemoteException;
import java.util.HashMap;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * PlayerData is a class () that contains the needed elements to let the server manage the connection of the client associated with the player.
 */
public class PlayerDataRMI implements ServerProtocolInterface
{
	/**
	 * Player instance
	 */
	private final Player player;

	/**
	 * ClientInterface instance of the RMI player (null if TCP)
	 */
	ClientInterface ci;

	/**
	 * Constructor of PlayerData
	 *
	 * @param player Player Instance
	 * @param ci     client interface for rmi
	 */
	public PlayerDataRMI(Player player, ClientInterface ci)
	{
		this.player = player;
		this.ci = ci;
	}

	/**
	 * Getter for the player
	 *
	 * @return player
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * Getter for ClientInterface
	 *
	 * @return ClientInterface
	 */

	@Override
	public void starterCardSelection(GameController gc, String nick)
	{
		try
		{
			HashMap<String, Integer> starterCards = gc.getGame().getNicksAndStartersIDs();
			ci.setStarterCards(starterCards, gc.getGame().getGoldDeck().getTopCardKingdom(), gc.getGame().getResourceDeck().getTopCardKingdom(), gc.getGame().getGoldDeck().getGroundCards(), gc.getGame().getResourceDeck().getGroundCards());
			ci.printLine("Choose a face for your card (up or down)");
			ci.setPhase(Turnings.CHOOSE1);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void colorSelection()
	{
		try
		{
			ci.printLine("Choose a color for your pawn (blue, red, yellow, green)");
			ci.setPhase(CHOOSE2);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void errorMessage(String s)
	{
		try
		{
			ci.printLine(s);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void preObjChoiceViewUpdate(GameController gc, Player p)
	{
		try
		{
			ci.printLine("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective");
			ci.setChoosingObjective(p.getPair().get(0).getDescription(), p.getPair().get(1).getDescription());

			//send data to client data

			ci.printLine("Chose one of them: type 'obj' and a number (1 or 2)");
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void waitTextPlayers()
	{
		try
		{
			ci.setPhase(STANDBY);
			ci.printLine("Waiting for other players...");
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void infoMessage(String s)
	{
		try
		{
			ci.printLine(s);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void playCard()
	{
        try {
            ci.setPhase(PLAYPHASE);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void drawCard()
	{
        try {
            ci.setPhase(DRAWPHASE);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void exceptionMessage(String s, int i)
	{

	}

	@Override
	public void endTurn()
	{
        try {
            ci.setPhase(NOCTURN);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void winnersMessage(String s)
	{

	}
}
