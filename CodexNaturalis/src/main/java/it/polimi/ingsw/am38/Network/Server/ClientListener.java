package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;

/**
 * ClientListener is a class that binds every tcp client to the TCPServer
 */
public class ClientListener implements Runnable
{
	/**
	 * Attributes that contains the input of the client
	 */
	final private ObjectInputStream clIn;
	/**
	 * Attributes that contains the ServerMessageSorter
	 */
	final private ServerMessageSorter msgIntSer;
	/**
	 * Instance of player needed for reconnection purposes
	 */
	final private Player player;

	/**
	 * Constructor of ClientListener
	 *
	 * @param p         player instance
	 * @param clIn      the input of the client
	 * @param msgIntSer the ServerMessageSorter
	 */
	public ClientListener(ObjectInputStream clIn, ServerMessageSorter msgIntSer, Player p)
	{
		this.clIn = clIn;
		this.msgIntSer = msgIntSer;
		this.player = p;
	}

	/**
	 * The listening method
	 */
	@Override
	public void run()
	{
		Message message;
		while (true)
		{
			try
			{
				message = (Message) clIn.readObject();
			}
			catch (IOException | ClassNotFoundException | NoSuchElementException e)
			{
				System.out.println(player.getNickname() + " disconnected");
				//player startCountDown
				return;
			}
			msgIntSer.addMessage(message);
		}
	}
}

