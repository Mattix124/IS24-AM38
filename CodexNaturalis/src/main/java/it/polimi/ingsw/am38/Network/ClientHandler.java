package it.polimi.ingsw.am38.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientThread implements Runnable
{
	final private Socket socket;
	private Scanner I;
	private PrintWriter O;
	private String username;
	final protected static LinkedList <ClientThread> clientList = new LinkedList <ClientThread>();

	public ClientThread(Socket socket)
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

		String line;
		O.println("Insert your Username:\nUsername: ");
		O.flush();
		username = I.nextLine();
		O.println("Hi " + username + ", you can now start chatting.");
		O.flush();


		do
		{
			line = I.nextLine();
			if (line.equals("quit"))
			{
				System.out.println("Closing socket for (" + username + ") " + socket.getInetAddress() + " ...");
				O.println("You are now quitting. Bye");
				O.flush();
			}
			else
			{
				System.out.println(line);
				Send(line);
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
		for (int i = 0 ; i < clientList.size() ; i++)
		{

			if (clientList.get(i).equals(this)) i++;
			clientList.get(i).O.println(username + ":" + message);
			clientList.get(i).O.flush();

		}

	}
}
