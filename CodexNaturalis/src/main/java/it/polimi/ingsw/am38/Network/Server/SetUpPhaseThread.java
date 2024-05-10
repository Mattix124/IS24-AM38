package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.SimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

public class SetUpPhaseThread extends Thread
{
	private final PrintWriter textOut;
	private final ObjectOutputStream objectOut;
	private final boolean serverType;
	private final Player p;
	private final Scanner clIn;
	private final GameController gc;

	SetUpPhaseThread(PlayerData pd, GameController gC, MessageInterpreterServer mIS)
	{
		this.textOut = pd.getClOut();
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
			objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE, new SimpleString("Choose a face for your card (up or down)\n")));
			message = clIn.nextLine();
			gc.chooseStarterCardFacing(p, Boolean.parseBoolean(message));
			objectOut.writeObject(new Message(GAME, COLORCHOICE, new SimpleString("Choose a color for your pawn (blue, red, yellow, green)\n")));
			boolean errorColor = false;
			do
			{
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
				}

				catch (ColorTakenException e)
				{
					errorColor = true;
				}

			} while (errorColor);

			objectOut.writeObject(new Message(GAME, INFOMESSAGE, new SimpleString("You have drawn 2 Resource Card and 1 Gold Card")));
			//VIEW UPDATE

		}
		catch (IOException e)
		{
			throw new RuntimeException();
		}
	}
}
