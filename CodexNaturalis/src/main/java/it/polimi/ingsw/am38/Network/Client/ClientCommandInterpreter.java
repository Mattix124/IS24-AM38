package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MCoords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

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
	public boolean checkCommand(String command) throws IOException
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
			System.out.println("-Play 'hand slot (number)' 'x' 'y' place (if possible) the card in the coordinates given");
			System.out.println("-Draw 'origin' 'n': draw a card from the origin chosen ('resource' or 'gold'), n specifies the location: 0 from deck, 1 from the first ground card, 2 for the second ground card");
			return true;
		}
		if (tokens.length > 1)
		{
			switch (tokens[0])
			{
				case "chatb" ->
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
					return true;
				}

				case "chatp" ->
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
						return false;
					}

					if (connectionType)
					{ //Tcp
						objectOut.writeObject(new Message(CHAT, PCHAT, nickname, new MPrivateChat(tokens[1], text)));
					}
					else
					{//RmiImplementation
						//non so come funziona la chat
					}
					return true;
				}

				case "showcard" ->
				{
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
						return false;
					}
					//
						if (connectionType)
						{
							objectOut.writeObject(new Message(VIEWUPDATE,SHOWCARD,new MCoords(x,y)));
							//SEND AGGIORNAMENTO
							//TCP CLI UPDATE
						}
						else
						{//RmiImplementation
							//non so ancora cosa fare
						}

					return true;
				}

				case "showfield" ->
				{
					if (!playersNicknames.contains(tokens[1]))
					{
						System.out.println("The player you specified is not present, please retry");
						return false;
					}
					else
					{
						if (connectionType)
						{
							//SEND AGGIORNAMENTO, RECEIVE (DI LA)
							//TcpCLI UPdate
						}
						else
						{
							//RmiImplementation
							//scrivere metodo
						}
						return true;
					}

				}

				case "placement" ->
				{
					if (connectionType)
					{ //Tcp

					}
					else
					{//RmiImplementation
						//scrivere metodo per ottenere i placement
					}
					return true;
				}

				case "play" ->
				{
					if (connectionType)
					{ //Tcp

					}
					else
					{//RmiImplementation
						try
						{
							clientInterface.playACard(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3], nickname); //call the method on the client interface that send the info to the server interface
						}
						catch (NotPlaceableException e)
						{
							throw new RuntimeException(e);
						}
						catch (InvalidInputException e)
						{
							throw new RuntimeException(e);
						}
					}
					return true;
				}

				case "draw" ->
				{
					if (connectionType)
					{ //Tcp

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
					return true;
				}
				default ->
				{
					System.out.println("Unknown command: " + tokens[0] + ", try: 'help'");
					return false;
				}
			}
		}
		else
		{
			System.out.println("Unknown command, or not enough instruction, try: 'help' ");
			return false;
		}
	}
}
