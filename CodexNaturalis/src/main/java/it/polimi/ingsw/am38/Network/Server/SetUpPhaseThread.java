package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MStringCard;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

/**
 * SetUpPhaseThread is the class that make all the player to enter the firsts parts of the game.
 */
public class SetUpPhaseThread extends Thread
{

	private final ObjectOutputStream objectOut;
	/**
	 * ServerMessageSorter Instance
	 */
	private final ServerMessageSorter sms;
	/**
	 * Instance of player
	 */
	private final Player p;

	private final PlayerData pd;
	/**
	 * GameController instance
	 */
	private final GameController gc;
	/**
	 * Control variable for synchronization
	 */
	private boolean allColored;

	private LockClass lock;

	ClientInterface ci;

	SetUpPhaseThread(PlayerData pd, GameController gC, ServerMessageSorter mIS, ClientInterface ci,LockClass locker)
	{
		this.objectOut = pd.getClOOut();
		this.sms = mIS;
		this.p = pd.getPlayer();
		this.gc = gC;
		this.ci = ci;
		this.pd = pd;
		this.lock = locker;
	}

	/**
	 * Running method that every client has to pass
	 */
	@Override
	public void run()
	{
		if (pd.isServerBool())
		{
			Message message;
			//StarterCard Face
			try
			{
				objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE, new MStringCard("Choose a face for your card: type face and orientation: (up or down)", p.getStarterCard().getCardID())));
				//CLI UPDATE
				message = sms.getGameMessage(p.getNickname());
				gc.chooseStarterCardFacing(p, Boolean.parseBoolean(((MSimpleString) message.getContent()).getText()));
				//CLI UPDATE
				objectOut.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Choose a color for your pawn: type 'color' and a color: (blue, red, yellow, green)")));
				boolean errorColor = false;
				do
				{
					message = sms.getGameMessage(p.getNickname());
					Color c = Color.RED;
					switch (((MSimpleString) message.getContent()).getText())
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
						objectOut.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Unfortunately the color you chose was taken by another player, try another one")));
					}

				} while (errorColor);
				lock.waitothers(objectOut);
				objectOut.writeObject(new Message(GAME, INFOMESSAGE, new MSimpleString("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective")));
				//VIEW UPDATE
				objectOut.writeObject(new Message(GAME, OBJECTIVECHOICE, new MSimpleString("Chose one of them: type 'obj' and a number (1 or 2)")));
				message = sms.getGameMessage(p.getNickname());
				gc.choosePersonalObjectiveCard(p, Integer.parseInt(((MSimpleString) message.getContent()).getText()));
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
		else
		{
			try
			{
				ci.setChoosingColorAndFace();
				/*Message m = null;
				while(m == null)
					m = sms.getGameMessage(p.getNickname());

				 */
			}
			catch (RemoteException e)
			{
				throw new RuntimeException(e);
			}

            try {
                lock.waitothers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try
			{
				ci.setChoosingObjective();
			}
			catch (RemoteException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
