package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * GameThread is the thread that allows the game to evolve, communicating with the client.
 */
public class GameThread extends Thread
{
	final private Game game;
	final private LobbyManager lobby = LobbyManager.getLobbyManager();
	final private int playerNumber;
	final private LinkedList <Thread> clientListeners;
	final private GameController gameController;
	final private HashMap <Player, PrintWriter> communicationMap = new HashMap <>();
	final private Player host;
	private int enteredPlayer = 0;
	final private ChatThread chatThread;
	final private MessageInterpreterServer serverInterpreter;

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
		this.serverInterpreter = new MessageInterpreterServer();
		this.chatThread = new ChatThread(communicationMap, serverInterpreter);
		chatThread.start();
	}

	public void addEntry(Thread clientListener, PrintWriter clOut, Player p)
	{
		communicationMap.put(p, clOut);
		clientListeners.add(clientListener);
		enteredPlayer++;
		synchronized (host)
		{
			if (enteredPlayer == playerNumber)
				host.notifyAll();
		}

	}

	public Game getGame()
	{
		return game;
	}

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
			try
			{
				sleep(500);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			for (Player p : communicationMap.keySet())
				communicationMap.get(p).println("Game is Started. Enjoy!");


				/*for (Player p : communicationMap.keySet())
				{
					try
					{
						communicationMap.get(p).println("Choose your color:\n");
						input = getMessage();
						//gameController.chooseColor(p, );
					}
					catch (Exception e)
					{
					}

				}*/
			break;
		}
	}

	public MessageInterpreterServer getServerInterpreter()
	{
		return serverInterpreter;
	}

	private String getMessage()
	{
		return serverInterpreter.getGameMessage();
	}

	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

}
