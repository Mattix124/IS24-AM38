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
	/**
	 * Boolean to check if the client has to be killed
	 */
	private boolean autokiller = false;

	/**
	 * Instance of the ClientCommandInterpreter
	 */
	private ClientCommandInterpreter cci;

	/**
	 * Nickname of the player
	 */
	private String nickname;
	/**
	 * Boolean to check if the ping has arrived from the server
	 */
	private boolean arrivedPing;
	/**
	 * Boolean to check if the player is disconnected
	 */
	private boolean disconnection = false;
	/**
	 *	Instance of ClientWriter
	 */
	private ClientWriter cw;
	/**
	 * Instance of the ClientPingerThread
	 */
	private ClientPingerThread cpt;
	/**
	 * Instance of the interface of the view
	 */
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
				this.socket = new Socket(ip, p);
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
		this.cci = new ClientCommandInterpreter(this, this.viewInterface);
		this.cw = viewInterface.startView(cci);
		if (cw != null)
			cw.start();
	}

	/**
	 * Listen all the message the server send
	 */
	public void run()
	{

		Message           receivedMessage = null;
		ObjectInputStream objectIn;

		try
		{
			this.sOut = new ObjectOutputStream(socket.getOutputStream());
			objectIn = new ObjectInputStream(socket.getInputStream());
			this.msgInter = new ParserTCP(cci, sOut);
			cpt = new ClientPingerThread(this,viewInterface);
			cpt.setName("PINGT");
			cpt.setDaemon(true);
			msgInter.setThreads(cpt, cw);
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

	/**
	 * Method to kill the client due to a disconnection
	 *
	 * @param code
	 * @throws RemoteException
	 */
	public void killer(int code)
	{
		autokiller = true;
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			System.err.println("error in closing app");
		}
	}

	/**
	 * Method to send the nickname to the client and set it in the ClientData(called by the server)
	 *
	 * @param s the nickname
	 */
	@Override
	public void setNickname(String s)
	{
		this.nickname = s;
		msgInter.getCCI().getClientData().setNickname(s);
	}

	/**
	 * This method communicates to the server the parameters it needs to perform a draw for the player
	 *
	 * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
	 * @param card     is an integer that allows the controller to know which card draw
	 * @throws RemoteException
	 */
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

	/**
	 * This method communicates to the server the parameters it needs to play a card for a
	 * players in its field
	 *
	 * @param card is the card to play
	 * @param x    the x coordinates where to play the card
	 * @param y    the x coordinates where to play the card
	 * @param face is how the card has to be played, face up or face down
	 * @throws RemoteException
	 */
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

	/**
	 * Method that communicate to the server the face chosen for the starter card
	 *
	 * @param face is the face chosen
	 * @throws RemoteException
	 */
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

	/**
	 * Method that communicate to the server the color chosen for the pawn
	 *
	 * @param color is the color chosen
	 * @throws RemoteException
	 */
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

	/**
	 * Method that communicate to the server the objective card chosen
	 *
	 * @param choose is the objective chosen
	 * @throws RemoteException
	 */
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

	/**
	 * Method to send a message to every player
	 *
	 * @param message the string that represent the message
	 * @throws RemoteException
	 */
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

	/**
	 * Method to send a message to a certain player
	 *
	 * @param receiver is the nickname of the player to whom to send the message
	 * @param message  the string that represent the message
	 * @throws RemoteException
	 */
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

	/**
	 * Method to send a ping to the server
	 *
	 * @throws RemoteException
	 */
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

	/**
	 * Method that waits for a ping response
	 *
	 * @throws RemoteException
	 */
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

	/**
	 * Method to set the client to disconnected
	 */
	@Override
	public void setDisconnection()
	{
		synchronized (this)
		{
			disconnection = true;
			this.notifyAll();
		}
	}

	/**
	 * Method to check the ping has arrived from the server
	 *
	 * @throws RemoteException
	 */
	@Override
	public void signalsPingArrived()
	{
		synchronized (this)
		{
			arrivedPing = true;
			this.notifyAll();
		}
	}

	/**
	 * Getter for the nickname
	 *
	 * @return
	 */
	@Override
	public String getNickname()
	{
		return nickname;
	}

	/**
	 *
	 * @param s
	 * @throws RemoteException
	 */
	@Override
	public void sendStringLogin(String s) throws RemoteException
	{
		try
		{
			sOut.writeObject(new Message(LOGIN, LOGIN, null, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}

