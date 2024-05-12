package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;

import java.io.ObjectOutputStream;
import java.util.Scanner;

public class PlayerData
{
	private final Player player;
	private final ObjectOutputStream clOOut;
	private final boolean serverBool;

	public PlayerData(Player player, ObjectOutputStream clOOut, boolean serverBool)
	{
		this.player = player;
		this.clOOut = clOOut;
		this.serverBool = serverBool;

	}

	public Player getPlayer()
	{
		return player;
	}

	public ObjectOutputStream getClOOut()
	{
		return clOOut;
	}

	public boolean isServerBool()
	{
		return serverBool;
	}

}
