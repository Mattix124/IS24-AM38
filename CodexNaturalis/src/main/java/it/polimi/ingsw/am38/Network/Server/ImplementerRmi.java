package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Model.ScoreBoard;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.io.IOException;
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
	private final ClientInterface ci;
	private ServerPingThread spt;
	private int hangingDrawId;

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
	@Override
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
	public String loginRequest(String s) throws IOException
	{
		try
		{
			ci.displayStringLogin(s);
			return ci.getStringLogin();
		}
		catch (RemoteException e)
		{
			throw new IOException();
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
			int[] goldCardsIds = new int[2];
			int[] resourceCardsIds = new int[2];
			goldCardsIds[0] = gc.getGame().getGoldDeck().getGroundCards()[0].getCardID();
			goldCardsIds[1] = gc.getGame().getGoldDeck().getGroundCards()[1].getCardID();
			resourceCardsIds[0] = gc.getGame().getResourceDeck().getGroundCards()[0].getCardID();
			resourceCardsIds[1] = gc.getGame().getResourceDeck().getGroundCards()[1].getCardID();
			ci.setStarterCards(starterCards, gc.getGame().getGoldDeck().getTopCardKingdom(), gc.getGame().getResourceDeck().getTopCardKingdom(), goldCardsIds, resourceCardsIds);
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
			HashMap <String, Boolean>  starterFacings = new HashMap <>(gc.getGame().getPlayersStarterFacing());
			HashMap <String, Color>    playersColors  = new HashMap <>(gc.getGame().getPlayersColors());
			HashMap <String, Symbol[]> handsColors    = new HashMap <>(gc.getGame().getPlayersCardsColors());
			HashMap<String, VisibleElements> playersVisibleElements = new HashMap<>();
			starterFacings.forEach((k, v) ->
				playersVisibleElements.put(k, gc.getGame().getPlayers().stream()
						.filter(x -> x.getNickname().equals(k))
						.map(x -> x.getField().getVisibleElements())
						.toList()
						.getFirst())
			);
			ci.setChoosingObjective(obj, hand, starterFacings, playersColors, handsColors, new String[]{"You have drawn 2 Resource Card, 1 Gold Card", "Chose one of them: type 'obj' and a number (1 or 2)"}, playersVisibleElements);

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
			ci.setPhase(NOCTURN);
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
	public void confirmedPlacement(String nickname, int id, int x, int y, boolean face, int points, VisibleElements symbolTab)
	{
		try
		{
			ci.confirmedPlacement(nickname, id, x, y, face, points, symbolTab);
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
			ci.confirmedDraw(gameController.getCardDrawn().getCardID(), game.getGoldDeck().getGround0().getCardID(), game.getGoldDeck().getGround1().getCardID(), game.getResourceDeck().getGround0().getCardID(), game.getResourceDeck().getGround1().getCardID(), game.getGoldDeck().getTopCardKingdom(), game.getResourceDeck().getTopCardKingdom());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void confirmedOtherDraw(GameController gameController, Symbol[] s)
	{
		String nickname = gameController.getGame().getCurrentPlayer().getNickname();
		Game game = gameController.getGame();
		int resourceFaceUp1Id = game.getResourceDeck().getGround0().getCardID();
		int resourceFaceUp2Id = game.getResourceDeck().getGround1().getCardID();
		int goldFaceUp1Id = game.getGoldDeck().getGround0().getCardID();
		int goldFaceUp2Id = game.getGoldDeck().getGround1().getCardID();
		Symbol resourceTopCardSymbol = game.getResourceDeck().getTopCardKingdom();
		Symbol goldTopCardSymbol = game.getGoldDeck().getTopCardKingdom();
		int cardDrawnId = 0;
		Symbol[] playerHandCardColors = s;
        try
		{
            ci.otherDrawUpdate(nickname, resourceFaceUp1Id,resourceFaceUp2Id,goldFaceUp1Id,goldFaceUp2Id,resourceTopCardSymbol,goldTopCardSymbol, cardDrawnId,playerHandCardColors);
        } catch (RemoteException ignored) {

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
			ci.setPhase(NOCTURN);
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

	@Override
	public void disconnectionHangingCard(int id)
	{
		hangingDrawId = id;
	}

	@Override
	public void resendInfo(Game game)
	{
		ScoreBoard                                      scores            = game.getScoreBoard();
		HashMap <String, PlayerDisconnectionResendInfo> resendInfoHashMap = new HashMap <>();
		for (Player p : game.getPlayers())
		{
			PlayerDisconnectionResendInfo playerDisconnectionResendInfo = new PlayerDisconnectionResendInfo(p.getField().getOrderedField(), scores.getScore(p.getColor()), p.getHandCardsColors());
			resendInfoHashMap.put(p.getNickname(), playerDisconnectionResendInfo);
		}
		try
		{
			ci.reconnectionDataUpdate(resendInfoHashMap, hangingDrawId);
		}
		catch (RemoteException ignored)
		{

		}

	}

}
