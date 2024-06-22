package it.polimi.ingsw.am38.Network.Client;

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
	private boolean lophase = true;

	/**
	 * Constructor of ClientWriter
	 *
	 * @param cci the clientCommandInterpreter that is in common with the ClientMessageSorter
	 */
	public ClientWriter(ClientCommandInterpreter cci)
	{
		this.clientCommandInterpreter = cci;
		this.in = new Scanner(System.in);
	}

	/**
	 * Listen every input of the user and send it
	 */
	@Override
	public void run()
	{
		String message;
		do
		{
			message = in.nextLine();
			message = message.toLowerCase();
			if (!lophase)
				clientCommandInterpreter.checkCommand(message);
			else
				clientCommandInterpreter.loginCommand(message);
		} while (!message.equals("reconnect"));

	}

	public void removeLoginPhase()
	{
		this.lophase = false;
	}
}
