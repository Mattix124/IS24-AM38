package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Server.Turnings;
import it.polimi.ingsw.am38.View.CLI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;
import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * This class parse the input to let the server do what's needed based on the type of
 * connection of the client
 */
public class ClientCommandInterpreter implements Serializable
{
	/**
	 * Serializable obligatory attribute
	 */
	@Serial
	private static final long serialVersionUID = 4469759083013548722L;
	/**
	 * Instance of ClientData presents on this client
	 */
	private ClientDATA clientData;
	/**
	 * Instance of clientInterface needed for RMI implementation
	 */
	private ClientInterface clientInterface;
	/**
	 * Attribute that permits the tcp client to communicate
	 */
	private ObjectOutputStream objectOut;
	/**
	 * Connection type
	 */
	boolean connectionType;
	/**
	 * Attribute that contain the gameId
	 */
	int gameID;
	/**
	 * Attribute that allow the scanning of the phases of the games
	 */
	private Turnings turnings = STANDBY;
	/**
	 * The attribute that contains the CLI of the client
	 */
	private final CLI cli = new CLI();

	/**
	 * Constructor for TCP clients
	 *
	 * @param objectOut
	 */
	public ClientCommandInterpreter(ObjectOutputStream objectOut)
	{
		this.objectOut = objectOut;
		this.connectionType = true; //indica connessione tcp
		this.clientData = new ClientDATA();
		//servertype

		//player e player list (per chat e show vari)
	}

	/**
	 * Constructor for RMI clients
	 *
	 * @param clientInterface
	 */
	public ClientCommandInterpreter(ClientInterface clientInterface)
	{
		this.clientInterface = clientInterface;
		this.connectionType = false; //indica connessione rmi
		this.clientData = new ClientDATA();
	}

	/**
	 * This method parse the input given and based on the connection type of the associated thread do
	 * what the player wants
	 *
	 * @param command is the input from command line
	 * @throws IOException
	 */
	public void checkCommand(String command) throws IOException
	{
		command = command.toLowerCase();
		String[] tokens = command.split(" ");

		if (turnings != CHOOSE1 && turnings != CHOOSE2 && turnings != CHOOSE3 && turnings != STANDBY)
		{
			if (command.equals("help"))
			{
				cli.printHelpBox();
			}
			else
				switch (tokens[0])
				{

					case "all" ->
					{//No control over the commands syntax
						StringBuilder text = new StringBuilder();
						for (int i = 1 ; i < tokens.length ; i++)
							text.append(tokens[i] + " ");

						if (connectionType)
						{ //Tcp
							objectOut.writeObject(new Message(CHAT, BCHAT, clientData.getNickname(), new MSimpleString(text)));
						}
						else
						{//RmiImplementation
							//non so come funziona la chat
						}
					}
					case "w" ->
					{
						StringBuilder text = new StringBuilder();
						if (clientData.getPlayersNicknames().contains(tokens[1]) && !tokens[1].equals(clientData.getNickname()))
						{
							for (int i = 2 ; i < tokens.length ; i++)
								text.append(tokens[i]).append(" ");
						}
						else
						{
							System.out.println("The nickname you specified is not present, please retry");
							return;
						}

						if (connectionType)
						{ //Tcp
							objectOut.writeObject(new Message(CHAT, PCHAT, clientData.getNickname(), new MPrivateChat(tokens[1], text)));
						}
						else
						{//RmiImplementation
							//non so come funziona la chat
						}
					}

					case "showcard" ->
					{
						if (tokens.length == 3)
						{

							int x = 0;
							int y = 0;
							try
							{
								x = Integer.parseInt(tokens[1]);
								y = Integer.parseInt(tokens[2]);
							}
							catch (NumberFormatException e)
							{
								System.out.println("The arguments you are giving are not numbers please try again");
								return;
							}
							//
							if (connectionType)
							{
								objectOut.writeObject(new Message(VIEWUPDATE, SHOWCARD, clientData.getNickname(), new MCoords(x, y)));
								//SEND AGGIORNAMENTO
								//TCP CLI UPDATE
							}
							else
							{//RmiImplementation
								clientInterface.showCard(clientData.getNickname(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), gameID);
							}

						}
						else
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
						}
					}

					case "showfield" ->
					{
						if (tokens.length == 2)
						{

							if (!clientData.getPlayersNicknames().contains(tokens[1]))
							{
								System.out.println("The player you specified is not present, please retry");
							}
							else
							{
								if (connectionType)
								{
									objectOut.writeObject(new Message(VIEWUPDATE, SHOWFIELD, clientData.getNickname(), new MSimpleString(tokens[1])));
									//TcpCLI UPdate
								}
								else
								{
									clientInterface.showField(clientData.getNickname(), tokens[1], gameID);
									//CLI update
								}
							}
						}
						else
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
						}

					}
/*
				case "placement" -> //BOH
				{
					if (tokens.length == 1)
					{
						if (connectionType)
						{ //Tcp
							objectOut.writeObject(new Message(VIEWUPDATE, PLACEMENT, nickname, null));
						}
						else
						{//RmiImplementation
							//scrivere metodo per ottenere i placement
						}
					}
					else
					{
						System.out.println("The command you insert has some syntax error, try 'help'.");
					}
				}
*/
					case "play" ->
					{
						if (turnings == PLAYPHASE)
						{
							if (tokens.length == 5)
							{

								int index = 0;
								int x     = 0;
								int y     = 0;
								try
								{
									index = Integer.parseInt(tokens[1]);
									x = Integer.parseInt(tokens[2]);
									y = Integer.parseInt(tokens[3]);
								}
								catch (NumberFormatException e)
								{
									System.out.println("The arguments you are giving are not numbers please try again");
									return;
								}
								if (index > 3 || index < 1)
								{
									System.out.println("The index argument you are giving is not 1,2 or 3 please try again");
									return;
								}
								if (!tokens[4].equals("up") && !tokens[4].equals("down"))
								{
									System.out.println("The face argument you are giving are not 'up' or 'down' please try again");
									return;
								}
								boolean b;
								if (tokens[4].equals("up"))
									b = true;
								else
									b = false;
								if (connectionType)
								{
									objectOut.writeObject(new Message(GAME, PLAYCARD, clientData.getNickname(), new MPlayCard(index, new Coords(x, y), b)));
								}
								else
								{//RmiImplementation
									try
									{
										clientInterface.playACard(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), b, clientData.getNickname(), gameID); //call the method on the client interface that send the info to the server interface
									}
									catch (NoPossiblePlacement e)
									{
										throw new RuntimeException(e);
									}
									catch (InvalidInputException e)
									{
										throw new RuntimeException(e);
									}
									catch (NotPlaceableException e)
									{
										throw new RuntimeException(e);
									}
								}
							}
							else
							{
								System.out.println("The command you insert has some syntax error, try 'help'.");
							}
						}
						else
							System.out.println("You can't play right now wait your turn!");
					}

					case "draw" ->
					{
						if (turnings == DRAWPHASE)
						{
							if (tokens.length == 3)
							{
								if (!tokens[1].equals("resource") && !tokens[1].equals("gold"))
								{
									System.out.println("The arguments you are giving are not resource or gold, please try again");
									return;
								}
								int x = 0;

								try
								{
									x = Integer.parseInt(tokens[2]);
								}
								catch (NumberFormatException e)
								{
									System.out.println("The arguments you are giving are not numbers please try again");
									return;
								}
								if (x > 2 || x < 0)
								{
									System.out.println("The location you chose not exists, please try again");
									return;
								}

								if (connectionType)
								{ //Tcp
									objectOut.writeObject(new Message(GAME, DRAWCARD, clientData.getNickname(), new MDrawCard(tokens[1], x)));
								}
								else
								{//RmiImplementation
									try
									{
										clientInterface.draw(clientData.getNickname(), tokens[0], Integer.parseInt(tokens[1]), gameID); //call the method on the client interface that send the info to the server interface
									}
									catch (EmptyDeckException e)
									{
										throw new RuntimeException(e);
									}
									catch (GameNotFoundException e)
									{
										throw new RuntimeException(e);
									}
									catch (InvalidInputException e)
									{
										throw new RuntimeException(e);
									}
								}
							}
							else
							{
								System.out.println("The command you insert has some syntax error, try 'help'.");
							}
						}
						else
						{
							System.out.println("You can't draw right now wait your turn!");
						}
					}
					default ->
					{
						System.out.println("Unknown command: " + tokens[0] + ", try: 'help' ");
					}
				}
		}
		else if (turnings != STANDBY)
		{
			switch (tokens[0])
			{

				case "face" ->
				{
					if (turnings != CHOOSE1)
					{
						System.out.println("No such command: " + tokens[0] + ", try: 'help'");
						return;
					}
					if (tokens.length == 2)
					{
						if (tokens[1].equals("up") || tokens[1].equals("down"))
						{
							String b;
							if (tokens[1].equals("up"))
								b = "true";
							else
								b = "false";

							if (connectionType)
							{
								objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE, clientData.getNickname(), new MSimpleString(b)));
							}
							else
							{
								clientInterface.chooseFaceStarterCard(clientData.getNickname(), b, gameID);
								System.out.println("Choose a color for your pawn (blue, red, yellow, green)\n");
								turnings = CHOOSE2;
							}
						}
						else
						{
							System.out.println("The face you chose not exists, please try again");
						}
					}
					else
					{
						System.out.println("The command you insert has some syntax error, try 'help'.");
					}

				}
				case "color" ->
				{
					if (turnings != CHOOSE2)
					{
						System.out.println("No such command: " + tokens[0] + ", try: 'help'");
						return;
					}
					if (tokens.length != 2)
					{
						System.out.println("The command you insert has some syntax error, try 'help'.");
						return;
					}
					if (!tokens[1].equals("red") && !tokens[1].equals("blue") && !tokens[1].equals("green") && !tokens[1].equals("yellow"))
					{
						System.out.println("The color you chose not exists, please try again");
						return;
					}
					if (connectionType)
					{
						objectOut.writeObject(new Message(GAME, COLORCHOICE, clientData.getNickname(), new MSimpleString(tokens[1])));
					}
					else
					{
						try
						{
							clientInterface.chooseColor(clientData.getNickname(), tokens[1], gameID);
							turnings = STANDBY;
							System.out.println("Wait for other players...");
						}
						catch (ColorTakenException e)
						{
							throw new RuntimeException(e);
						}
					}
				}

				case "obj" ->
				{
					if (turnings != CHOOSE3)
					{
						System.out.println("No such command: " + tokens[0] + ", try: 'help'");
						return;
					}
					if (tokens.length != 2)
					{
						System.out.println("The command you insert has some syntax error, try 'help'.");
					}
					int x = 0;
					try
					{
						x = Integer.parseInt(tokens[1]);
					}
					catch (NumberFormatException e)
					{
						System.out.println("The arguments you are giving is not a number please try again");
						return;
					}
					if (x > 2 || x < 1)
					{
						System.out.println("The objective you chose not exists, please try again");
						return;
					}
					if (connectionType)
					{
						objectOut.writeObject(new Message(GAME, OBJECTIVECHOICE, clientData.getNickname(), new MSimpleString(tokens[1])));
						getCLI().setPersonalObjective(tokens[1]);
					}
					else
					{
						try
						{
							clientInterface.chooseObjectiveCard(clientData.getNickname(), tokens[1], gameID);
							turnings = STANDBY;
						}
						catch (InvalidInputException e)
						{
							throw new RuntimeException(e);
						}
					}
				}
				default -> System.out.println("Unknown command: " + tokens[0] + ", try: 'help'");

			}
		}

	}

	/**
	 * This method allows the control of which command a client can use in a phase of the game
	 *
	 * @param turnings
	 */
	public void setTurning(Turnings turnings)
	{
		this.turnings = turnings;
	}

	/**
	 * Used to set the list of username of all the players of the game and the nickname of the player
	 *
	 * @param playersNicknames list of all nicknames
	 * @param nickname         the nickname of the client using this class
	 */
	public void setPlayersNicknames(LinkedList <String> playersNicknames, String nickname)
	{
		this.clientData.setPlayersNicknames(playersNicknames);
		this.clientData.setNickname(nickname);
	}

	/**
	 * Used to set the gameId of the game the player is in
	 *
	 * @param gameID the gameId
	 */
	public void setGameID(int gameID)
	{
		this.gameID = gameID;
	}

	/**
	 * Get the Cli of this clietn
	 *
	 * @return the cli
	 */
	public CLI getCLI()
	{
		return this.cli;
	}

	/**
	 * Get the class clientData
	 *
	 * @return the clientData Class
	 */
	public ClientDATA getClientData()
	{
		return clientData;
	}
}

