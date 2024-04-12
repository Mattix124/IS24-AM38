package it.polimi.ingsw.am38.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
	final private Socket socket;
	private Scanner I;
	private PrintWriter O;
	private String username;
	final protected static LinkedList <ClientHandler> clientList = new LinkedList <ClientHandler>();

	public ClientHandler(Socket socket)
	{
		this.socket = socket;
		clientList.add(this);
		try
		{
			this.I = new Scanner(socket.getInputStream());
			this.O = new PrintWriter(socket.getOutputStream());
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	public void run()
	{
		O.println("OK");
		O.flush();
		O.println("Insert your Username:\nUsername: ");
		O.flush();
		username = I.nextLine();
		Send(username + " Has join the chat.");
		O.println("Hi " + username + ", you can now start chatting.");
		O.flush();
		String line;

		do
		{
			line = I.nextLine();
			if (line.equals("quit"))
			{
				System.out.println("Closing socket for (" + username + ") " + socket.getInetAddress() + " ...");
			}
			else
			{
				Send(username + ": " + line);
			}
		} while (!line.equals("quit"));
		I.close();
		O.close();
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	private void Send(String message)
	{
		for (ClientHandler cl : clientList)
		{

			if (!cl.equals(this))
			{
				cl.O.println(message);
				cl.O.flush();
			}


		}

	}
}
