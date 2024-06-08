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

	void confirmedPlacement(String s);

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
