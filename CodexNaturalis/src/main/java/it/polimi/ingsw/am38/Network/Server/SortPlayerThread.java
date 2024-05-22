package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.CONNECTION;
import static it.polimi.ingsw.am38.Network.Packet.Scope.VIEWUPDATE;

public class SortPlayerThread implements Runnable
{

	private final LobbyManager lobbyManager = LobbyManager.getLobbyManager();
	private ObjectOutputStream clOOut;
	private PrintWriter clOut;
	private Scanner clIn;
	private ObjectInputStream clOIn;

	public SortPlayerThread(Socket clSocket)
	{
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
		try
		{
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
		}
		catch (NoSuchElementException e)
		{
			return;
		}
		clOut.println("/username "+name);

		if (player.isPlaying())
		{
			clOut.println("You have been reconnected to your previous game");
			gt = getGameThreadFromGameId(player.getGame().getGameID());
			ClientListener clGH = new ClientListener(clOIn, gt.getServerInterpreter(), player);
			gt.getServerInterpreter().addMessage(new Message(VIEWUPDATE, CONNECTION, player.getNickname(), null)); //RESEND ALL THE INFO
			Thread listener = new Thread(clGH);
			listener.start();
			clOut.println("ends");
			return;
		}
		String nick =  player.getNickname();
		try
		{
			boolean choice = false;
			do
			{

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
					errorMessage = "To create a game specify the number of players that will participate (from 2 to 4) [type 'e' to go back in the menu]:";
					do
					{
						clOut.println(errorMessage);
						instruction = clIn.nextLine();

						if (instruction.equalsIgnoreCase("e"))
						{
							choice = false;
							break;
						}
						try
						{
							int x = Integer.parseInt(instruction);
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
								gameId = lobbyManager.createNewGame(Integer.parseInt(instruction), player);
								error = false;
								choice = true;
							}
							catch (NumOfPlayersException e)
							{
								errorMessage = "Your input is not valid. Retry:\nFrom 2 to 4 players.";
								error = true;
							}
						}
					} while (error);
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

					do
					{
						clOut.println(errorMessage);
						instruction = clIn.nextLine();
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
							errorMessage = e.getMessage() + "The argument you have given is not a number please retry";
							error = true;
						}
					} while (error);
					if (choice)
						errorMessage = "You joined a game successfully. Have fun!";
				}
			} while (!choice);
			clOut.println(errorMessage + "\nWaiting for other players...");
			gt = getGameThreadFromGameId(player.getGame().getGameID());
			clIn.reset();
			ClientListener clGH     = new ClientListener(clOIn, gt.getServerInterpreter(), player);
			Thread         listener = new Thread(clGH);
			listener.start();
			gt.addEntry(listener, clOOut, player);
			clOut.println("ends");
		}
		catch (NoSuchElementException e)
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