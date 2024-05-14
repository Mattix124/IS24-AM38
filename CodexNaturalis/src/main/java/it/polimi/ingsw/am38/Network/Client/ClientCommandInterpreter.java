package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
	private static final long serialVersionUID = 4469759083013548722L;
	private Player player;
	private String nickname;
	private LinkedList <String> playersNicknames;
	private ClientInterface clientInterface;
	private ObjectOutputStream objectOut;
	boolean connectionType;
	private Turnings turnings = STANDBY;

	/**
	 * Constructor for TCP clients
	 *
	 * @param objectOut
	 */
	public ClientCommandInterpreter(ObjectOutputStream objectOut)
	{
		this.objectOut = objectOut;
		this.connectionType = true; //indica connessione tcp
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
	}

	public void setPlayersNicknames(Player player, LinkedList <String> playersNicknames)
	{
		this.playersNicknames = playersNicknames;
		this.player = player;
		this.nickname = player.getNickname();
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
		if (command.equals("help"))
		{
			//clear cli
			System.out.println("Here is a list of your commands:\n");
			System.out.println("██ALWAYS AVAILABLE██");
			System.out.println("-Chatb 'your message' : the message you write will be send to all the other players in the game.");
			System.out.println("-Chatp 'player nickname' 'your message' : the message you write will be send to the player with the nickname given.");
			System.out.println("-ShowCard 'x' 'y' : if the card in the coordinates given exists it will be displayed to have more details");
			System.out.println("-ShowField 'player nickname' : the field of the player with given nickname will be displayed");
			System.out.println("-Placement : the field will show you where you place your cards (face down)");
			System.out.println("██ONLY IN YOUR TURN██");
			System.out.println("-Play 'hand slot (number)' 'x' 'y' 'face' place (if possible) the card in the coordinates given the facing allowed are up or down");
			System.out.println("-Draw 'origin' 'n': draw a card from the origin chosen ('resource' or 'gold'), n specifies the location: 0 from deck, 1 from the first ground card, 2 for the second ground card");
		}
		if (turnings != CHOOSE1 && turnings != CHOOSE2 && turnings != CHOOSE3 && turnings != STANDBY)
		{
			switch (tokens[0])
			{

				case "all" ->
				{//No control over the commands syntax
					StringBuilder text = new StringBuilder();
					for (int i = 1 ; i < tokens.length ; i++)
						text.append(tokens[i] + " ");

					if (connectionType)
					{ //Tcp
						objectOut.writeObject(new Message(CHAT, BCHAT, nickname, new MSimpleString(text)));
					}
					else
					{//RmiImplementation
						//non so come funziona la chat
					}
				}
				case "w" ->
				{
					StringBuilder text = new StringBuilder();
					if (playersNicknames.contains(tokens[1]) && !tokens[1].equals(nickname))
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
						objectOut.writeObject(new Message(CHAT, PCHAT, nickname, new MPrivateChat(tokens[1], text)));
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
							objectOut.writeObject(new Message(VIEWUPDATE, SHOWCARD, nickname, new MCoords(x, y)));
							//SEND AGGIORNAMENTO
							//TCP CLI UPDATE
						}
						else
						{//RmiImplementation
							//non so ancora cosa fare
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

						if (!playersNicknames.contains(tokens[1]))
						{
							System.out.println("The player you specified is not present, please retry");
						}
						else
						{
							if (connectionType)
							{
								objectOut.writeObject(new Message(VIEWUPDATE, SHOWFIELD, nickname, new MSimpleString(tokens[1])));
								//TcpCLI UPdate
							}
							else
							{
								//RmiImplementation
								//scrivere metodo
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
							if (!tokens[4].equals("up") && !tokens[4].equals("down"))
							{
								System.out.println("The face argument you are giving are not 'up' or 'down' please try again");
								return;
							}

							if (connectionType)
							{
								objectOut.writeObject(new Message(GAME, PLAYCARD, nickname, new MPlayCard(index, new Coords(x, y), tokens[4])));
							}
							else
							{//RmiImplementation
								try
								{
									clientInterface.playACard(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3], nickname); //call the method on the client interface that send the info to the server interface
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
								objectOut.writeObject(new Message(GAME, DRAWCARD, nickname, new MDrawCard(tokens[1], x)));
							}
							else
							{//RmiImplementation
								try
								{
									clientInterface.draw(nickname, tokens[0], Integer.parseInt(tokens[1])); //call the method on the client interface that send the info to the server interface
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
					System.out.println("Unknown command: " + tokens[0] + ", try: 'help'");
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

							if (connectionType)
							{
								objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE,nickname, new MSimpleString(tokens[1])));
								//da dare alla cli
							}
							else
							{
								clientInterface.chooseFaceStarterCard(nickname, "up");
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
						objectOut.writeObject(new Message(GAME, COLORCHOICE,nickname, new MSimpleString(tokens[1])));					}
					else
					{
						try
						{
							clientInterface.chooseColor(nickname, "red");
							turnings = STANDBY;
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
						System.out.println("The arguments you are giving are not numbers please try again");
						return;
					}
					if (x > 2 || x < 1)
					{
						System.out.println("The objective you chose not exists, please try again");
						return;
					}
					if (connectionType)
					{
						//tcp
					}
					else
					{
						try
						{
							clientInterface.chooseObjectiveCard(nickname, 1);
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

	public void setTurning(Turnings turnings)
	{
		this.turnings = turnings;
	}
	public Turnings getTurnings()
	{
		return turnings;
	}
}
