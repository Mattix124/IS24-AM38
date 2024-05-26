package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class PlayerDataTCP implements ServerProtocolInterface
{
	private final ObjectOutputStream out;
	private final Player p;

	public PlayerDataTCP(ObjectOutputStream out, Player p)
	{
		this.out = out;
		this.p = p;
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
	public void errorMessage(String s)
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
		catch (IOException e)
		{
			System.out.println("errore in pingTCP");
		}
	}
}
