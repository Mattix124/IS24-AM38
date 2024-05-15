package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;

import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * PlayerData is a class () that contains the needed elements to let the server manage the connection of the client associated with the player.
 */
public class PlayerData
{
	/**
	 * Player instance
	 */
	private final Player player;
	/**
	 * TCP output of the player (null if rmi)
	 */
	private final ObjectOutputStream clOOut;
	/**
	 * Boolean to identify if the player is connected as TCP or RMI
	 */
	private final boolean serverBool;
	/**
	 * ClientInterface instance of the RMI player (null if TCP)
	 */
	ClientInterface ci;

	/**
	 * Constructor of PlayerData
	 * @param player Player Instance
	 * @param clOOut output for tcp
	 * @param serverBool rmi or tcp connection
	 * @param ci client interface for rmi
	 */
	public PlayerData(Player player, ObjectOutputStream clOOut, boolean serverBool, ClientInterface ci)
	{
		this.player = player;
		this.clOOut = clOOut;
		this.serverBool = serverBool;
		this.ci = ci;
	}

	/**
	 * Getter for the player
	 * @return player
	 */
	public Player getPlayer()
	{
		return player;
	}
	/**
	 * Getter for output tcp
	 * @return output tcp
	 */
	public ObjectOutputStream getClOOut()
	{
		return clOOut;
	}
	/**
	 * Getter for the player connection
	 * @return player connection
	 */
	public boolean isServerBool()
	{
		return serverBool;
	}
	/**
	 * Getter for ClientInterface
	 * @return ClientInterface
	 */
	public ClientInterface getInterface(){return ci;}
}
