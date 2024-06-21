package Controller;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Server.GameThread;
import it.polimi.ingsw.am38.Network.Server.ServerRMI;
import it.polimi.ingsw.am38.Network.Server.ServerTCP;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyManagerTest {
    LobbyManager lm = LobbyManager.getLobbyManager();

    @Test
    void allMethodsTest() throws NicknameTakenException, NullNicknameException, NumOfPlayersException, GameNotFoundException, RemoteException {
        Player p1 = lm.createPlayer("tommy");
        Player p2 = lm.createPlayer("beppe");
        Player p3 = lm.createPlayer("matti");
        Player p4 = lm.createPlayer("dade");

        lm.createNewGame(4, p1);

        lm.joinGame(0, p2);
        lm.joinGame(0, p3);
        lm.joinGame(0, p4);

        GameThread gt = new GameThread(p1, 0, 4);
        lm.addGameThread(gt);

        assertEquals(4, lm.getGame(0).getNumPlayers());
        assertEquals(p1, lm.getPlayer("tommy"));
        assertEquals(p2, lm.getPlayer("beppe"));
        assertEquals(p3, lm.getPlayer("matti"));
        assertEquals(p4, lm.getPlayer("dade"));
        assertEquals(true , lm.getGameThreadList().contains(gt));
        assertEquals(lm.getGameController(0).getGame(), lm.getGame(0));
        assertEquals(gt, lm.getGameThread("tommy"));

        ServerRMI serverRMI = new ServerRMI(0);
        ServerTCP serverTCP = new ServerTCP(0);
        lm.setServerRMI(serverRMI);
        lm.setServerTCP(serverTCP);
        assertNotNull(lm.getReferenceContainer());
}


}