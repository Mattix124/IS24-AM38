package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Game;
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
			ci.sendLine(s);
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
	public void emptyDeck(String s)
	{
		try
		{
			ci.emptyDeck(s);
		}
		catch (RemoteException ignored)
		{
		}
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
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void colorSelection(String s)
	{
		try
		{
			ci.sendLine(s);
			ci.setPhase(CHOOSE2);
		}
		catch (RemoteException ignored)
		{
		}

	}

	@Override
	public void preObjChoiceViewUpdate(GameController gc, Player p)
	{
		try
		{
			int[] obj = new int[4];
			obj[0] = gc.getGame().getSharedObjectiveCards().get(0).getCardID();
			obj[1] = gc.getGame().getSharedObjectiveCards().get(1).getCardID();
			obj[2] = p.getPair().get(0).getCardID();
			obj[3] = p.getPair().get(1).getCardID();
			int[] hand = new int[3];
			hand[0] = p.getHand().getCard(0).getCardID();
			hand[1] = p.getHand().getCard(1).getCardID();
			hand[2] = p.getHand().getCard(2).getCardID();
			HashMap <String, Boolean> starterFacings = new HashMap <>(gc.getGame().getPlayersStarterFacing());
			HashMap <String, Color>   playersColors  = new HashMap <>(gc.getGame().getPlayersColors());
			HashMap <String, Symbol[]> handsColors   = new HashMap <>(gc.getGame().getPlayersCardsColors());
			ci.setChoosingObjective(obj,hand,starterFacings,playersColors,handsColors,new String[]{"You have drawn 2 Resource Card, 1 Gold Card","Chose one of them: type 'obj' and a number (1 or 2)"});

			//send data to client data

			ci.sendLine("Chose one of them: type 'obj' and a number (1 or 2)");
		}
		catch (RemoteException ignored)
		{
		}

	}

	@Override
	public void waitTextPlayers()
	{
		try
		{
			ci.setPhase(STANDBY);
			ci.sendLine("Waiting for other players...");
		}
		catch (RemoteException ignored)
		{
		}

	}

	@Override
	public void display(String s)
	{
		try
		{
			ci.display(s);
		}
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void enterGame(String s)
	{
		try
		{
			ci.enterGame(s);
		}
		catch (RemoteException ignored)
		{
		}

	}

	@Override
	public void playCard(String s)
	{
		try
		{
			ci.sendLine(s);
			ci.setPhase(PLAYPHASE);
		}
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void drawCard(String s)
	{
		try
		{
			ci.setPhase(DRAWPHASE);
			ci.sendLine(s);
		}
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void noPossiblePlacement(String s)
	{
		try
		{
			ci.noPossiblePlacement(s);
			ci.setPhase(NOCTURN);
		}
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void confirmedPlacement(int id, int x, int y, boolean face, int points, VisibleElements symbolTab)
	{
		try
		{
			ci.confirmedPlacement(id,x,y,face,points,symbolTab);
		}
		catch (RemoteException ignored)
		{
		}

	}

	@Override
	public void confirmedDraw(GameController gameController)
	{
		Game game = gameController.getGame();

		try
		{
			ci.confirmedDraw(gameController.getCardDrawnId(),  game.getGoldDeck().getGround0().getCardID(), game.getGoldDeck().getGround1().getCardID(),game.getResourceDeck().getGround0().getCardID(), game.getResourceDeck().getGround1().getCardID(),game.getGoldDeck().getTopCardKingdom(),  game.getResourceDeck().getTopCardKingdom());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void winnersMessage(String s)
	{
		try
		{
			ci.winnersMessage(s);
			ci.setPhase(NOCTURN);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void chatMessage(String s)
	{
		try
		{
			ci.printChatMessage(s);
		}
		catch (RemoteException ignored)
		{
		}
	}

	@Override
	public void turnShifter(String s)
	{
		try
		{
			ci.turnShifter(s);
		}
		catch (RemoteException ignored)
		{
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
				ci.clientPing();
		}
		catch (RemoteException e)
		{
			System.out.println(this.getPlayer().getNickname() + "is disconnected");
		}
	}

	@Override
	public void lightError(String s)
	{
		try
		{
			ci.lightError(s);
		}
		catch (RemoteException ignored)
		{
		}

	}
}
