package it.polimi.ingsw.am38.Network;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.TCP.NamerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.jar.Attributes;

import static it.polimi.ingsw.am38.Enum.Color.NONE;

public class CNServer
{
	final int port;

	private final LinkedList <Socket> sockets = new LinkedList <>();

	CNServer(int port)
	{
		this.port = port;
	}

	public static void main(String[] args)
	{
		CNServer server = new CNServer(Integer.parseInt(args[0]));
		server.start();
	}

	public void start()
	{
		ServerSocket serverSocket = null;
		Player       player;
		String       errorMessage = "Insert your username:\n ";
		Socket       clSocket;
		PrintWriter  clOut;
		Scanner      clIn;

		try
		{
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		System.out.println("The server is listening on port: " + port);
		while (true)
		{
			clSocket = null;
			player = null;
			do
			{
				try
				{
					clSocket = serverSocket.accept();
					sockets.add(clSocket);

				}
				catch (IOException e)
				{
					System.err.println(e.getMessage());
				}
			} while (clSocket == null);

			Thread playerSorter = new Thread(new NamerThread(clSocket));

			//THREAD CHE FA FARE COSE AL PLAYER. (crea, joina...)
		}
	}

}
