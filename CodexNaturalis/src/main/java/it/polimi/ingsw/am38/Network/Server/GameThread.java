package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * GameThread is the thread that allows the game to evolve, communicating with the client.
 */
public class GameThread extends Thread
{
	/**
	 * Attribute that contains the instance of the game
	 */
	final private Game game;
	/**
	 * Instance of LobbyManager as Singleton
	 */
	final private LobbyManager lobby = LobbyManager.getLobbyManager();
	/**
	 * Attribute that contain the number of desired player in the game
	 */
	final private int playerNumber;
	/**
	 * Instance of Gamecontroller
	 */
	final private GameController gameController;
	/**
	 * Attributes that contains the host
	 */
	final private Player host;
	/**
	 * It contains all the Interface that allows the server to send information to the clients
	 */
	final private LinkedList <ServerProtocolInterface> interfaces;
	/**
	 * Integer that contains the number of client disconnected at a certain moment.
	 */
	private int connectedNow;

	/**
	 * Attributes that counts the number of player entered in game (post login phase)
	 */
	private int enteredPlayer = 0;
	/**
	 * It contains the reference of ChatThread
	 */
	private ChatThread chatThread;
	/**
	 * It contains the reference to serverInterpreter
	 */
	final private ServerMessageSorter serverInterpreter;
	/**
	 * Boolean that allows the cycle of the winner to stop
	 */
	private boolean close = false;
	/**
	 * The constructor of Gamethread
	 *
	 * @param host         it indicates which player create the game
	 * @param gameID       The id that defines the game
	 * @param playerNumber The number of player chosen by the host
	 */
	public GameThread(Player host, int gameID, int playerNumber)
	{
		try
		{
			game = lobby.getGame(gameID);
		}
		catch (GameNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		this.playerNumber = playerNumber;
		this.host = host;
		this.gameController = lobby.getGameController(game.getGameID());
		this.interfaces = new LinkedList <>();
		this.serverInterpreter = new ServerMessageSorter(this);
		serverInterpreter.setDaemon(true);
		serverInterpreter.start();
		lobby.getReferenceContainer().add(this);
	}

	/**
	 * Method used to add the information of a new (or reconnected) player.
	 *
	 * @param pd        player's interface
	 * @param reconnect boolean that describes if is a first connection or a reconnection
	 */

	public synchronized void addEntry(ServerProtocolInterface pd, boolean reconnect)
	{
		this.interfaces.add(pd);
		serverInterpreter.addPlayer(pd.getPlayer().getNickname());
		ServerPingThread pingT = new ServerPingThread(pd, serverInterpreter, this);
		pingT.setDaemon(true);
		pd.addPingThread(pingT);

		pingT.start();

		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		if (!reconnect)
			sync(pd);
		else
		{
			serverInterpreter.setPlayerConnection(pd.getPlayer().getNickname(), true);
			pd.resendInfo(game);
		}

	}

	/**
	 * Getter of the instance of game
	 *
	 * @return The game
	 */
	public Game getGame()
	{
		return game;
	}

	/**
	 * The flow of the game itself. This method activate after the login phase and then define every step
	 */
	@Override
	public void run()
	{
		synchronized (host)
		{
			while (!isGameCreated())
			{
				try
				{
					host.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		this.chatThread = new ChatThread(interfaces, serverInterpreter);
		chatThread.setDaemon(true);
		chatThread.start();
		LinkedList <SetUpPhaseThread> taskList = new LinkedList <>(); //creating a thread pool that allows player to do simultaneously the choice of color,the choice of the starter card's face, draw 3 cards and objective.
		LockClass                     locker   = new LockClass(gameController, gameController.getGame().getNumPlayers());
		for (ServerProtocolInterface inter : interfaces)
		{
			SetUpPhaseThread sUpT = new SetUpPhaseThread(inter, gameController, serverInterpreter, locker);
			taskList.add(sUpT);
			sUpT.start();
		}
		for (SetUpPhaseThread sUpT : taskList) //waiting all the players to effectively start the game.
		{
			try
			{
				sUpT.join();
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
		taskList.clear();
		try
		{

			for (ServerProtocolInterface user : interfaces)
				user.enterGame("The game is now Started! Good luck!");
			checkConnections();
			Message message;
			try
			{
				List <Player> winners;
				do
				{

					boolean                 control;
					Player                  currentPlayer = game.getCurrentPlayer();
					ServerProtocolInterface playingPlayer = null;

					playingPlayer = interfaces.stream().filter(x -> x.getPlayer() == currentPlayer).toList().getFirst();
					for (ServerProtocolInterface user : interfaces)
						user.turnShifter(playingPlayer.getPlayer().getNickname());

					boolean disconnection = false;
					do
					{
						playingPlayer.playCard("Play/Play a card:");
						try
						{
							message = serverInterpreter.getGameMessage(currentPlayer.getNickname());
						}
						catch (DisconnectedException e)
						{

							for (ServerProtocolInterface user : interfaces)
								if (user.getPlayer() != playingPlayer.getPlayer())
									playingPlayer.chatMessage(playingPlayer.getPlayer().getNickname() + "disconnected");
							disconnection = true;
							break;
						}
						MPlayCard pc = (MPlayCard) message.getContent();

						try
						{
							int id = currentPlayer.getHand().getCard(pc.getHandIndex()).getCardID();
							gameController.playerPlay(pc.getHandIndex(), pc.getCoords().x(), pc.getCoords().y(), pc.getFacing());

							control = false;
							for (ServerProtocolInterface user : interfaces)
								user.confirmedPlacement(playingPlayer.getPlayer().getNickname(), id, pc.getCoords().x() - pc.getCoords().y(), pc.getCoords().y() + pc.getCoords().x(), pc.getFacing(), gameController.getGame().getScoreBoard().getPlayersScores().get(currentPlayer.getColor()), gameController.getSymbolTab());

						}

						catch (NotPlaceableException e)
						{
							control = true;
							playingPlayer.lightError("NotPlaceable/" + e.getMessage());

						}
						catch (NoPossiblePlacement e)
						{
							playingPlayer.noPossiblePlacement(e.getMessage());
							for (ServerProtocolInterface user : interfaces)
								if (!user.getPlayer().equals(playingPlayer.getPlayer()))
									user.lightError("NoOtherPosPlac/" + playingPlayer.getPlayer().getNickname() + " is now stuck");
							control = false;
							playingPlayer.getPlayer().setStuck(true);

						}

					} while (control);
					if (!disconnection)
					{
						try
						{
							do
							{
								playingPlayer.drawCard("Draw/Draw a card:");
								MDrawCard dC;
								dC = (MDrawCard) serverInterpreter.getGameMessage(currentPlayer.getNickname()).getContent();
								try
								{
									gameController.playerDraw(dC.getDeck(), dC.getIndex());
									control = false;

									playingPlayer.confirmedDraw(gameController);
									for (ServerProtocolInterface user : interfaces)
									{
										if (!user.getPlayer().equals(playingPlayer.getPlayer()))
											user.confirmedOtherDraw(gameController, gameController.getGame().getPlayersCardsColors().get(playingPlayer.getPlayer().getNickname()));
									}

								}
								catch (EmptyDeckException e)
								{
									playingPlayer.emptyDeck(e.getMessage());
									control = true;
								}
							} while (control);
						}
						catch (DisconnectedException e)
						{
							currentPlayer.setHangingDrawId(drawRand());
						}
					}
					else
					{
						currentPlayer.setHangingDrawId(drawRand());
					}

					gameController.passTurn();
					winners = gameController.getWinners();
				} while (winners == null);

				for (ServerProtocolInterface players : interfaces)
				{
					if (winners.size() == 1)
						players.winnersMessage("Winner/The winner is: " + winners.getFirst().getNickname());
					else
						players.winnersMessage("Winner/The winner are: " + winners.getFirst().getNickname() + " " + winners.getLast().getNickname());

				}
				closeAll();
			}
			catch (InvalidInputException e)
			{
				throw new RuntimeException(e);
			}

		}
		catch (NoSuchElementException | InterruptedException e)
		{
			closeAll();
		}
	}

	/**
	 * Method to close the game
	 */
	private void closeAll()
	{
		lobby.getGameThreadList().remove(this);
		lobby.endAGame(game);
	}

	/**
	 * Getter for the ServerMessageSorter
	 *
	 * @return
	 */
	public ServerMessageSorter getServerMessageSorter()
	{
		return serverInterpreter;
	}

	/**
	 * Method that allow the game to start after all player has connected
	 *
	 * @return
	 */
	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

	/**
	 * @param s
	 */
	private void sync(ServerProtocolInterface s)
	{
		synchronized (host)
		{
			enteredPlayer++;
			if (enteredPlayer == playerNumber)
			{
				host.notifyAll();
			}
			else
				s.waitTextPlayers();
		}
	}

	public void setClose()
	{
		close = true;
	}
	/**
	 * Method to update the number of players connected
	 *
	 * @param connected
	 */
	public void changeGameThreadConnectionCount(boolean connected)
	{
		if (connected)
			connectedNow++;
		else
			connectedNow--;
	}

	/**
	 * Method that clear the data of the player who disconnected
	 *
	 * @param pd
	 */
	public void RemovePlayerData(ServerProtocolInterface pd)
	{
		ServerProtocolInterface remover = null;
		for (ServerProtocolInterface inter : interfaces)
		{
			if (inter.equals(pd))
			{
				remover = inter;
				break;
			}
		}
		interfaces.remove(remover);
	}

	/**
	 * @return
	 */
	private int drawRand()
	{
		String[] deck = {"resource", "gold"};
		for (int j = 0 ; j < 2 ; j++)
			for (int i = 0 ; i < 3 ; i++)
			{
				try
				{
					gameController.playerDraw(deck[j], i);
					return gameController.getCardDrawn().getCardID();
				}
				catch (InvalidInputException | EmptyDeckException e)
				{
					//
				}
			}
		return -1;
	}

	/**
	 * Method to check the connection of the players
	 */
	private void checkConnections()
	{

		Thread checkConnectionTimer = new Thread(() -> {
			while (!close)
			{
				TimerWinner timer        = null;
				int         connectedOld = connectedNow;
				if (connectedNow <= 1)
				{
					if (connectedNow == 0)
					{
						closeAll();
						return;
					}

					timer = new TimerWinner(this, gameController, interfaces,serverInterpreter);
					timer.start();
				}
				try
				{
					Thread.sleep(3000);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
				if (connectedNow > connectedOld)
				{
					timer.interrupt();
				}
				else if (connectedNow < 1)
				{
					closeAll();
					return;
				}
				try
				{
					Thread.sleep(5000);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}

			}
		});
		checkConnectionTimer.setDaemon(true);
		checkConnectionTimer.start();
	}

	/**
	 * Getter for the number of players connected at the game
	 *
	 * @return
	 */
	public int getConnectedNow()
	{
		return connectedNow;
	}
}
