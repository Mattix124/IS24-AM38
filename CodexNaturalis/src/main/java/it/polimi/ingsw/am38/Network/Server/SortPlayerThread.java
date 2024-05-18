package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SortPlayerThread implements Runnable
{
	final private Socket clSocket;
	private final LobbyManager lobbyManager = LobbyManager.getLobbyManager();
	private ObjectOutputStream clOOut;
	private PrintWriter clOut;
	private Scanner clIn;
	private ObjectInputStream clOIn;

	public SortPlayerThread(Socket clSocket)
	{
		this.clSocket = clSocket;
		do
		{
			try
			{
				this.clOut = new PrintWriter(clSocket.getOutputStream(), true);
				this.clIn = new Scanner(clSocket.getInputStream());
				this.clOIn = new ObjectInputStream(clSocket.getInputStream());
				this.clOOut = new ObjectOutputStream(clSocket.getOutputStream());
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		} while (clIn == null || clOut == null);
	}

	@Override
	public void run()
	{
		Player     player       = null;
		String     errorMessage = "Insert your username max 15 character:";
		String     instruction;
		String     name;
		boolean    error;
		int        gameId       = 0;
		GameThread gt;

		do
		{
			do
			{
				clOut.println(errorMessage);
				name = clIn.nextLine();
			} while (name.length() > 15);
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

		if (player.isPlaying())
		{
			clOut.println("You have been reconnected to your previous game");
			gt = getGameThreadFromGameId(player.getGame().getGameID());
			ClientListener clGH     = new ClientListener(clOIn, gt.getServerInterpreter());
			Thread         listener = new Thread(clGH);
			listener.start();
			return;
		}
		errorMessage = "What do you want to do?\n1) Create a game\n2) Join a game";

		do
		{
			clOut.println(errorMessage);
			instruction = clIn.nextLine();

			if (!instruction.equals("1") && !instruction.equals("2"))
			{
				errorMessage = "Your input is not valid. Retry:\n1) Create a game\n2)Join a game";
			}

		} while (!instruction.equals("1") && !instruction.equals("2"));

		if (instruction.equals("1")) //CREATE A GAME----------------------------------------------------------------------------------------------------
		{
			errorMessage = "To create a game specify the number of players that will participate (from 2 to 4):";
			do
			{
				clOut.println(errorMessage);
				instruction = clIn.nextLine();

				try
				{
					gameId = lobbyManager.createNewGame(Integer.parseInt(instruction), player);
					error = false;
				}
				catch (NumOfPlayersException e)
				{
					errorMessage = "Your input is not valid. Retry:\nFrom 2 to 4 players.";
					error = true;
				}
			} while (error);
			gt = new GameThread(player, gameId, Integer.parseInt(instruction));
			lobbyManager.addGameThread(gt);
			gt.start();
			errorMessage = "You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameId;
		}
		else //JOIN A GAME
		{
			errorMessage = "To join a game specify its GameId number:";
			do
			{
				clOut.println(errorMessage);
				instruction = clIn.nextLine();

				try
				{
					lobbyManager.joinGame(Integer.parseInt(instruction), player);
					error = false;
				}
				catch (NumOfPlayersException e)
				{
					errorMessage = e.getMessage() + " The game you are trying to connect is full. Retry";
					error = true;
				}
				catch (GameNotFoundException e)
				{
					errorMessage = e.getMessage() + " Insert the IdGame you or your friend have exposed on the screen. Retry:";
					error = true;
				}
			} while (error);
			errorMessage = "You joined a game successfully. Have fun!";
		}
		clOut.println(errorMessage + "\nWaiting for other players...");
		gt = getGameThreadFromGameId(player.getGame().getGameID());
		clIn.reset();
		ClientListener clGH     = new ClientListener(clOIn, gt.getServerInterpreter());
		Thread         listener = new Thread(clGH);
		listener.start();
		gt.addEntry(listener, clOOut, player, true, null);
		clOut.println("ends");
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