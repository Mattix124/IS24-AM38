package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class SortPlayerThread implements Runnable
{

	private final LobbyManager lobbyManager = LobbyManager.getLobbyManager();
	private final ServerProtocolInterface inter;

	public SortPlayerThread(ClientInterface ci)
	{
		inter = new ImplementerRmi(ci);
	}

	public SortPlayerThread(Socket clSocket)
	{
		ObjectInputStream  clOIn  = null;
		ObjectOutputStream clOOut = null;
		do
		{
			try
			{
				clOIn = new ObjectInputStream(clSocket.getInputStream());
				clOOut = new ObjectOutputStream(clSocket.getOutputStream());
			}
			catch (IOException e)
			{
				System.out.println("error in I/O socket");
			}
		} while (clOIn == null || clOOut == null);
		inter = new ImplementerTCP(clOOut, clOIn);
	}

	@Override
	public void run()
	{
		Player     player       = null;
		String     errorMessage = "Insert your username max 15 characters min 3 characters: (no space)";
		String     instruction;
		String     name;
		boolean    error;
		int        gameId       = 0;
		GameThread gt;
		try
		{
			do
			{
				do
				{
					name = inter.loginRequest(errorMessage);
				} while (name == null || name.length() > 15 || name.length() < 4 || name.contains(" "));
				try
				{
					player = lobbyManager.createPlayer(name);
				}
				catch (NicknameTakenException e)
				{
					errorMessage = "Nickname already taken, retry:";
				}
				catch (NullNicknameException e)
				{
					errorMessage = "Nickname not inserted, retry:";
				}

			} while (player == null);

			inter.setClientUsername(name);

			if (player.getGame() != null) //disconnection
			{
				System.out.println("ENTRATO");
				gt = getGameThreadFromGameId(player.getGame().getGameID());
				inter.finalizeInitialization(gt, player, true);
				inter.infoMessage("You have been reconnected to your previous game");
				return;
			}

			boolean choice = false;
			do
			{

				errorMessage = "What do you want to do?\n1) Create a game\n2) Join a game";

				do
				{
					instruction = inter.loginRequest(errorMessage);
					if (instruction != null)
						if (!instruction.equals("1") && !instruction.equals("2"))
						{
							errorMessage = "Your input is not valid. Retry:\n1) Create a game\n2)Join a game";
						}

				} while (instruction == null || (!instruction.equals("1") && !instruction.equals("2")));

				if (instruction.equals("1")) //CREATE A GAME----------------------------------------------------------------------------------------------------
				{
					errorMessage = "To create a game specify the number of players that will participate (from 2 to 4) [type 'e' to go back in the menu]:";
					error = false;
					do
					{

						instruction = inter.loginRequest(errorMessage);

						if (instruction != null)
						{
							if (instruction.equalsIgnoreCase("e"))
							{
								choice = false;
								break;
							}
							int x = 0;
							try
							{
								x = Integer.parseInt(instruction);
								error = false;
							}
							catch (NumberFormatException e)
							{
								errorMessage = "Your input is not valid. Retry:\nFrom 2 to 4 players.";
								error = true;
							}
							if (!error)
							{
								try
								{
									gameId = lobbyManager.createNewGame(x, player);
									error = false;
									choice = true;
								}
								catch (NumOfPlayersException e)
								{
									errorMessage = "Your input is not valid. Retry:\nFrom 2 to 4 players.";
									error = true;
								}
							}
						}
					} while (error || instruction == null);
					if (choice)
					{
						gt = new GameThread(player, gameId, Integer.parseInt(instruction));
						lobbyManager.addGameThread(gt);
						gt.start();
						errorMessage = "You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameId;
					}
				}
				else //JOIN A GAME
				{
					errorMessage = "To join a game specify its GameId number [type 'e' to go back in the menu]:";
					error = false;
					do
					{
						instruction = inter.loginRequest(errorMessage);
						if (instruction != null)
						{

							if (instruction.equalsIgnoreCase("e"))
							{
								choice = false;
								break;
							}
							try
							{
								lobbyManager.joinGame(Integer.parseInt(instruction), player);
								error = false;
								choice = true;
							}
							catch (NumOfPlayersException e)
							{
								errorMessage = e.getMessage() + " The game you are trying to connect is full. Retry";
								error = true;
							}
							catch (GameNotFoundException e)
							{
								errorMessage = e.getMessage() + " Insert the IdGame you or your friend have exposed on the screen. Retry [e to go back to menu]:";
								error = true;
							}
							catch (NumberFormatException e)
							{
								errorMessage = "The argument you have given is not a number please retry";
								error = true;
							}
						}
					} while (error || instruction == null);
					if (choice)
						errorMessage = "You joined a game successfully. Have fun!";
				}
			} while (!choice);
			inter.infoMessage(errorMessage);
			gt = getGameThreadFromGameId(player.getGame().getGameID());
			inter.finalizeInitialization(gt, player, false);
		}
		catch (NoSuchElementException | ClassNotFoundException | IOException e)
		{
			System.out.println("disconnected post nick");

		}
	}

	private GameThread getGameThreadFromGameId(int gameId)
	{

		for (GameThread gt : lobbyManager.getGameThreadList())
		{
			if (gt.getGame().getGameID() == gameId)
				return gt;
		}
		throw new NoSuchElementException("Impossible");
	}
}