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
	private int gameID;
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
	 * ?
	 */
	final private LinkedList <ServerProtocolInterface> interfaces;

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

	final private LinkedList <ServerPingThread> pingThreadsList = new LinkedList <>();

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
		this.serverInterpreter = new ServerMessageSorter();
		serverInterpreter.setDaemon(true);
		serverInterpreter.start();
		this.gameID = gameID;
		lobby.getReferenceContainer().add(this);
	}

	/**
	 * Method used to update the information of new entered player, then cause the start of the game
	 *
	 * @param pd interface
	 */

	public synchronized void addEntry(ServerProtocolInterface pd)
	{
		this.interfaces.add(pd);
		serverInterpreter.addPlayer(pd.getPlayer().getNickname());
		ServerPingThread pingT = new ServerPingThread(pd, serverInterpreter, this);
		pingT.setDaemon(true);
		pd.addPingThread(pingT);
		pingT.start();
		sync(pd);
	}

	public synchronized void reconnection(Player p, ServerProtocolInterface pd)
	{
		ServerProtocolInterface remover = null;
		for (ServerProtocolInterface inter : interfaces)
		{
			if (inter.getPlayer().equals(p))
			{
				remover = inter;
				break;
			}
		}
		interfaces.remove(remover);
		interfaces.add(pd);
		serverInterpreter.setPlayerConnection(pd.getPlayer().getNickname(), true);
		ServerPingThread pingT = new ServerPingThread(pd, serverInterpreter, this);
		pingT.setDaemon(true);
		pd.addPingThread(pingT);
		p.setIsPlaying(true);
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
	 * The flow of the game itself. This method happens after the login phase and then define every step
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
			/*for (ServerPingThread pt : pingThreadsList)
				pt.start();*/
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
			for (ServerProtocolInterface playerData : interfaces)
				playerData.startGameMessage("The game is now Started! Good luck!");

			Message message;

			try
			{
				List <Player> winners;
				do
				{
					boolean                 control;
					Player                  currentPlayer = game.getCurrentPlayer();
					ServerProtocolInterface inter         = interfaces.stream().filter(x -> x.getPlayer() == currentPlayer).toList().getFirst();
					inter.infoMessage("Is now your turn! Use 'help' to see what you can do!");
					boolean disconnection = false;
					do
					{
						inter.playCard("Play a card:");
						try
						{
							message = serverInterpreter.getGameMessage(currentPlayer.getNickname());
						}
						catch (DisconnectedException e)
						{
							//broadcast to other players
							disconnection = true;
							break;
						}
						MPlayCard pc = (MPlayCard) message.getContent();
						try
						{
							gameController.playerPlay(pc.getHandIndex(), pc.getCoords().x(), pc.getCoords().y(), pc.getFacing());
							control = false;
							inter.confirmedPlacement("Your card was placed correctly");
						}

						catch (NotPlaceableException e)
						{
							control = true;
							inter.infoMessage(e.getMessage());
						}
						catch (NoPossiblePlacement e)
						{
							inter.infoMessage(e.getMessage());
							control = false;
						}

					} while (control);
					if (!disconnection)
					{
						do
						{
							inter.drawCard("Draw a card:");
							MDrawCard dC;
							try
							{
								dC = (MDrawCard) serverInterpreter.getGameMessage(currentPlayer.getNickname()).getContent();
							}
							catch (DisconnectedException e)
							{
								//broadcast to other players
								disconnection = true;
								break;
							}
							try
							{
								gameController.playerDraw(dC.getDeck(), dC.getIndex());
								control = false;
							}
							catch (EmptyDeckException e)
							{
								inter.infoMessage(e.getMessage());
								control = true;
							}

						} while (control);
					}
					else
					{

					}
						//end turn
					inter.endTurn("Your turn has ended!");

					winners = gameController.getWinners();
				} while (winners == null);

				for (ServerProtocolInterface players : interfaces)
				{
					if (winners.size() == 1)
					{
						players.winnersMessage("The winner is: " + winners.getFirst().getNickname());
					}
					else
					{
						players.winnersMessage("The winner are: " + winners.getFirst().getNickname() + " " + winners.getLast().getNickname());
					}
				}
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

	public void RemovePlayerData(String nick, ServerPingThread pingThread)
	{
	}
}
