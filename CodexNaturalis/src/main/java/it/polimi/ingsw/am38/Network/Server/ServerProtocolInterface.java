package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;

public interface ServerProtocolInterface
{
		//login ?
	void starterCardSelection(GameController gc);

	void colorSelection();

	void errorMessage(String s);

	void preObjChoiceViewUpdate(GameController gc, Player p);

	void waitTextPlayers();

	void infoMessage(String s);

	void exceptionMessage(String s, int i);

	void playCard();

	void drawCard();

	void endTurn();

	void winnersMessage(String s);

	Player getPlayer();
}
