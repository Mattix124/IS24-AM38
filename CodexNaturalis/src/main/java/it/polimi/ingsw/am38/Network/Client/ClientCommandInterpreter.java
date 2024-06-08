package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Server.Turnings;
import it.polimi.ingsw.am38.View.CLI;

import java.io.IOException;

import static it.polimi.ingsw.am38.Network.Server.Turnings.*;

/**
 * This class parse the input to let the server do what's needed based on the type of
 * connection of the client
 */
public class ClientCommandInterpreter
{
	/**
	 * Instance of ClientData presents on this client
	 */
	private final ClientDATA clientData;
	/**
	 * Instance of clientInterface needed for RMI implementation
	 */
	private CommonClientInterface inter;
	/**
	 * Attribute that allow the scanning of the phases of the games
	 */
	private Turnings turnings = STANDBY;
	/**
	 * The attribute that contains the CLI of the client
	 */
	private final CLI cli = new CLI();

	/**
	 * Constructor of ClientCommandInterpreter
	 *
	 * @param inter
	 */
	public ClientCommandInterpreter(CommonClientInterface inter)
	{
		this.inter = inter;
		this.clientData = ClientDATA.getClientDATA();
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
		String[] tokens = command.split(" ");
		if (command.equals("reconnect"))
		{
			CLIENTSTARTER.disconnectionHappenedSetter();
			return;
		}
		if(command.equals("exit"))
		{
			CLIENTSTARTER.quit();
		}

		if (turnings != CHOOSE1 && turnings != CHOOSE2 && turnings != CHOOSE3 && turnings != STANDBY)
		{
			if (command.equals("help"))
			{
				cli.printHelpBox();
			}
			else
			{
				switch (tokens[0])
				{

					case "all" ->
					{//No control over the commands syntax
						if (!(tokens.length > 1))
						{
							System.out.println("You have to write a message!");
							return;
						}

						StringBuilder text = new StringBuilder();
						for (int i = 1 ; i < tokens.length ; i++)
							text.append(tokens[i] + " ");

						inter.broadcastMessage(text);

					}
					case "w" ->
					{
						if (!(tokens.length > 2))
						{
							System.out.println("You have to write an addressee and a message!");
							return;
						}
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
						inter.privateMessage(tokens[1], text);
					}

					case "show" ->
					{
					/*	if (tokens.length != 3)
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
						}
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
							//clientInterface.showCard(clientData.getNickname(), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), gameID);
						}*/
					}

					case "showfield" ->
					{
					/*	if (tokens.length != 2)
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
						}
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
								//clientInterface.showField(clientData.getNickname(), tokens[1], gameID);
								//CLI update
							}
						}
*/
					}

					case "play" ->
					{
						if (turnings != PLAYPHASE)
						{
							System.out.println("You can't play right now");
							return;
						}
						if (tokens.length != 5)
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
							return;
						}
						if (!tokens[4].equals("up") && !tokens[4].equals("down"))
						{
							System.out.println("The face argument you are giving are not 'up' or 'down' please try again");
							return;
						}

						int index;
						int tmpX;
						int tmpY;
						int x;
						int y;

						try
						{
							index = Integer.parseInt(tokens[1]);
							tmpX = Integer.parseInt(tokens[2]); //coords to give to the ClientDATA if the placement is successful
							tmpY = Integer.parseInt(tokens[3]); //coords to give to the ClientDATA if the placement is successful
						}
						catch (NumberFormatException e)
						{
							System.out.println("The arguments you are giving are not numbers please try again");
							return;
						}
						if ((tmpX + tmpY) % 2 != 0)
						{
							System.out.println("Invalid placement: please choose coordinates with an even sum (YES zero is EVEN!)");
							return;
						}
						if (index > 3 || index < 1)
						{
							System.out.println("The index argument you are giving is not 1,2 or 3 please try again");
							return;
						}
						x = (tmpX + tmpY) / 2; //translates input coords to dataStruct Coords
						y = (tmpY - tmpX) / 2; //translates input coords to dataStruct Coords
						boolean b;
						b = tokens[4].equals("up");

						inter.playACard(index, x, y, b);

					}

					case "draw" ->
					{
						if (turnings != DRAWPHASE)
						{
							System.out.println("You can't draw right now!");
							return;
						}
						if (tokens.length != 3)
						{
							System.out.println("The command you insert has some syntax error, try 'help'.");
							return;
						}
						if (!tokens[1].equals("resource") && !tokens[1].equals("gold"))
						{
							System.out.println("The arguments you are giving are not resource or gold, please try again");
							return;
						}
						int x;

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

						inter.draw(tokens[1], x); //call the method on the client interface that send the info to the server interface
					}

					default -> System.out.println("Unknown command: " + tokens[0] + ", try: 'help' ");
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
					if (tokens.length != 2)
					{
						System.out.println("The command you insert has some syntax error, try 'help'.");
						return;
					}
					if (!tokens[1].equals("up") && !tokens[1].equals("down"))
					{
						System.out.println("The face you chose not exists, please try again");
						return;
					}
					String b;
					if (tokens[1].equals("up"))
						b = "true";
					else
						b = "false";

					inter.chooseFaceStarterCard(b);
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

					inter.chooseColor(tokens[1]);
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
						return;
					}
					int x;
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

					inter.chooseObjectiveCard(tokens[1]);
					getClientData().setPersonalObjectiveChosen(tokens[1]);
					getCLI().setPersonalObjective(getClientData().getPersonalObjective().getDescription());
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

	public CommonClientInterface getInterface()
	{
		return inter;
	}

	/**
	 * Get the Cli of this client
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

