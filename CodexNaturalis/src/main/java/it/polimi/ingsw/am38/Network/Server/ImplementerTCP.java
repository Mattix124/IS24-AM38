package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Model.ScoreBoard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

/**
 * ImplementerTCP is a class () that contains the needed elements to let the server manage the connection of the client associated with the player.
 */
public class ImplementerTCP implements ServerProtocolInterface
{
	/**
	 * Output stream to send message to the client
	 */
	private final ObjectOutputStream out;
	/**
	 * Input stream to receive message to the client
	 */
	private final ObjectInputStream in;
	/**
	 * Player instance
	 */
	private Player p;
	/**
	 * ServerPingThread instance
	 */
	private ServerPingThread spt;
	/**
	 * Int for the id of a card drawn by a player disconnected
	 */
	private int hangingDrawId;

	/**
	 * Constructor method that set the output and input stream from which to exchange messages
	 * with the client
	 *
	 * @param out output stream
	 * @param in input stream
	 */
	public ImplementerTCP(ObjectOutputStream out, ObjectInputStream in)
	{
		this.out = out;
		this.in = in;
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
			out.writeObject(new Message(LOGIN, NICKNAME, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			System.err.println("Error in nickname communication out");
		}
	}

	/**
	 * Method that send to the client messages for the login phase and get back
	 * the response
	 *
	 * @param s message to send to the client
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public String loginRequest(String s) throws IOException, ClassNotFoundException
	{
		out.writeObject(new Message(LOGIN, INFOMESSAGE, new MSimpleString(s)));
		return ((MSimpleString) ((Message) in.readObject()).getContent()).getText();
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
		this.p = p;
		ClientListener clGH     = new ClientListener(in, gt.getServerMessageSorter(), p);
		Thread         listener = new Thread(clGH);
		listener.setDaemon(true);
		listener.start();
		gt.addEntry(this, reconnect);
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
	 * Getter for the player
	 *
	 * @return
	 */
	@Override
	public Player getPlayer()
	{
		return p;
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
			out.writeObject(new Message(INFOMESSAGE, START, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method that take from the GameController the information to send to the client in order for it to
	 * choose the face of the starter card and set the phase of the client to CHOOSE1 (i.e. the phase that
	 * allow the client only to choose the face)
	 *
	 * @param gc the GameController of the game
	 */
	@Override
	public void starterCardSelection(GameController gc)
	{
		try
		{
			out.writeObject(new Message(GAME, STARTINGFACECHOICE, new MStartSetup(gc)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method that tells the client to choose the color for the pawn
	 *
	 * @param s string to display
	 */
	@Override
	public void colorSelection(String s)
	{
		try
		{
			out.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method that says to the client that the placement of a card has gone well
	 *
	 * @param nickName of the player that has played the card
	 * @param id id of the card placed
	 * @param x coordinates on player's field
	 * @param y coordinates on player's field
	 * @param face chosen for the card placed
	 * @param points given by the card placed
	 * @param symbolTab VisibleElements updated with the elements given by the card placed
	 */
	@Override
	public void confirmedPlacement(String nickName, int id, int x, int y, boolean face, int points, VisibleElements symbolTab)
	{
		try
		{
			out.writeObject(new Message(GAME, PLACEMENT, new MConfirmedPlacement(nickName, id, x, y, face, points, symbolTab)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(GAME, OBJECTIVECHOICE, new MObjViewSetup(gc, p)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString("Wait/Waiting for other players...")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method that communicates to the client that the deck is empty
	 *
	 * @param s string to display
	 */
	@Override
	public void enterGame(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			System.err.println("error in sending infomessage ");
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
			out.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(GAME, PLAYCARD, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
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
			out.writeObject(new Message(GAME, EMPTYDECK, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(GAME, NOPOSSIBLEPLACEMENT, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(GAME, WINNER, new MSimpleString(s)));
		}
		catch (IOException e)
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
			out.writeObject(new Message(CHAT, BCHAT, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
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
			out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to exchange ping with the client
	 *
	 * @param start if true start a new ping communication, if false send a ping to the client
	 */
	@Override
	public void ping(boolean start)
	{
		try
		{
			if (start)
			{
				out.writeObject(new Message(CONNECTION, START, null));
			}
			else
				out.writeObject(new Message(CONNECTION, CONNECTION, null));
		}
		catch (IOException ignored)
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
		try
		{
			out.writeObject(new Message(GAME, DRAWCONFIRMED, new MConfirmedDraw(gameController)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method that send updated info about the game to the client after another player has drawn
	 *
	 * @param gameController
	 * @param s
	 */
	@Override
	public void confirmedOtherDraw(GameController gameController, String[] s)
	{
		try
		{
			out.writeObject(new Message(VIEWUPDATE, DRAWCONFIRMED, new MConfirmedDraw(gameController, s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * Method that draw a card for a client disconnected
	 *
	 * @param id of the card drawn
	 */
	@Override
	public void setDisconnectionHangingCard(int id)
	{
		hangingDrawId = id;
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
		for (Player p : game.getPlayers())
		{
			PlayerDisconnectionResendInfo playerDisconnectionResendInfo = new PlayerDisconnectionResendInfo(p.getField().getOrderedField(), scores.getScore(p.getColor()), p.getHandCardsColors());
			resendInfoHashMap.put(p.getNickname(), playerDisconnectionResendInfo);
		}
		try
		{
			out.writeObject(new Message(CONNECTION, VIEWUPDATE, new MReconnectionInfo(resendInfoHashMap, hangingDrawId)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
