package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

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
	final private Player host;
	final private LinkedList <PlayerData> pd;
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
		this.pd = new LinkedList <>();
		this.serverInterpreter = new MessageInterpreterServer();
		this.chatThread = new ChatThread(pd, serverInterpreter);
		chatThread.start();
	}

	public void addEntry(Thread clientListener, PrintWriter clOut, ObjectOutputStream out, Player p, boolean serverType, Scanner clIn)
	{
		PlayerData pd = new PlayerData(p, out, clOut, serverType, clIn);
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
			//CLI SETUP PHASE

			LinkedList <SetUpPhaseThread> taskList = new LinkedList <>(); //creating a thread pool that allows player to do simultaneously the choice of color,the choice of the starter card's face, draw 3 cards and objective.
			for (PlayerData playerData : pd)
			{
				SetUpPhaseThread sUpT = new SetUpPhaseThread(playerData, gameController, serverInterpreter);
				taskList.add(sUpT);
				super.start();
			}
			for (SetUpPhaseThread sUpT : taskList)
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
			//Game INIT
			do
			{

			}while ()
			break;
		}
	}

	public MessageInterpreterServer getServerInterpreter()
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
