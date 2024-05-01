package it.polimi.ingsw.am38.Network.TCP;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * GameThread is the thread that allows the game to evolve, communicating with the client.
 */
public class GameThread extends Thread
{
	final Game game;
	final LobbyManager lobby = LobbyManager.getLobbyManager();
	final int playerNumber;

	final Map <Thread, MessageInterpreter> communicationMap = new HashMap <>();
	final Player host;

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

	}

	public void addEntryInCommunicationMap(Thread clientListener, MessageInterpreter msgInt)
	{
		communicationMap.put(clientListener, msgInt);
	}

	public Game getGame()
	{
		return game;
	}

	@Override
	public void run()
	{

	}
}
