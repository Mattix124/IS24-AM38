package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MDrawCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayCard;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPrivateChat;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class TCPClient extends Thread implements CommonClientInterface
{
	private ObjectOutputStream sOut;
	/**
	 * Instance of ClientMessageSorter
	 */
	private ParserTCP msgInter;
	private Socket socket;
	private boolean autokiller = false;

	private String nickname;
	private boolean arrivedPing;
	private boolean disconnection = false;
	private ClientPingerThread cpt;
	private Viewable viewInterface;

	/**
	 * Constructor of TCPClient
	 *
	 * @param ip ip address
	 * @param p  port
	 */
	public TCPClient(String ip, int p, Viewable viewInterface)
	{
		this.viewInterface = viewInterface;
		do
		{
			try
			{
				this.socket = new Socket(ip,p );
			}
			catch (IOException e)
			{
				System.err.println("Trying to connect...");
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException w)
				{
					throw new RuntimeException(w);
				}
			}

		} while (socket == null);
		viewInterface.startView();
	}

	/**
	 * Listen all the message the server send
	 */
	public void run()
	{

		Message           receivedMessage = null;
		ObjectInputStream objectIn;
		ClientWriter      clientWriter;

		try
		{
			this.sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			ClientCommandInterpreter cci = new ClientCommandInterpreter(this, this.viewInterface);
			viewInterface.setCommandInterpreter(cci);
			this.msgInter = new ParserTCP(cci, sOut);
			cpt = new ClientPingerThread(this);
			cpt.setName("PINGT");
			cpt.setDaemon(true);
			clientWriter = new ClientWriter(cci);
			clientWriter.setName("WriterT");
			clientWriter.setDaemon(true);
			msgInter.setThreads(cpt, clientWriter);
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
		while (!receivedMessage.getHeader1().equals(KILL) && !autokiller)
		{
			try
			{
				msgInter.addMessage(receivedMessage);
				receivedMessage = (Message) objectIn.readObject();
			}
			catch (ClassNotFoundException | IOException e)
			{
				break;
			}
		}
	}

	public void killer(int code)
	{
		autokiller = true;
		try
		{
			socket.close();
			//System.exit(code);
		}
		catch (IOException e)
		{
			System.out.println("non qui pls");
		}
	}

	/**
	 * getter method for the Viewable interface
	 * @return the Viewable instance in this.viewInterface
	 */
	@Override
	public Viewable getViewableInterface() throws RemoteException{
		return this.viewInterface;
	}

	@Override
	public ClientCommandInterpreter getCommandIntepreter() {
		return null;
	}

	@Override
	public void setNickname(String s)
	{
		this.nickname = s;
		msgInter.getCCI().getClientData().setNickname(s);
	}

	@Override
	public void draw(String cardType, int card) throws RemoteException
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
	public void playACard(int card, int x, int y, boolean face) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(GAME, PLAYCARD, nickname, new MPlayCard(card, new Coords(x, y), face)));
		}
		catch (IOException e)
		{
			System.err.println("Error sending a play command" + e.getMessage());
		}
	}

	@Override
	public void chooseFaceStarterCard(String face) throws RemoteException
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
	public void chooseColor(String color) throws RemoteException
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
	public void chooseObjectiveCard(String choose) throws RemoteException
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
	public void broadcastMessage(StringBuilder message) throws RemoteException
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
	public void privateMessage(String receiver, StringBuilder message) throws RemoteException
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

	@Override
	public void ping()
	{
		try
		{
			sOut.writeObject(new Message(CONNECTION, CONNECTION, nickname, null));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void waitPingConfirm()
	{
		synchronized (this)
		{
			while (!arrivedPing && !disconnection)
			{
				try
				{
					this.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
			arrivedPing = false;
		}
	}

	@Override
	public void setDisconnection()
	{
		synchronized (this)
		{
			disconnection = true;
			this.notifyAll();
		}
	}

	@Override
	public void signalsPingArrived()
	{
		synchronized (this)
		{
			arrivedPing = true;
			this.notifyAll();
		}
	}

	@Override
	public String getNickname()
	{
		return nickname;
	}
}

