package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Player;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class SetUpPhaseThread extends Thread
{
	private final PrintWriter textOut;
	private final ObjectOutputStream objectOut;
	private final boolean serverType;
	private final Player p;
	private final Scanner clIn;
	private final GameController gc;
	private final Object lock;

	SetUpPhaseThread(PlayerData pd, GameController gC, Object lock)
	{
		this.textOut = pd.getClOut();
		this.objectOut = pd.getClOOut();
		this.serverType = pd.isServerBool();
		this.p = pd.getPlayer();
		this.clIn = pd.getClIn();
		this.gc = gC;
		this.lock = lock;
	}

	@Override
	public void run()
	{
		String      message;
		StarterCard starterCard = null;
		//StarterCard Draw

		//gc.chooseStarterCardFacing(); commentato perché mi dava errore

	}
}
