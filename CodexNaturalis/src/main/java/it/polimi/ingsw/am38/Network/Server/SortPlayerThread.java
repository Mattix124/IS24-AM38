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
		System.out.println("ciao");
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
		String     errorMessage = "Insert";
		String     instruction;
		String     name;
		boolean    error;
		int        gameId       = 0;
		GameThread gt;
		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		try
		{
			do
			{
				do
				{
					name = inter.loginRequest(errorMessage);
					System.out.println(name);
				} while (name.length() > 15 || name.length() < 4 || name.contains(" "));
				try
				{
					player = lobbyManager.createPlayer(name);
				}
				catch (NicknameTakenException e)
				{
					errorMessage = "Taken";
				}
				catch (NullNicknameException e)
				{
					errorMessage = "NotIn";
				}

			} while (player == null);

			inter.setClientUsername(name);

			if (player.getGame() != null) //disconnection
			{
				gt = getGameThreadFromGameId(player.getGame().getGameID());
				inter.finalizeInitialization(gt, player, true);
				inter.enterGame("You have been reconnected to your game!");
				return;
			}

			boolean choice = false;
			do
			{

				errorMessage = "What";

				do
				{
					instruction = inter.loginRequest(errorMessage);
					if (instruction != null)
						if (!instruction.equals("1") && !instruction.equals("2"))
						{
							errorMessage = "NotValidWhat";
						}

				} while (instruction == null || (!instruction.equals("1") && !instruction.equals("2")));

				if (instruction.equals("1")) //CREATE A GAME----------------------------------------------------------------------------------------------------
				{
					errorMessage = "Create";
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
								errorMessage = "NotValidCreate";
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
									errorMessage = "NotValidCreate";
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
					errorMessage = "Join";
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
								errorMessage = "Full";
								error = true;
							}
							catch (GameNotFoundException e)
							{
								errorMessage = "NotFound";
								error = true;
							}
							catch (NumberFormatException e)
							{
								errorMessage = "NotNumber";
								error = true;
							}
						}
					} while (error || instruction == null);
					if (choice)
						errorMessage = "You joined a game successfully. Have fun!";
				}
			} while (!choice);
			inter.display(errorMessage);
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