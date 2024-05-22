package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.ObjectOutputStream;
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
	 * Attribute used to store the reference to the clientListener
	 */
	final private LinkedList <Thread> clientListeners;
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
	 * It contains the name of every player in the game
	 */
	final private LinkedList <String> playersName;
	/**
	 * Attributes that counts the number of player entered in game (post login phase)
	 */
	private int enteredPlayer = 0;
	/**
	 * It contains the reference of ChatThread
	 */
	//final private ChatThread chatThread;
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
		this.playersName = new LinkedList <>();
		playersName.add(host.getNickname());
		this.gameController = lobby.getGameController(game.getGameID());
		this.clientListeners = new LinkedList <>();
		this.interfaces = new LinkedList <>();
		this.serverInterpreter = new ServerMessageSorter();
		serverInterpreter.start();
		//this.chatThread = new ChatThread(interfaces, serverInterpreter);
		//chatThread.start();
		this.gameID = gameID;
		lobby.getReferenceContainer().add(this);
	}

	/**
	 * Method used to update the information of new entered player, then cause the start of the game
	 *
	 * @param p Player that is added
	 */
	public void addEntry(Player p, ClientInterface ci)
	{
		PlayerDataRMI pd = new PlayerDataRMI(p, ci);
		this.interfaces.add(pd);
		enteredPlayer++;
		playersName.add(p.getNickname());

		synchronized (host)
		{
			if (enteredPlayer == playerNumber)
			{
				host.notifyAll();
			}
		}
	}

	public void addEntry(Thread clientListener, ObjectOutputStream out, Player p)
	{
		PlayerDataTCP pd = new PlayerDataTCP(out, p);
		this.interfaces.add(pd);
		clientListeners.add(clientListener);
		enteredPlayer++;
		playersName.add(p.getNickname());

		synchronized (host)
		{
			if (enteredPlayer == playerNumber)
			{
				host.notifyAll();
			}
		}
	}

	/**
	 * Getter of the istance of game
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
		String input;
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

			//CLI SETUP PHASE

			LinkedList <SetUpPhaseThread> taskList = new LinkedList <>(); //creating a thread pool that allows player to do simultaneously the choice of color,the choice of the starter card's face, draw 3 cards and objective.
			LockClass                     locker   = new LockClass(gameController, gameController.getGame().getNumPlayers());
			for (ServerProtocolInterface playerData : interfaces)
			{
				SetUpPhaseThread sUpT = new SetUpPhaseThread(playerData, gameController, serverInterpreter, locker);
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
			for (ServerProtocolInterface playerData : interfaces)
				playerData.infoMessage("The game is now Started! Good luck!");

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
					do
					{
						inter.playCard();
						message = serverInterpreter.getGameMessage(currentPlayer.getNickname());
						MPlayCard pc = (MPlayCard) message.getContent();
						try
						{
							gameController.playerPlay(pc.getHandIndex(), pc.getCoords().x(), pc.getCoords().y(), pc.getFacing());
							control = false;
							//out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString("Your card was placed correctly")));
							inter.infoMessage("Your card was placed correctly");
						}
						catch (NotPlaceableException e)
						{
							control = true;
							//out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString(e.getMessage())));
							inter.infoMessage(e.getMessage());
						}
						catch (NoPossiblePlacement e)
						{
							//out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString(e.getMessage())));
							inter.infoMessage(e.getMessage());
							control = false;
						}

					} while (control);
					do
					{
						//out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString("Draw a card:")));
						inter.drawCard();
						MDrawCard dC = (MDrawCard) serverInterpreter.getGameMessage(currentPlayer.getNickname()).getContent();

						try
						{
							gameController.playerDraw(dC.getDeck(), dC.getIndex());
							control = false;
						}
						catch (EmptyDeckException e)
						{
							//out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString(e.getMessage())));
							inter.infoMessage(e.getMessage());
							control = true;
						}
					} while (control);
					//out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString("Your turn has ended!")));
					inter.endTurn();

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

	public ServerMessageSorter getServerInterpreter()
	{
		return serverInterpreter;
	}

	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

	public ServerProtocolInterface getPlayerData(String nick)
	{
		for (ServerProtocolInterface playerData : interfaces)
			if (playerData.getPlayer().getNickname().equals(nick))
				return playerData;
		return null;
	}
}
