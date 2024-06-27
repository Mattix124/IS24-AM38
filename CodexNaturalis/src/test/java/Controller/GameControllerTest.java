package Controller;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    LobbyManager lm = LobbyManager.getLobbyManager();
    Player p1  = new Player("tommy");
    Player p2  = new Player("beppe");
    Player p3  = new Player("dade");
    Player p4  = new Player("matti");

    Game g = new Game(0, 4, p1);
    GameController gc = new GameController(lm, g);

    void defineGame(Game g, Player p1, Player p2, Player p3, Player p4) throws Exception {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);

        g.gameStartConstructor();

        gc.chooseStarterCardFacing(p1, true);
        gc.chooseStarterCardFacing(p2, true);
        gc.chooseStarterCardFacing(p3, true);
        gc.chooseStarterCardFacing(p4, true);

        p1.chooseColor(Color.RED);
        p2.chooseColor(Color.GREEN);
        p3.chooseColor(Color.BLUE);
        p4.chooseColor(Color.YELLOW);

        g.postColorSelectionSetUp();

        for(Player p:g.getPlayers())
            p.setIsPlaying(true);

        gc.choosePersonalObjectiveCard(p1, 1);
        gc.choosePersonalObjectiveCard(p2, 1);
        gc.choosePersonalObjectiveCard(p3, 1);
        gc.choosePersonalObjectiveCard(p4, 1);

    }

    @Test
    void playerPlay() throws Exception {
        defineGame(g, p1, p2, p3, p4);

        PlayableCard card1 = g.getCurrentPlayer().getHand().getCard(0);
        int x = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().x();
        int y = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().y();
        gc.playerPlay(0, x, y, false);
        VisibleElements ve = gc.getSymbolTab();

        PlayableCard card2 = g.getCurrentPlayer().getGameField().getCardFromCoordinate(x,y);

        assertEquals(card1, card2);
    }

    @Test
    void playerDraw() throws Exception {
        defineGame(g, p1, p2, p3, p4);

        //------test draw gold card
        //PlayableCard resourceCard = g.getCurrentPlayer().getHand().getCard(0);
        int x1 = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().x();
        int y1 = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().y();
        gc.playerPlay(1, x1, y1, false);

        PlayableCard pc1 = g.getGoldDeck().getGround0(); //keep the current player cause it changes after draw

        Player p = g.getCurrentPlayer();

        gc.playerDraw("gold", 1);

        assertEquals(gc.getCardDrawn(), p.getHand().getCard(1));

        //------test draw resource card
        int x2 = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().x();
        int y2 = g.getCurrentPlayer().getGameField().getPossiblePlacement().getFirst().y();
        gc.playerPlay(1, x2, y2, false);

        PlayableCard pc2 = g.getResourceDeck().getGround0();

        p = g.getCurrentPlayer();

        gc.playerDraw("resource", 1);
        List<Player> pl = gc.getWinners();

        assertTrue(p.getHand().getCardsInHand().contains(pc2));
    }

    @Test
    void chooseStarterCardFacing() throws NumOfPlayersException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);

        g.gameStartConstructor();

        gc.chooseStarterCardFacing(p1, true);
        gc.chooseStarterCardFacing(p2, true);
        gc.chooseStarterCardFacing(p3, true);
        gc.chooseStarterCardFacing(p4, true);

        assertTrue(p1.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertTrue(p2.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertTrue(p3.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertTrue(p4.getGameField().getCardFromCoordinate(0, 0).getFace());
    }

    @Test
    void chooseColor() throws NumOfPlayersException, ColorTakenException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);

        gc.getGame().gameStartConstructor();

        gc.chooseColor(p1, Color.RED);
        gc.chooseColor(p2, Color.GREEN);
        gc.chooseColor(p3, Color.BLUE);
        gc.chooseColor(p4, Color.YELLOW);

        assertEquals(Color.RED, p1.getColor());
        assertEquals(Color.GREEN, p2.getColor());
        assertEquals(Color.BLUE, p3.getColor());
        assertEquals(Color.YELLOW, p4.getColor());
    }

    @Test
    void choosePersonalObjectiveCard() throws Exception {
        defineGame(g, p1, p2, p3, p4);

        assertEquals(p1.getPair().getFirst(), p1.getObjectiveCard());
        assertEquals(p2.getPair().getFirst(), p2.getObjectiveCard());
        assertEquals(p3.getPair().getFirst(), p3.getObjectiveCard());
        assertEquals(p4.getPair().getFirst(), p4.getObjectiveCard());
    }
}