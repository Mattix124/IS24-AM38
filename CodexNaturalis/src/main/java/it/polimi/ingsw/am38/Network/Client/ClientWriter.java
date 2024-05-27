package it.polimi.ingsw.am38.Network.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class that works in parallel to "listen" the text from the user
 */
public class ClientWriter extends Thread
{
	/**
	 * Instance of ClientCommandInterpreter
	 */
	private ClientCommandInterpreter clientCommandInterpreter;
	/**
	 * Instance of scanner that allows the communication in tcp with String
	 */
	private Scanner in;
	/**
	 * Boolean that control the sender class (String or Object)
	 */
	private boolean loPhase;
	/**
	 * Instance of PrintWriter that sends the string to the server
	 */
	private PrintWriter stringOut;

	/**
	 * Constructor of ClientWriter
	 * @param socket the socket bound to the server
	 * @param cci the clientCommandInterpreter that is in common with the ClientMessageSorter
	 */
	public ClientWriter(Socket socket,ClientCommandInterpreter cci)
	{
		try
		{
			this.clientCommandInterpreter = cci;
			this.in = new Scanner(System.in);
			this.stringOut = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		this.loPhase = true;

	}

	/**
	 * Listen every input of the user and send it
	 */
	@Override
	public void run()
	{
		String message;
		while (true)
		{
			message = in.nextLine();
			if (loPhase)
			{
				stringOut.println(message);
			}
			else
			{
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
	}

	/**
	 * Setter that change the "destination"
	 * @param phaseClientWriter phase
	 */
	public void setPhaseClientWriter(boolean phaseClientWriter)
	{
		this.loPhase = phaseClientWriter;
	}
}
