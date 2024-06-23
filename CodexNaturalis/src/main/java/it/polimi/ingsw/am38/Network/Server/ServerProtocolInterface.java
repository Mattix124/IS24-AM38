package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.io.IOException;

public interface ServerProtocolInterface
{
	void setClientUsername(String s);

	String loginRequest(String s) throws ClassNotFoundException, IOException;

	void finalizeInitialization(GameThread gt, Player p, boolean reconnect);

	void display(String s);
	void addPingThread(ServerPingThread spt);

	void starterCardSelection(GameController gc);

	void colorSelection(String s);

	void preObjChoiceViewUpdate(GameController gc, Player p);

	void waitTextPlayers();

	void enterGame(String s);

	//Game Related
	void turnShifter(String s);

	void noPossiblePlacement(String s);

	void emptyDeck(String s);

	void lightError(String s);

	void playCard(String s);

	void confirmedPlacement(String nickname, int id, int x, int y, boolean face, int points, VisibleElements symbolTab);

	void drawCard(String s);

	void confirmedDraw(GameController gameController);

	void winnersMessage(String s);

	void disconnectionHangingCard(int id);

	//------------------------
	void chatMessage(String s);

	void ping(boolean b);

	void resendInfo(Game game);

	Player getPlayer();

	void confirmedOtherDraw(GameController gameController, Symbol[] s);
}
