package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.SimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import static it.polimi.ingsw.am38.Network.Packet.Scope.BCHAT;
import static it.polimi.ingsw.am38.Network.Packet.Scope.CHAT;

public class ClientCommandInterpreter
{
	ObjectOutputStream objectOut;
	PrintWriter stringOut;

	public ClientCommandInterpreter(ObjectOutputStream objectOut, PrintWriter stringOut)
	{
		this.objectOut = objectOut;
		this.stringOut = stringOut;
		//servertype
		//player e player list (per chat e show vari)
	}

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
			System.out.println("-Play 'hand slot (number)' 'x' 'y' place (if possible) the card in the coordinates given");
			System.out.println("-Draw 'origin' 'n': draw a card from the origin chosen ('resource' or 'gold'), n specifies the location: 0 from deck, 1 from the first ground card, 2 for the second ground card");
		}

		/*switch (tokens[0])
		{
			case "chatb" ->
			{//No control over the commands syntax
				StringBuilder text = new StringBuilder();
				for (int i = 1 ; i < tokens.length ; i++)
					text.append(tokens[i]);

				//Tcp
				objectOut.writeObject(new Message(CHAT, BCHAT, new SimpleString(text)));
				//RmiImplementation

			}
			case "chatp" ->
			{
				if(playerlist.get(tokens[1] != null && playerlist.get(tokens[1] != playerName))
				StringBuilder text = new StringBuilder();
				for (int i = 1 ; i < tokens.length ; i++)
					text.append(tokens[i]);

			}
			case "showcard" ->
			{

			}
			case "showfield" ->
			{

			}
			case "placement" ->
			{

			}

			case "play" ->
			{

			}
			case "draw" ->
			{

			}
		}*/
	}
}
