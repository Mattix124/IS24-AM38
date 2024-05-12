package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class SetUpPhaseThread extends Thread
{
	private final ObjectOutputStream objectOut;
	private final boolean serverType;
	private final Player p;
	private final Scanner clIn;
	private final GameController gc;
	private static Boolean allColored = false;

	SetUpPhaseThread(PlayerData pd, GameController gC, ServerMessageSorter mIS)
	{
		this.objectOut = pd.getClOOut();
		this.serverType = pd.isServerBool();
		this.p = pd.getPlayer();
		this.clIn = pd.getClIn();
		this.gc = gC;
	}

	@Override
	public void run()
	{
		String message;
		//StarterCard Face
		try
		{
			objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE, new MSimpleString("Choose a face for your card (up or down)\n")));
			message = clIn.nextLine();
			gc.chooseStarterCardFacing(p, Boolean.parseBoolean(message));

			boolean errorColor = false;
			do
			{
				objectOut.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Choose a color for your pawn (blue, red, yellow, green)\n")));
				message = clIn.nextLine();
				Color c = Color.RED;
				switch (message)
				{
					case "blue":
						c = Color.BLUE;
						break;
					case "red":
						c = Color.RED;
						break;
					case "yellow":
						c = Color.YELLOW;
						break;
					case "green":
						c = Color.GREEN;
						break;
				}
				try
				{
					gc.chooseColor(p, c);
					errorColor = false;

					synchronized (allColored)
					{
						if (gc.isF())
						{
							allColored = true;
							allColored.notifyAll();
						}
						while (!allColored)
						{
							allColored.wait();
						}
					}
				}

				catch (ColorTakenException e)
				{
					errorColor = true;
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}

			} while (errorColor);

			objectOut.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective, chose one of them:\n (1 or 2)")));
			//VIEW UPDATE
			message = clIn.nextLine();
			gc.choosePersonalObjectiveCard(p, Integer.parseInt(message));
			//obbiettivo
			objectOut.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString("Waiting for other players...")));
		}
		catch (IOException e)
		{
			throw new RuntimeException();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
