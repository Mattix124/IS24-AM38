package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Model.Player;

import java.io.IOException;

public interface ServerProtocolInterface
{
	void setClientUsername(String s);

	String loginRequest(String s) throws ClassNotFoundException, IOException;

	void finalizeInitialization(GameThread gt, Player p, boolean reconnect);

	void addPingThread(ServerPingThread spt);

	void starterCardSelection(GameController gc);

	void colorSelection(String s);

	void confirmedPlacement(int id,int x, int y, boolean face);

	void preObjChoiceViewUpdate(GameController gc, Player p);

	void waitTextPlayers();

	void phaseShifter(String s);

	void startGame(String s);

	void noPlaceable(String s);
	void lightError(String s);
	void playCard(String s);

	void drawCard(String s);

	void turnScanner(String s);

	void winnersMessage(String s);

	void chatMessage(String s);

	void ping(boolean b);

	Player getPlayer();
}
