package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;

/**
 * SetUpPhaseThread is the class that make all the player to enter the firsts parts of the game.
 */
public class SetUpPhaseThread extends Thread
{

	/**
	 * ServerMessageSorter Instance
	 */
	private final ServerMessageSorter sms;
	/**
	 * Instance of player
	 */
	private final Player p;

	private final ServerProtocolInterface inter;
	/**
	 * GameController instance
	 */
	private final GameController gc;

	/**
	 * Control class for synchronization
	 */
	private LockClass lock;


	SetUpPhaseThread(ServerProtocolInterface pd, GameController gC, ServerMessageSorter mIS, LockClass locker)
	{
		this.sms = mIS;
		this.p = pd.getPlayer();
		this.gc = gC;

		this.inter = pd;
		this.lock = locker;
	}

	/**
	 * Running method that every client has to pass
	 */
	@Override
	public void run()
	{
		Message message;
		Color   c          = null;
		boolean errorColor = false;

		//StarterCard Face
		try
		{
			//deck e ground
			//objectOut.writeObject(new Message(GAME, STARTINGFACECHOICE, new MStringCard(gc)));
			inter.starterCardSelection(gc);
			message = sms.getGameMessage(p.getNickname());

			gc.chooseStarterCardFacing(p, Boolean.parseBoolean(((MSimpleString) message.getContent()).getText()));
			//objectOut.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Choose a color for your pawn: type 'color' and a color: (\u001B[1;34mBLUE\u001B[0m, \u001B[1;31mRED\u001B[0m, \u001B[1;33mYELLOW\u001B[0m, \u001B[1;32mGREEN\u001B[0m)")));
			inter.colorSelection();
			do
			{
				message = sms.getGameMessage(p.getNickname());
				c = switch (((MSimpleString) message.getContent()).getText())
				{
					case "blue" -> Color.BLUE;
					case "red" -> Color.RED;
					case "yellow" -> Color.YELLOW;
					case "green" -> Color.GREEN;
					default -> null;
				};
				try
				{
					gc.chooseColor(p, c);
					errorColor = false;
				}

				catch (ColorTakenException e)
				{
					errorColor = true;
					//objectOut.writeObject(new Message(GAME, COLORCHOICE, new MSimpleString("Unfortunately the color you chose was taken by another player, try another one")));
					inter.errorMessage("Unfortunately the color you chose was taken by another player, try another one");
				}

			} while (errorColor);
			lock.waitForPlayers(inter);
			//objectOut.writeObject(new Message(VIEWUPDATE,OBJECTIVECHOICE,new MClientFirstViewSetup(gc,p)));
			inter.preObjChoiceViewUpdate(gc, p);
			message = sms.getGameMessage(p.getNickname());
			gc.choosePersonalObjectiveCard(p, Integer.parseInt(((MSimpleString) message.getContent()).getText()));
			//objectOut.writeObject(new Message(INFOMESSAGE, INFOMESSAGE, new MSimpleString("Waiting for other players...")));
			lock.waitForPlayers(inter);
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

		/*
			try
			{
				ci.getSarterCard(p.getNickname(), p.getGame().getGameID());
				ci.printLine("Choose a face for your card (up or down)");
				ci.setPhase(Turnings.CHOOSE1);
				message = sms.getGameMessage(p.getNickname());
				gc.chooseStarterCardFacing(p, Boolean.parseBoolean(((MSimpleString) message.getContent()).getText()));

				ci.printLine("Choose a color for your pawn (blue, red, yellow, green)");
				ci.setPhase(CHOOSE2);
				do{
					message = sms.getGameMessage(p.getNickname());
					switch (((MSimpleString) message.getContent()).getText())
					{
						case "blue" -> c = Color.BLUE;
						case "red" -> c = Color.RED;
						case "yellow" -> c = Color.YELLOW;
						case "green" -> c = Color.GREEN;
						default -> c = null;
					};


					try {
						gc.chooseColor(p, c);
						errorColor = false;
					} catch (ColorTakenException e) {
						errorColor = true;
						ci.printLine("Unfortunately the color you chose was taken by another player, try another one");
					}
				}while (errorColor);
				ci.setPhase(STANDBY);
				lock.waitothers(ci);

				ci.printLine("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective");
				ci.setChoosingObjective(p.getPair().get(0).getDescription(), p.getPair().get(1).getDescription());

				//send data to client data

				ci.printLine("Chose one of them: type 'obj' and a number (1 or 2)");
				message = sms.getGameMessage(p.getNickname());
				gc.choosePersonalObjectiveCard(p, Integer.parseInt(((MSimpleString) message.getContent()).getText()));
				ci.setPhase(STANDBY);
				ci.printLine("Waiting for other players...");
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
			}*/
