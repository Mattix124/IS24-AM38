package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayerData
{
	private final Player player;
	private final ObjectOutputStream clOOut;
	private final PrintWriter clOut;
	private final boolean serverBool;
	private final Scanner clIn;

	public PlayerData(Player player, ObjectOutputStream clOOut, PrintWriter clOut, boolean serverBool, Scanner clIn)
	{
		this.player = player;
		this.clOOut = clOOut;
		this.clOut = clOut;
		this.serverBool = serverBool;
		this.clIn = clIn;
	}

	public Player getPlayer()
	{
		return player;
	}

	public ObjectOutputStream getClOOut()
	{
		return clOOut;
	}

	public PrintWriter getClOut()
	{
		return clOut;
	}

	public boolean isServerBool()
	{
		return serverBool;
	}

	public Scanner getClIn()
	{
		return clIn;
	}

}
