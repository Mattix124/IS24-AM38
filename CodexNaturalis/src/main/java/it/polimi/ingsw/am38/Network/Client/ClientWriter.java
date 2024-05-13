package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriter implements Runnable
{
	private ClientCommandInterpreter clientCommandInterpreter;
	private Scanner in;
	private boolean lophase;
	private PrintWriter stringOut;

	public ClientWriter(Socket socket)
	{
		try
		{
			this.clientCommandInterpreter = new ClientCommandInterpreter(new ObjectOutputStream(socket.getOutputStream()));
			this.in = new Scanner(System.in);
			this.stringOut = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		this.lophase = true;

	}

	@Override
	public void run()
	{
		String message;
		while (lophase)
		{
			message = in.nextLine();
			stringOut.println(message);
		}

		while (true)
		{
			message = in.nextLine();
			try
			{
				clientCommandInterpreter.checkCommand(message);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	public void setPhaseClientWriter(boolean phaseClientWriter)
	{
		this.lophase = phaseClientWriter;
	}
}
