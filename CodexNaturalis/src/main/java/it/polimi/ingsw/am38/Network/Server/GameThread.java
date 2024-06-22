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

		if (!reconnect)
			sync(pd);
		else
		{
			serverInterpreter.setPlayerConnection(pd.getPlayer().getNickname(), true);
			pd.getPlayer().setIsPlaying(true);
			pd.resendInfo(game); //inviare campi e punti avversari, e carta pescata se necessaria
		}
		pingT.start();
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
		while (true)
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
			checkConnections();
			for (ServerProtocolInterface user : interfaces)
				user.enterGame("The game is now Started! Good luck!");

			Message message;
			try
			{
				List <Player> winners;
				do
				{
					checkConnections();
					boolean                 control;
					Player                  currentPlayer = game.getCurrentPlayer();
					ServerProtocolInterface playingPlayer = interfaces.stream().filter(x -> x.getPlayer() == currentPlayer).toList().getFirst();
					for (ServerProtocolInterface user : interfaces)
						user.turnShifter(playingPlayer.getPlayer().getNickname());

					boolean disconnection = false;
					do
					{
						playingPlayer.playCard("Play a card:");
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
								user.confirmedPlacement(playingPlayer.getPlayer().getNickname(), id, pc.getCoords().x() - pc.getCoords().y(), pc.getCoords().y() + pc.getCoords().x(), pc.getFacing(), gameController.getGame().getScoreBoard().getPlayerScores().get(currentPlayer.getColor()), gameController.getSymbolTab());

						}

						catch (NotPlaceableException e)
						{
							control = true;
							playingPlayer.lightError(e.getMessage());
						}
						catch (NoPossiblePlacement e) // -------------------> COMMENTO PER MATTEA: DA MANDARE A TODOS
						{
							playingPlayer.noPossiblePlacement(e.getMessage());
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
								playingPlayer.drawCard("Draw a card:");
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
											user.confirmedOtherDraw(gameController);
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
							playingPlayer.disconnectionHangingCard(drawRand());
						}
						playingPlayer.turnShifter("Your turn has ended!");
					}
					else
					{
						playingPlayer.disconnectionHangingCard(drawRand());
					}

					gameController.passTurn();
					winners = gameController.getWinners();
				} while (winners == null);

				for (ServerProtocolInterface players : interfaces)
				{
					if (winners.size() == 1)
						players.winnersMessage("The winner is: " + winners.getFirst().getNickname());
					else
						players.winnersMessage("The winner are: " + winners.getFirst().getNickname() + " " + winners.getLast().getNickname());

				}
				//chiudi tutto sul server
			}
			catch (InvalidInputException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public ServerMessageSorter getServerMessageSorter()
	{
		return serverInterpreter;
	}

	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

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

	public void changeGameThreadConnectionCount(boolean connected)
	{
		if (connected)
			connectedNow++;
		else
			connectedNow--;
	}

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

	private int drawRand()
	{
		String[] deck = {"resource", "gold"};
		int      id   = -1;
		for (int j = 0 ; j < 2 ; j++)
			for (int i = 0 ; i < 3 ; i++)
			{
				try
				{
					gameController.playerDraw(deck[j], i);
					return gameController.getCardDrawnId();
				}
				catch (InvalidInputException | EmptyDeckException e)
				{
					//
				}
			}
		return -1;
	}

	private void checkConnections() //SBAGLIATO RIGUARDA //probabilmente non serve il thread
	{
		if (connectedNow <= 1)
		{
			if (connectedNow == 0)
			{
				//chiudi tutto
				return;
			}

			TimerWinner timer = new TimerWinner(this);
			timer.start();
			try
			{
				timer.join();
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			if (connectedNow <= 1)
			{

				ServerProtocolInterface winner = null;
				for (ServerProtocolInterface pd : interfaces)
					if (pd.getPlayer().equals(gameController.getGame().getCurrentPlayer()))
						winner = pd;

				winner.winnersMessage("(FORFEIT) The winner is: " + winner.getPlayer().getNickname());
				//chiudi tutto
			}
		}
	}
}
