package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Server.Turnings;
import it.polimi.ingsw.am38.View.Viewable;

import java.rmi.RemoteException;

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
	 * Instance of the ViewInterface needed for some view updates
	 */
	private Viewable viewInterface;
	/**
	 * Boolean that allow the client to reconnect to the server
	 */
	private boolean disconnectionHappened;

	/**
	 * Constructor of ClientCommandInterpreter
	 *
	 * @param inter         the interface implemented from both rmi and tcp client
	 * @param viewInterface the interface implemented from both CLI and GUI
	 */
	public ClientCommandInterpreter(CommonClientInterface inter, Viewable viewInterface)
	{
		this.inter = inter;
		this.clientData = new ClientDATA();
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

			if (disconnectionHappened)
			{
				if (command.equals("reconnect"))
				{
					CLIENTSTARTER.disconnectionHappenedSetter();
					return;
				}
			}
			if (command.equals("exit"))
			{
				CLIENTSTARTER.quit();
				return;
			}
			String[] tokens = command.split(" ");
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
								viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
								return;
							}

							StringBuilder text = new StringBuilder();
							for (int i = 1 ; i < tokens.length ; i++)
								text.append(tokens[i]).append(" ");

							inter.broadcastMessage(text);
							viewInterface.receiveOwnMessage("To all: " + text);
						}
						case "w" ->
						{
							if (!(tokens.length > 2))
							{
								viewInterface.priorityString("Adresse/You have to write an addressee and a message!");
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
								viewInterface.priorityString("NickNotPresent/The nickname you specified is not present, please retry");
								return;
							}
							inter.privateMessage(tokens[1], text);
							viewInterface.receiveOwnMessage("whispered to " + tokens[1] + ": " + text);
						}

						case "showcard" ->
						{
							if (tokens.length != 3)
							{
								viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
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
								viewInterface.sendString("The arguments you are giving are not numbers please try again");
								return;
							}

							viewInterface.updateCardDisplay(clientData.getCardFromPlayerField("onScreenForRealForReal", x, y), x, y);
						}

						case "showfield" ->
						{
							if (tokens.length != 2)
							{
								viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
								return;
							}
							if (!clientData.getPlayersNicknames().contains(tokens[1]))
							{

								viewInterface.sendString("The player you specified is not present, please retry");
								return;
							}
							clientData.setShownPayerNick(tokens[1]);
							viewInterface.showPlayerField(tokens[1]);
						}

						case "play" ->
						{
							System.out.println("1");
							if (turnings != PLAYPHASE)
							{
								getViewInterface().priorityString("NotPlay/You can't play right now");
								return;
							}
							if (tokens.length != 5)
							{
								viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
								return;
							}
							if (!tokens[4].equals("up") && !tokens[4].equals("down"))
							{
								viewInterface.sendString("The face argument you are giving are not 'up' or 'down' please try again");
								return;
							}

							int index, tmpX, tmpY, x, y;

							try
							{
								index = Integer.parseInt(tokens[1]);
								tmpX = Integer.parseInt(tokens[2]); //coords to give to the ClientDATA if the placement is successful
								tmpY = Integer.parseInt(tokens[3]); //coords to give to the ClientDATA if the placement is successful
							}
							catch (NumberFormatException e)
							{
								viewInterface.sendString("The arguments you are giving are not numbers please try again");
								return;
							}
							if ((tmpX + tmpY) % 2 != 0)
							{
								viewInterface.sendString("Invalid placement: please choose coordinates with an even sum (YES zero is EVEN!)");
								return;
							}
							if (index > 2 || index < 0)
							{
								viewInterface.sendString("The index argument you are giving is not 1,2 or 3 please try again");
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
								viewInterface.priorityString("NotDraw/You can't draw right now!");
								return;
							}
							if (tokens.length != 3)
							{
								viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
								return;
							}
							if (!tokens[1].equals("resource") && !tokens[1].equals("gold"))
							{
								viewInterface.sendString("The arguments you are giving are not resource or gold, please try again");
								return;
							}
							int x;

							try
							{
								x = Integer.parseInt(tokens[2]);
							}
							catch (NumberFormatException e)
							{
								viewInterface.sendString("The arguments you are giving are not numbers please try again");
								return;
							}
							if (x > 2 || x < 0)
							{
								viewInterface.sendString("The location you chose does not exist, please try again");
								return;
							}

							inter.draw(tokens[1], x);
						}

						default -> viewInterface.sendString("Unknown command: " + tokens[0] + ", try: 'help' ");
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
							viewInterface.sendString("No such command: " + tokens[0] + ", try: 'help'");
							return;
						}
						if (tokens.length != 2)
						{
							viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
							return;
						}
						if (!tokens[1].equals("up") && !tokens[1].equals("down"))
						{
							viewInterface.sendString("The face you chose not exists, please try again");
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
							viewInterface.sendString("No such command: " + tokens[0] + ", try: 'help'");
							return;
						}
						if (tokens.length != 2)
						{
							viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
							return;
						}
						if (!tokens[1].equals("red") && !tokens[1].equals("blue") && !tokens[1].equals("green") && !tokens[1].equals("yellow"))
						{
							viewInterface.sendString("The color you chose not exists, please try again");
							return;
						}

						inter.chooseColor(tokens[1]);
					}

					case "obj" ->
					{
						if (turnings != CHOOSE3)
						{
							viewInterface.sendString("No such command: " + tokens[0] + ", try: 'help'");
							return;
						}
						if (tokens.length != 2)
						{
							viewInterface.sendString("The command you insert has some syntax error, try 'help'.");
							return;
						}
						int x;
						try
						{
							x = Integer.parseInt(tokens[1]);
						}
						catch (NumberFormatException e)
						{
							viewInterface.sendString("The arguments you are giving are not a number please try again");
							return;
						}
						if (x > 2 || x < 1)
						{
							viewInterface.sendString("The objective you chose not exists, please try again");
							return;
						}

						inter.chooseObjectiveCard(tokens[1]);
						getClientData().setPersonalObjectiveChosen(tokens[1]);
						getViewInterface().setPersonalObjective(getClientData().getPersonalObjective());
					}
					default -> viewInterface.sendString("Unknown command: " + tokens[0] + ", try: 'help'");
				}
			}
		}
		catch (RemoteException e)
		{
			System.err.println("Something went wrong, Retry");
		}
	}

	/**
	 * Method to send to the server the nickname in order to perform a login
	 *
	 * @param s is the nickname
	 */
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

	/**
	 * Getter for the client's interface
	 *
	 * @return
	 */
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
	 * Setter method for the view
	 *
	 * @param view a Viewable representing the view used by this Player
	 */
	public void setView(Viewable view)
	{
		this.viewInterface = view;
	}

	/**
	 * Getter for the view's interface
	 *
	 * @return viewInterface
	 */
	public Viewable getViewInterface()
	{
		return viewInterface;
	}

	/**
	 * Set the disconnectionHappened boolean to alert a disconnection
	 * @param b the value of the boolean
	 */
	public void setDisconnectionHappened(boolean b)
	{
		disconnectionHappened = b;
	}
	public ClientDATA getClientDATA(){
		return this.clientData;
	}
}

