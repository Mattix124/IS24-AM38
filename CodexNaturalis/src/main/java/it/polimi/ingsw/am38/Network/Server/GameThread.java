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
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

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
	final private PlayerDataList pd;
	private int enteredPlayer = 0;
	final private ChatThread chatThread;
	final private ServerMessageSorter serverInterpreter;

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
			Message message;
			Player  currentPlayer;
			try
			{
				List<Player> winners;
				do
				{
					boolean notPlaceable = true;
					currentPlayer = game.getCurrentPlayer();
					ObjectOutputStream out = pd.get(currentPlayer).getClOOut();
					do
					{
						out.writeObject(new Message(GAME, TURN, new MSimpleString("Play your card:\n 1, 2, 3 and the face (up,down)")));
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
							out.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString(e.getMessage())));
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
