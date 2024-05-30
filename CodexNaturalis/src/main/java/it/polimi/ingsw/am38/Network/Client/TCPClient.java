package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.am38.Network.Packet.Scope.KILL;

public class TCPClient extends Thread
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
	public TCPClient(String ip, int p)
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
		Message            receivedMessage = null;
		ObjectInputStream  objectIn;
		ClientWriter       clientWriter;
		ClientPingerThread cpt;

		try
		{
			socket = new Socket(ip, port);
			this.clientData = new ClientDATA();
			ObjectOutputStream sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			ClientCommandInterpreter cci = new ClientCommandInterpreter(sOut, clientData);
			this.msgInter = new ClientMessageSorter(cci);
			cpt = new ClientPingerThread(sOut, msgInter, this);
			cpt.setName("PINGT");
			cpt.setDaemon(true);
			msgInter.setCpt(cpt);
			clientWriter = new ClientWriter(socket, cci);
			clientWriter.setName("WriterT");
			clientWriter.setDaemon(true);
			clientWriter.start();
			msgInter.getCCI().getCLI().printTitle();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		try
		{
			receivedMessage = (Message) objectIn.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage() + e.getClass());
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

