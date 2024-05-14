package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
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
		this.playersName = new LinkedList <>();
		playersName.add(host.getNickname());
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
	public void addEntry(Thread clientListener, ObjectOutputStream out, Player p, boolean serverType, ClientInterface ci)
	{
		PlayerData pd = new PlayerData(p, out, serverType);
		clientListeners.add(clientListener);
		enteredPlayer++;
		playersName.add(p.getNickname());
		if(!serverType) pd.ci = ci;

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
		String              input;
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
				if(playerData.isServerBool()){
					SetUpPhaseThread sUpT = new SetUpPhaseThread(playerData, gameController, serverInterpreter, null);
					taskList.add(sUpT);
					super.start();
				}else{
					SetUpPhaseThread sUpT = new SetUpPhaseThread(playerData, gameController, serverInterpreter, playerData.ci);
					taskList.add(sUpT);
					sUpT.start();
				}

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

					if(pd.get(currentPlayer).isServerBool()){
						do
						{
							out.writeObject(new Message(GAME, PLAYCARD, new MSimpleString("Play your card:\n)")));
							message = serverInterpreter.getGameMessage(currentPlayer.getNickname());
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
							catch (NoPossiblePlacement e)
							{
								out.writeObject(new Message(GAME, EXCEPTION, new MSimpleString(e.getMessage())));
								notPlaceable = false;
							}

						} while (notPlaceable);

						out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString("Draw a card:\n")));
						MDrawCard dC = (MDrawCard) serverInterpreter.getGameMessage(currentPlayer.getNickname()).getContent();
						try
						{
							gameController.playerDraw(dC.getDeck(), dC.getIndex());
						}
						catch (EmptyDeckException e)
						{
							throw new RuntimeException(e);
						}
					}else{
						ClientInterface ci = pd.get(currentPlayer).ci;
						ci.startPLay();
						ci.startDraw();
					}

					winners = gameController.getWinners();
				} while (winners.isEmpty());

				for (PlayerData playerData : pd)
				{
					if (winners.size() == 1)
					{
						if(playerData.isServerBool()){
							playerData.getClOOut().writeObject(new Message(GAME, WINNER, new MSimpleString("The winner is: " + winners.getFirst().getNickname())));
							playerData.getClOOut().writeObject(new Message(KILL, KILL, new MSimpleString("The game will be closed in 10 seconds")));
						}else{
							//notifica chi vince
							//killa
						}
					}
					else
					{
						if(playerData.isServerBool()){
							playerData.getClOOut().writeObject(new Message(GAME, WINNER, new MSimpleString("The winner are: " + winners.getFirst().getNickname() + " " + winners.getLast().getNickname())));
							playerData.getClOOut().writeObject(new Message(KILL, KILL, new MSimpleString("The game will be closed in 10 seconds")));
						}else{
							//notifica chi vince
							//killa
						}
					}

				}

				break;
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

	private boolean isGameCreated()
	{
		return game.getScoreBoard() != null;
	}

	public GameThread getGameThread(){
		return this;
	}
}
