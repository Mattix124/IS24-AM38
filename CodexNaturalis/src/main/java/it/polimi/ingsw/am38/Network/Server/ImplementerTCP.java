package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MConfirmedPlacement;
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
	public String loginRequest(String s) throws IOException, ClassNotFoundException
	{
		out.writeObject(new Message(LOGIN, INFOMESSAGE, new MSimpleString(s)));
		return ((MSimpleString) ((Message) in.readObject()).getContent()).getText();
	}

	@Override
	public void finalizeInitialization(GameThread gt, Player p, boolean reconnect)
	{
		this.p = p;
		ClientListener clGH     = new ClientListener(in, gt.getServerMessageSorter(), p);
		Thread         listener = new Thread(clGH);
		listener.setDaemon(true);
		listener.start();
		gt.addEntry(this, reconnect);
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
	public void confirmedPlacement(int id, int x, int y, boolean face)
	{
		try
		{
			out.writeObject(new Message(GAME, PLACEMENT, new MConfirmedPlacement(id, x, y, face)));
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
			out.writeObject(new Message(VIEWUPDATE, PLACEMENT, new MClientFirstViewSetup(gc, p)));
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
			out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString("Waiting for other players...")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	@Override
	public void phaseShifter(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			System.err.println("error in sending infomessage ");
		}
	}

	@Override
	public void startGame(String s)
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
	public void noPlaceable(String s)
	{
		try
		{
			out.writeObject(new Message(EXCEPTION, PLACEMENT, new MSimpleString(s)));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void turnScanner(String s)
	{
		try
		{
			out.writeObject(new Message(EXCEPTION, INFOMESSAGE, new MSimpleString(s)));
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
	public void lightError(String s)
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, EXCEPTION, new MSimpleString(s)));
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
