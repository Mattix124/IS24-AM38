package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

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
	final private PlayerDataList pd;
	/**
	 * Attributes that counts the number of player entered in game (post login phase)
	 */
	private int enteredPlayer = 0;
	/**
	 * It contains the reference of ChatThread
	 */
	final private ChatThread chatThread;
	/**
	 * It contains the reference to serverInterpreter
	 */
	final private ServerMessageSorter serverInterpreter;

	/**
	 * The constructor of Gamethread
	 *
	 * @param host         it indicates which player create the game
	 * @param gameId       The id that defines the game
	 * @param playerNumber The number of player chosen by the host
	 */
	public GameThread(Player host, int gameId, int playerNumber)
	{
		try
		{
			game = lobby.getGame(gameId);
		}
		catch (GameNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		this.playerNumber = playerNumber;
		this.host = host;
		this.gameController = lobby.getGameController(game.getGameID());
		this.clientListeners = new LinkedList <>();
		this.pd = new PlayerDataList();
		this.serverInterpreter = new ServerMessageSorter();
		this.chatThread = new ChatThread(pd, serverInterpreter);
		chatThread.start();
	}

	/**
	 * Method used to update the information of new entered player, then cause the start of the game
	 *
	 * @param clientListener the thread that allows the server to listen to the client bounded
	 * @param out            the ObjectOutputStream needed to the communication with the client
	 * @param p              Player that is added
	 * @param serverType     the type of the server that player specifies (RMI , TCP)
	 */
	public void addEntry(Thread clientListener, ObjectOutputStream out, Player p, boolean serverType)
	{
		PlayerData pd = new PlayerData(p, out, serverType);
		clientListeners.add(clientListener);
		enteredPlayer++;
		synchronized (host)
		{
			if (enteredPlayer == playerNumber)
				host.notifyAll();
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
			for (PlayerData playerData : pd)
			{
				SetUpPhaseThread sUpT = new SetUpPhaseThread(playerData, gameController, serverInterpreter);
				taskList.add(sUpT);
				super.start();
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
			Message message;
			Player  currentPlayer;
			try
			{
				List <Player> winners;
				do
				{
					boolean notPlaceable = true;
					currentPlayer = game.getCurrentPlayer();
					ObjectOutputStream out = pd.get(currentPlayer).getClOOut();
					do
					{
						out.writeObject(new Message(GAME, PLAYCARD, new MSimpleString("Play your card:\n 1, 2, 3 and the face (up,down)")));
						message = serverInterpreter.getGameMessage();
						MPlayCard pc = (MPlayCard) message.getContent();
						try
						{
							gameController.playerPlay(pc.getHandIndex(), pc.getCoords().x(), pc.getCoords().y(), pc.getFacing());
							notPlaceable = false;
							out.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString("Your card was placed correctly")));
						}
						catch (NotPlaceableException e)
						{
							notPlaceable = true;
							out.writeObject(new Message(GAME, EXCEPTION, new MSimpleString(e.getMessage())));
						}

					} while (notPlaceable);
					out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString("Draw a card: 'gold' or 'resource' for the type and the number of the place\n(0 for the deck card \n1 for the first card on the ground\n2 for the second card on the ground")));
					MDrawCard dC = (MDrawCard) serverInterpreter.getGameMessage().getContent();
					try
					{
						gameController.playerDraw(dC.getDeck(), dC.getIndex());
					}
					catch (EmptyDeckException e)
					{
						throw new RuntimeException(e);
					}
					winners = gameController.getWinners();
				} while (winners.isEmpty());
				//WINNER DISPLAY
			}
			catch (InvalidInputException | IOException e)
			{
				throw new RuntimeException(e);
			}

		}

	}

	public ServerMessageSorter getServerInterpreter()
	{
		return serverInterpreter;
	}

	private Object getMessage()
	{
		return serverInterpreter.getGameMessage();
	}

	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

}
