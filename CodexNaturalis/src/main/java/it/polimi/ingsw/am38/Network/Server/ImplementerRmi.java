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
 * ImplementerRmi is a class () that contains the needed elements to let the server manage the connection of the client associated with the player.
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
	/**
	 * ServerPingThread instance
	 */
	private ServerPingThread spt;
	/**
	 * Int for the id of a card drawn by a player disconnected
	 */
	private int hangingDrawId;

	/**
	 * Constructor method that set the interface of the client on which call the methods
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

	/**
	 * Method that call the client to set the nickname in it
	 *
	 * @param s username validated
	 */
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

	/**
	 * Method that send to the client messages for the login phase and get back
	 * the response
	 *
	 * @param s message to send to the client
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Method that add the player logged in to the game
	 *
	 * @param gt GameThread of the game
	 * @param p player to add
	 * @param reconnect parameter set to true if the player is reconnecting to his previous game
	 */
	@Override
	public void finalizeInitialization(GameThread gt, Player p, boolean reconnect)
	{
		this.player = p;
		gt.addEntry(this, reconnect);
	}

	/**
	 * Method that communicates to the client that the deck is empty
	 *
	 * @param s string to display
	 */
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

	/**
	 * Setter for the ServerPingThread
	 *
	 * @param spt the ServerPingThread
	 */
	@Override
	public void addPingThread(ServerPingThread spt)
	{
		this.spt = spt;
	}

	/**
	 * Method that take from the GameController the information to send to the client in order for it to
	 * choose the face of the starter card and set the phase of the client to CHOOSE1 (i.e. the phase that
	 * allow the client only to choose the face)
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

	/**
	 * Method that tells the client to choose the color for the pawn and set the phase
	 * to CHOOSE2 (i.e. the phase that allow the client only to choose the color)
	 *
	 * @param s string to display
	 */
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

	/**
	 * Method that takes from the GameController and sends to client the info to let the client choose
	 * the objective card
	 *
	 * @param gc the GameController of the game
	 * @param p the player to send the info
	 */
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
			Symbol topG = gc.getGame().getGoldDeck().getTopCardKingdom();
			Symbol topR = gc.getGame().getResourceDeck().getTopCardKingdom();
			HashMap <String, Boolean>  starterFacings = new HashMap <>(gc.getGame().getPlayersStarterFacing());
			HashMap <String, Color>    playersColors  = new HashMap <>(gc.getGame().getPlayersColors());
			HashMap <String, String[]> handsColors    = new HashMap <>(gc.getGame().getPlayersCardsColors());
			HashMap<String, VisibleElements> playersVisibleElements = new HashMap<>();
			starterFacings.forEach((k, v) ->
				playersVisibleElements.put(k, gc.getGame().getPlayers().stream()
						.filter(x -> x.getNickname().equals(k))
						.map(x -> x.getGameField().getVisibleElements())
						.toList()
						.getFirst())
			);
			ci.setChoosingObjective(obj, hand, topG, topR, starterFacings, playersColors, handsColors, new String[]{"You have drawn 2 Resource Card, 1 Gold Card", "Chose one of them: type 'obj' and a number (1 or 2)"}, playersVisibleElements);

		}
		catch (RemoteException ignored)
		{
		}

	}

	/**
	 * Method that set the phase of the client to STANDBY (i.e. the phase in which the client can only send
	 * chat messages and see the commands available) to make it wait the other players
	 * to complete a certain action
	 */
	@Override
	public void waitTextPlayers()
	{
		try
		{
			ci.setPhase(STANDBY);
			ci.sendLine("Wait/Waiting for other players...");
		}
		catch (RemoteException ignored)
		{
		}

	}

	/**
	 * Method that sends to the client a string to display
	 *
	 * @param s string with the message
	 */
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

	/**
	 * Method that says to the client that the game is started (after the setup phase)
	 *
	 * @param s string to display
	 */
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

	/**
	 * Method that says to the client to play
	 *
	 * @param s string to display
	 */
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

	/**
	 * Method that says to the client to draw
	 *
	 * @param s string to display
	 */
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

	/**
	 * Method that communicates to the client that the spot chosen to play a card
	 * is not available
	 *
	 * @param s string to display
	 */
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

	/**
	 * Method that says to the client that the placement of a card has gone well
	 *
	 * @param nickname of the player that has played the card
	 * @param id id of the card placed
	 * @param x coordinates on player's field
	 * @param y coordinates on player's field
	 * @param face chosen for the card placed
	 * @param points given by the card placed
	 * @param symbolTab VisibleElements updated with the elements given by the card placed
	 */
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

	/**
	 * Method that says to the client that the draw of a card has gone well taking the info from the
	 * GameController and sending them to the client
	 *
	 * @param gameController GameController of the game
	 */
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

	/**
	 * Method that send updated info about the game to the client after another player has drawn
	 *
	 * @param gameController GameController of the game
	 * @param s the kingdoms of the hand of the player who drawn
	 */
	@Override
	public void confirmedOtherDraw(GameController gameController, String[] s)
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
        try
		{
            ci.otherDrawUpdate(nickname, resourceFaceUp1Id,resourceFaceUp2Id,goldFaceUp1Id,goldFaceUp2Id,resourceTopCardSymbol,goldTopCardSymbol, cardDrawnId, s);
        } catch (RemoteException ignored) {

        }
    }

	/**
	 * Method that says to the clients who the winner(s) is(/are)
	 *
	 * @param s string that contains the winner(s)
	 */
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

	/**
	 * Method that send to the client the chat message to print
	 *
	 * @param s chat message to display
	 */
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

	/**
	 * Method to communicates whose turn it is
	 *
	 * @param s nickname of the player whose turn it is
	 */
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

	/**
	 * Method to exchange ping with the client
	 *
	 * @param b if true start a new ping communication, if false send a ping to the client
	 */
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

	/**
	 * Method that communicates to the client some minor errors
	 *
	 * @param s string to display
	 */
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


	/**
	 * Method to send the information back to a client after a disconnection
	 *
	 * @param game the game to which the player has reconnected
	 */
	@Override
	public void resendInfo(Game game)
	{
		ScoreBoard                                      scores            = game.getScoreBoard();
		HashMap <String, PlayerDisconnectionResendInfo> resendInfoHashMap = new HashMap <>();
		int id = player.getHangingDrawId();
		for (Player pl : game.getPlayers())
		{
			PlayerDisconnectionResendInfo playerDisconnectionResendInfo = new PlayerDisconnectionResendInfo(pl.getGameField().getOrderedField(), scores.getScore(pl.getColor()), pl.getHandCardsColors(), pl.getGameField().getVisibleElements(), pl.getColor());
			resendInfoHashMap.put(pl.getNickname(), playerDisconnectionResendInfo);
		}
		try
		{
			ci.reconnectionDataUpdate(resendInfoHashMap, player.getHand().getCardsInHand(), player.getNickname(), game.getGoldDeck().getGround0().getCardID(), game.getGoldDeck().getGround1().getCardID(), game.getResourceDeck().getGround0().getCardID(), game.getResourceDeck().getGround1().getCardID(), game.getGoldDeck().getTopCardKingdom(), game.getResourceDeck().getTopCardKingdom(), game.getSharedObjectiveCards().getFirst().getCardID(), game.getSharedObjectiveCards().getLast().getCardID(), player.getObjectiveCard().getCardID());
		}
		catch (RemoteException ignored)
		{

		}
	}
}
