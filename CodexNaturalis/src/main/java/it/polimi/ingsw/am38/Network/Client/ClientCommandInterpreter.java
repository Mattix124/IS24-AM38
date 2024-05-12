package it.polimi.ingsw.am38.Network.Client;

import com.sun.javafx.UnmodifiableArrayList;
import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.SimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.Network.Server.PlayerData;
import it.polimi.ingsw.am38.Network.Server.PlayerDataList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import static it.polimi.ingsw.am38.Network.Packet.Scope.BCHAT;
import static it.polimi.ingsw.am38.Network.Packet.Scope.CHAT;

/**
 * This class parse the input to let the server do what's needed based on the type of
 * connection of the client
 */
public class ClientCommandInterpreter implements Serializable
{
	private static final long serialVersionUID = 4469759083013548722L;
	String nickname;
	ClientInterface clientInterface;
	ObjectOutputStream objectOut;
	PrintWriter stringOut;
	boolean connectionType;

	/**
	 * Constructor for TCP clients
	 * @param objectOut
	 * @param stringOut
	 */
	public ClientCommandInterpreter(ObjectOutputStream objectOut, PrintWriter stringOut)
	{
		this.objectOut = objectOut;
		this.stringOut = stringOut;
		this.connectionType = true; //indica connessione tcp
		//servertype
		//player e player list (per chat e show vari)
	}

	/**
	 * Constructor for RMI clients
	 * @param clientInterface
	 */
	public ClientCommandInterpreter(ClientInterface clientInterface)
	{
		this.clientInterface = clientInterface;
		this.connectionType = false; //indica connessione rmi
	}

	/**
	 * This method parse the input given and based on the connection type of the associated thread do
	 * what the player wants
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
			System.out.println("-Play 'hand slot (number)' 'x' 'y' place (if possible) the card in the coordinates given");
			System.out.println("-Draw 'origin' 'n': draw a card from the origin chosen ('resource' or 'gold'), n specifies the location: 0 from deck, 1 from the first ground card, 2 for the second ground card");
		}

		switch (tokens[0])
		{
			case "chatb" ->
			{//No control over the commands syntax
				StringBuilder text = new StringBuilder();
				for (int i = 1 ; i < tokens.length ; i++)
					text.append(tokens[i]);

				if(connectionType){ //Tcp
					objectOut.writeObject(new Message(CHAT, BCHAT, new SimpleString(text)));
				}else{//RmiImplementation
					//non so come funziona la chat
				}
			}

			case "chatp" ->
			{
				//if(playerlist.get(tokens[1] != null && playerlist.get(tokens[1] != playerName))
				StringBuilder text = new StringBuilder();
				for (int i = 1 ; i < tokens.length ; i++)
					text.append(tokens[i]);

				if(connectionType){ //Tcp

				}else{//RmiImplementation
					//non so come funziona la chat
				}
			}

			case "showcard" ->
			{
				if(connectionType){ //Tcp

				}else{//RmiImplementation
					//non so ancora cosa fare
				}
			}

			case "showfield" ->
			{
				if(connectionType){ //Tcp

				}else{//RmiImplementation
					//scrivere metodo
				}
			}

			case "placement" ->
			{
				if(connectionType){ //Tcp

				}else{//RmiImplementation
					//scrivere metodo per ottenere i placement
				}
			}

			case "play" ->
			{
				if(connectionType){ //Tcp

				}else{//RmiImplementation
                    try {
                        clientInterface.playACard(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3], nickname); //call the method on the client interface that send the info to the server interface
                    } catch (NotPlaceableException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidInputException e) {
                        throw new RuntimeException(e);
                    }
                }
			}

			case "draw" ->
			{
				if(connectionType){ //Tcp

				}else{//RmiImplementation
                    try {
                        clientInterface.draw(nickname, tokens[0], Integer.parseInt(tokens[1])); //call the method on the client interface that send the info to the server interface
                    } catch (EmptyDeckException e) {
                        throw new RuntimeException(e);
                    } catch (GameNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidInputException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
		}
	}
}
