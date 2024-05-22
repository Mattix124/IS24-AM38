package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MClientFirstViewSetup;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MPlayersData;
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

	public ObjectOutputStream getOut()
	{
		return out;
	}

	@Override
	public Player getPlayer()
	{
		return p;
	}

	@Override
	public void starterCardSelection(GameController gc, String nick)
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
	public void colorSelection()
	{
		try
		{
			out.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Choose a color for your pawn: type 'color' and a color: (\u001B[1;34mBLUE\u001B[0m, \u001B[1;31mRED\u001B[0m, \u001B[1;33mYELLOW\u001B[0m, \u001B[1;32mGREEN\u001B[0m)")));
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
	public void playCard()
	{
		try
		{
			out.writeObject(new Message(GAME, PLAYCARD, new MSimpleString("Play your card:")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void drawCard()
	{
		try
		{
			out.writeObject(new Message(GAME, DRAWCARD, new MSimpleString("Draw a card:")));
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
	public void endTurn()
	{
		try
		{
			out.writeObject(new Message(INFOMESSAGE, GAME, new MSimpleString("Your turn has ended!")));
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
}
