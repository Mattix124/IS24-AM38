package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;

public interface ServerProtocolInterface
{
	String loginMessage(String s);

	void setClientUsername(String s);

	String askForIntentions(String s);

	void finalizeInitialization(GameThread gt , Player p, ServerProtocolInterface inter);

	void starterCardSelection(GameController gc);

	void colorSelection(String s);

	void errorMessage(String s);

	void preObjChoiceViewUpdate(GameController gc, Player p);

	void waitTextPlayers();

	void infoMessage(String s);

	void startGameMessage(String s);

	void exceptionMessage(String s, int i);

	void playCard(String s);

	void drawCard(String s);

	void endTurn(String s);

	void winnersMessage(String s);

	void chatMessage(String s);

	void ping(boolean b);

	Player getPlayer();
}
