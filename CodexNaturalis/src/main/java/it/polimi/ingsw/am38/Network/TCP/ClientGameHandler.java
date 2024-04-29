package it.polimi.ingsw.am38.Network.TCP;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientGameHandler implements Runnable
{
	final private Socket clSocket;
	final private Scanner clIn;
	final private PrintWriter clOut;

	public ClientGameHandler(Socket clSocket, Scanner clIn, PrintWriter clOut)
	{
		this.clSocket = clSocket;
		this.clIn = clIn;
		this.clOut = clOut;
	}

	@Override
	public void run()
	{

	}
}
