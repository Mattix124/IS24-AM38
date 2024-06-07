package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class ImplementerTCP implements ServerProtocolInterface
{
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	private Player p;
	private ServerPingThread spt;

	public ImplementerTCP(ObjectOutputStream out, ObjectInputStream in)
	{
		this.out = out;
		this.in = in;
	}

	@Override
	public void setClientUsername(String s)
	{
		try
		{
			out.writeObject(new Message(LOGIN, NICKNAME, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			System.out.println("Error in nickname communication out");
		}
	}

	@Override
	public String loginRequest(String s)
	{
		try
		{
			out.writeObject(new Message(LOGIN, INFOMESSAGE, new MSimpleString(s)));
			return ((MSimpleString) ((Message) in.readObject()).getContent()).getText();
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println("Error in Intention communication");
		}
		return null;
	}

	@Override
	public void finalizeInitialization(GameThread gt, Player p, boolean reconnect)
	{
		this.p = p;
		ClientListener clGH     = new ClientListener(in, gt.getServerMessageSorter(), p);
		Thread         listener = new Thread(clGH);
		listener.setDaemon(true);
		listener.start();
		System.out.println(reconnect +" recon");
		if (!reconnect)
			gt.addEntry(this);
		else
			gt.reconnection(p, this);

	}

	@Override
	public void addPingThread(ServerPingThread spt)
	{
		this.spt = spt;
	}

	@Override
	public Player getPlayer()
	{
		return p;
	}

	@Override
	public void starterCardSelection(GameController gc)
	{
		try
		{
			out.writeObject(new Message(GAME, STARTINGFACECHOICE, new MStringCard(gc)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void colorSelection(String s)
	{
		try
		{
			out.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void confirmedPlacement(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, INFOMESSAGE, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void preObjChoiceViewUpdate(GameController gc, Player p)
	{
		try
		{
			out.writeObject(new Message(VIEWUPDATE, OBJECTIVECHOICE, new MClientFirstViewSetup(gc, p)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void waitTextPlayers()
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, INFOMESSAGE, new MSimpleString("Waiting for other players...")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void infoMessage(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, INFOMESSAGE, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startGameMessage(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, START, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void playCard(String s)
	{
		try
		{
			out.writeObject(new Message(GAME, PLAYCARD, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void drawCard(String s)
	{
		try
		{
			out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void exceptionMessage(String s, int i)
	{

	}

	@Override
	public void endTurn(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void winnersMessage(String s)
	{
		try
		{
			out.writeObject(new Message(GAME, WINNER, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void chatMessage(String s)
	{
		try
		{
			out.writeObject(new Message(CHAT, BCHAT, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void ping(boolean start)
	{
		try
		{
			if (start)
			{
				out.writeObject(new Message(CONNECTION, START, null));
			}
			else
				out.writeObject(new Message(CONNECTION, CONNECTION, null));
		}
		catch (IOException ignored)
		{
		}
	}
}
