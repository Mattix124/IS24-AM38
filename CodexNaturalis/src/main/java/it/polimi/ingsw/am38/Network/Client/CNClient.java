package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.KILL;

public class CNClient extends Thread
{
	/**
	 * Ip address
	 */
	private final String ip;
	/**
	 * Socket port
	 */
	private final int port;
	/**
	 * Instance of ClientMessageSorter
	 */
	private ClientDATA clientData;
	private ClientMessageSorter msgInter;

	private boolean autokiller = false;

	/**
	 * Constructor of TCPClient
	 *
	 * @param ip ip address
	 * @param p  port
	 */
	public CNClient(String ip, int p)
	{
		this.ip = ip;
		this.port = p;
	}

	/**
	 * Listen all the message the server send
	 */
	public void run()
	{
		Socket             socket;
		String             received;
		Message            receivedMessage = null;
		Scanner            sIn;
		ObjectInputStream  objectIn;
		ClientWriter       cw;
		ClientPingerThread cpt;

		try
		{
			socket = new Socket(ip, port);
			this.clientData = new ClientDATA();
			sIn = new Scanner(socket.getInputStream());
			ObjectOutputStream sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			ClientCommandInterpreter cci = new ClientCommandInterpreter(sOut, clientData);
			this.msgInter = new ClientMessageSorter(cci);
			cpt = new ClientPingerThread(sOut, msgInter, this);
			msgInter.setCpt(cpt);
			cw = new ClientWriter(socket, cci);
			Thread clientWriter = new Thread(cw);
			clientWriter.setDaemon(true);
			clientWriter.start();
			msgInter.getCCI().getCLI().printTitle();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		received = sIn.nextLine();
		String[] s;
		while (!received.equals("ends"))//login phase
		{
			try
			{
				if (received.contains("/username"))
				{
					s = received.split(" ");
					clientData.setNickname(s[1]);
					cpt.setNick(s[1]);
				}
				else
				{
					System.out.println(received);
				}

				received = sIn.nextLine();

			}
			catch (NoSuchElementException e)
			{
				break;
			}
		}
		sIn.reset();
		cw.setPhaseClientWriter(false);
		try
		{
			receivedMessage = (Message) objectIn.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
		}

		while (!receivedMessage.getHeader1().equals(KILL) && !autokiller) //game
		{
			try
			{
				msgInter.addMessage(receivedMessage);
				receivedMessage = (Message) objectIn.readObject();
			}
			catch (ClassNotFoundException | IOException e)
			{
				System.out.println("disconnection happened");
				autokiller = true;
			}
		}
	}

	public void killer()
	{
		autokiller = true;
	}
}

