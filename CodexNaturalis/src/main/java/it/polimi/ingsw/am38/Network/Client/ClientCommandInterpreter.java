package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Server.Turnings;
import it.polimi.ingsw.am38.View.Viewable;

import java.rmi.RemoteException;
import java.util.LinkedList;

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

	private Viewable viewInterface;

	/**
	 * Constructor of ClientCommandInterpreter
	 *
	 * @param inter
	 */
	public ClientCommandInterpreter(CommonClientInterface inter, Viewable viewInterface)
	{
		this.inter = inter;
		this.clientData = ClientDATA.getClientDATA();
		this.viewInterface = viewInterface;

	}
	/**
	 * This method parse the input given and based on the connection type of the associated thread do
	 * what the player wants
	 *
	 * @param command is the input from command line
	 */
	public void checkCommand(String command)
	{
		try
		{

			String[] tokens = command.split(" ");
			if (command.equals("reconnect"))
			{
				CLIENTSTARTER.disconnectionHappenedSetter();
				return;
			}
			if (command.equals("exit"))
			{
				CLIENTSTARTER.quit();
			}

			if (turnings != CHOOSE1 && turnings != CHOOSE2 && turnings != CHOOSE3 && turnings != STANDBY)
			{
				if (command.equals("help"))
					viewInterface.printHelp();
				else
				{
					switch (tokens[0])
					{

						case "all" ->
						{//No control over the commands syntax
							if (!(tokens.length > 1))
							{
								getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
								return;
							}

							StringBuilder text = new StringBuilder();
							for (int i = 1 ; i < tokens.length ; i++)
								text.append(tokens[i] + " ");

							inter.broadcastMessage(text);
							viewInterface.receiveOwnMessage(String.valueOf(text));

						}
						case "w" ->
						{
							if (!(tokens.length > 2))
							{
								viewInterface.priorityString("You have to write an addressee and a message!", 2);
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
								viewInterface.priorityString("The nickname you specified is not present, please retry", 2);
								return;
							}
							inter.privateMessage(tokens[1], text);
							viewInterface.receiveOwnMessage("whispered to " + tokens[1] + " " + text);
						}

						case "show" ->
						{
							if (tokens.length != 3)
							{
								System.out.println("The command you insert has some syntax error, try 'help'.");
							}
							int x;
							int y;
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

							viewInterface.setCardDisplay(clientData.getCardFromPlayerField("onScreenForRealForReal", x, y), x, y);
						}

						case "showfield" ->
						{
							if (tokens.length != 2)
							{
								getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
								return;
							}
							if (!clientData.getPlayersNicknames().contains(tokens[1]))
							{

								getViewInterface().priorityString("The player you specified is not present, please retry", 0);
								return;
							}
							viewInterface.showPlayerField(tokens[1]);
						}

						case "play" ->
						{
							if (turnings != PLAYPHASE)
							{
								getViewInterface().priorityString("You can't play right now", 1);
								return;
							}
							if (tokens.length != 5)
							{
								getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
								return;
							}
							if (!tokens[4].equals("up") && !tokens[4].equals("down"))
							{
								getViewInterface().priorityString("The face argument you are giving are not 'up' or 'down' please try again", 0);
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
								getViewInterface().priorityString("The arguments you are giving are not numbers please try again", 0);
								return;
							}
							if ((tmpX + tmpY) % 2 != 0)
							{
								getViewInterface().priorityString("Invalid placement: please choose coordinates with an even sum (YES zero is EVEN!)", 1);
								return;
							}
							if (index > 2 || index < 0)
							{
								getViewInterface().priorityString("The index argument you are giving is not 1,2 or 3 please try again", 0);
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
								getViewInterface().priorityString("You can't draw right now!", 1);
								return;
							}
							if (tokens.length != 3)
							{
								getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
								return;
							}
							if (!tokens[1].equals("resource") && !tokens[1].equals("gold"))
							{
								getViewInterface().priorityString("The arguments you are giving are not resource or gold, please try again", 0);
								return;
							}
							int x;

							try
							{
								x = Integer.parseInt(tokens[2]);
							}
							catch (NumberFormatException e)
							{
								getViewInterface().priorityString("The arguments you are giving are not numbers please try again", 0);
								return;
							}
							if (x > 2 || x < 0)
							{
								getViewInterface().priorityString("The location you chose does not exist, please try again", 0);
								return;
							}

							inter.draw(tokens[1], x);
						}

						default ->
								getViewInterface().priorityString("Unknown command: " + tokens[0] + ", try: 'help' ", 0);
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
							getViewInterface().priorityString("No such command: " + tokens[0] + ", try: 'help'", 0);
							return;
						}
						if (tokens.length != 2)
						{
							getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
							return;
						}
						if (!tokens[1].equals("up") && !tokens[1].equals("down"))
						{
							getViewInterface().priorityString("The face you chose not exists, please try again", 0);
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
							getViewInterface().priorityString("No such command: " + tokens[0] + ", try: 'help'", 0);
							return;
						}
						if (tokens.length != 2)
						{
							getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
							return;
						}
						if (!tokens[1].equals("red") && !tokens[1].equals("blue") && !tokens[1].equals("green") && !tokens[1].equals("yellow"))
						{
							getViewInterface().priorityString("The color you chose not exists, please try again", 0);
							return;
						}

						inter.chooseColor(tokens[1]);
					}

					case "obj" ->
					{
						if (turnings != CHOOSE3)
						{
							getViewInterface().priorityString("No such command: " + tokens[0] + ", try: 'help'", 0);
							return;
						}
						if (tokens.length != 2)
						{
							getViewInterface().priorityString("The command you insert has some syntax error, try 'help'.", 0);
							return;
						}
						int x;
						try
						{
							x = Integer.parseInt(tokens[1]);
						}
						catch (NumberFormatException e)
						{
							getViewInterface().priorityString("The arguments you are giving are not a number please try again", 0);
							return;
						}
						if (x > 2 || x < 1)
						{
							getViewInterface().priorityString("The objective you chose not exists, please try again", 0);
							return;
						}

						inter.chooseObjectiveCard(tokens[1]);
						getClientData().setPersonalObjectiveChosen(tokens[1]);
						getViewInterface().setPersonalObjective(getClientData().getPersonalObjective());
					}
					default -> getViewInterface().priorityString("Unknown command: " + tokens[0] + ", try: 'help'", 0);
				}
			}
		}
		catch (RemoteException e)
		{
			System.err.println("Something went wrong, Retry");
		}
	}

	public void loginCommand(String s)
	{
		try
		{
			inter.sendStringLogin(s);
		}
		catch (RemoteException e)
		{
			throw new RuntimeException(e);
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
	 * Get the class clientData
	 *
	 * @return the clientData Class
	 */
	public ClientDATA getClientData()
	{
		return clientData;
	}

	/**
	 * setter method for the view
	 *
	 * @param view a Viewable representing the view used by this Player
	 */
	public void setView(Viewable view)
	{
		this.viewInterface = view;
	}

	public Viewable getViewInterface()
	{
		return viewInterface;
	}

}

