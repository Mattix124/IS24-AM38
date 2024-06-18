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
public class ImplementerRmi implements ServerProtocolInterface
{
	/**
	 * Player instance
	 */
	private Player player;

	/**
	 * ClientInterface instance of the RMI player (null if TCP)
	 */
	private ClientInterface ci;

	private ServerPingThread spt;

	/**
	 * Constructor of PlayerData
	 *
	 * @param ci client interface for rmi
	 */
	public ImplementerRmi(ClientInterface ci)
	{
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

	@Override
	public void setClientUsername(String s)
	{
		try
		{
			ci.sendNickname(s);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public String loginRequest(String s)
	{
		try
		{
			ci.printLine(s);
			return ci.getString();
		}
		catch (RemoteException e)
		{
			return null;
		}
	}

	@Override
	public void finalizeInitialization(GameThread gt, Player p, boolean reconnect)
	{
		this.player = p;
		gt.addEntry(this, reconnect);
	}

	@Override
	public void addPingThread(ServerPingThread spt)
	{
		this.spt = spt;
	}

	/**
	 * Getter for ClientInterface
	 *
	 * @return ClientInterface
	 */

	@Override
	public void starterCardSelection(GameController gc)
	{
		try
		{
			HashMap <String, Integer> starterCards = gc.getGame().getNicksAndStartersIDs();
			ci.setStarterCards(starterCards, gc.getGame().getGoldDeck().getTopCardKingdom(), gc.getGame().getResourceDeck().getTopCardKingdom(), gc.getGame().getGoldDeck().getGroundCards(), gc.getGame().getResourceDeck().getGroundCards());
			ci.setPhase(Turnings.CHOOSE1);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public void colorSelection(String s)
	{
		try
		{
			ci.printLine(s);
			ci.setPhase(CHOOSE2);
		}
		catch (RemoteException e)
		{
			return;
		}

	}

	@Override
	public void confirmedPlacement( int id, int x, int y, boolean face)
	{
		return;
	}

	@Override
	public void preObjChoiceViewUpdate(GameController gc, Player p)
	{
		try
		{
			ci.printLine("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective");
			int[] obj = new int[4];
			obj[0] = gc.getGame().getSharedObjectiveCards().get(0).getCardID();
			obj[1] = gc.getGame().getSharedObjectiveCards().get(1).getCardID();
			obj[2] = p.getPair().get(0).getCardID();
			obj[3] = p.getPair().get(1).getCardID();
			ci.setChoosingObjective(p.getPair().get(0).getDescription(), p.getPair().get(1).getDescription(), obj);

			//send data to client data

			ci.printLine("Chose one of them: type 'obj' and a number (1 or 2)");
		}
		catch (RemoteException e)
		{
			return;
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
			return;
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
	public void playCard(String s)
	{
		try
		{
			ci.printLine(s);
			ci.setPhase(PLAYPHASE);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public void drawCard(String s)
	{
		try
		{
			ci.setPhase(DRAWPHASE);
			ci.printLine(s);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public void exceptionMessage(String s, int i)
	{

	}

	@Override
	public void endTurn(String s)
	{
		try
		{
			ci.printLine(s);
			ci.setPhase(NOCTURN);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public void winnersMessage(String s)
	{

	}

	@Override
	public void chatMessage(String s)
	{
        try {
            ci.printChatMessage(s);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void startGameMessage(String s)
	{
		try
		{
			ci.printLine(s);
		}
		catch (RemoteException e)
		{
			return;
		}
	}

	@Override
	public void ping(boolean b)
	{
		try
		{
			if (b)
				ci.startPing();
			else
				ci.cping();
		}
		catch (RemoteException e)
		{
			System.out.println(this.getPlayer().getNickname() + "is disconnected");
		}
	}
}
