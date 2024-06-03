package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class TCPClient extends Thread implements CommonClientInterface
{
	/**
	 * Ip address
	 */
	private final String ip;
	/**
	 * Socket port
	 */
	private final int port;
	private ObjectOutputStream sOut;
	/**
	 * Instance of ClientMessageSorter
	 */
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
			this.sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			ClientCommandInterpreter cci = new ClientCommandInterpreter(this);
			this.msgInter = new ClientMessageSorter(cci,sOut);
			cpt = new ClientPingerThread(sOut, msgInter, this);
			cpt.setName("PINGT");
			cpt.setDaemon(true);
			clientWriter = new ClientWriter(cci);
			clientWriter.setName("WriterT");
			clientWriter.setDaemon(true);
			msgInter.setThreads(cpt,clientWriter);
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

	@Override
	public void draw(String nickname, String cardType, int card) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, DRAWCARD, nickname, new MDrawCard(cardType, card)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending a draw command");
		}
	}

	@Override
	public void playACard(int card, int x, int y, boolean face, String nickname) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, PLAYCARD, nickname, new MPlayCard(card, new Coords(x, y), face)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending a play command");
		}
	}

	@Override
	public void showCard(String nickname, int x, int y) throws RemoteException
	{

	}

	@Override
	public void showField(String nickname, String player) throws RemoteException
	{

	}

	@Override
	public void chooseFaceStarterCard(String nickname, String face) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, STARTINGFACECHOICE, nickname, new MSimpleString(face)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void chooseColor(String nickname, String color) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, COLORCHOICE, nickname, new MSimpleString(color)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending the color choice command");
		}
	}

	@Override
	public void chooseObjectiveCard(String nickname, String choose) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, OBJECTIVECHOICE, nickname, new MSimpleString(choose)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending objective choice command");
		}
	}

	@Override
	public void broadcastMessage(String nickname, StringBuilder message) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(CHAT, BCHAT, nickname, new MSimpleString(message)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending a broadcast message");
		}
	}

	@Override
	public void privateMessage(String nickname, String receiver, StringBuilder message) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(CHAT, PCHAT, nickname, new MPrivateChat(receiver, message)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending a private message");
		}

	}
}

