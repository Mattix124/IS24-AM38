package Controller;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import org.junit.jupiter.api.Test;

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

        p1.chooseStarterCardFace(true);
        p2.chooseStarterCardFace(true);
        p3.chooseStarterCardFace(true);
        p4.chooseStarterCardFace(true);

        p1.chooseColor(Color.RED);
        p2.chooseColor(Color.GREEN);
        p3.chooseColor(Color.BLUE);
        p4.chooseColor(Color.YELLOW);

        g.postColorSelectionSetUp();

        gc.choosePersonalObjectiveCard(p1, 1);
        gc.choosePersonalObjectiveCard(p2, 1);
        gc.choosePersonalObjectiveCard(p3, 1);
        gc.choosePersonalObjectiveCard(p4, 1);
    }

    @Test
    void playerPlay() throws Exception {
        defineGame(g, p1, p2, p3, p4);

        PlayableCard resourceCard = g.getCurrentPlayer().getHand().getCard(0);
        int x = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).x();
        int y = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).y();
        gc.playerPlay(0, x, y, false);

        PlayableCard card = g.getCurrentPlayer().getGameField().getCardFromCoordinate(x,y);

        assertEquals(resourceCard, card);
    }

    @Test
    void playerDraw() throws Exception {
        defineGame(g, p1, p2, p3, p4);

        //------test draw gold card
        //PlayableCard resourceCard = g.getCurrentPlayer().getHand().getCard(0);
        int x1 = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).x();
        int y1 = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).y();
        gc.playerPlay(1, x1, y1, false);

        PlayableCard pc1 = g.getGoldDeck().getGround0(); //keep the current player cause it changes after draw

        Player p = g.getCurrentPlayer();

        gc.playerDraw("gold", 1);

        assertEquals(pc1, p.getHand().getCard(2));

        //------test draw resource card
        int x2 = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).x();
        int y2 = g.getCurrentPlayer().getGameField().getPossiblePlacement().get(0).y();
        gc.playerPlay(1, x2, y2, false);

        PlayableCard pc2 = g.getResourceDeck().getGround0();

        p = g.getCurrentPlayer();

        gc.playerDraw("resource", 1);

        assertEquals(pc2, p.getHand().getCard(2));
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

        assertEquals(true, p1.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertEquals(true, p2.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertEquals(true, p3.getGameField().getCardFromCoordinate(0, 0).getFace());
        assertEquals(true, p4.getGameField().getCardFromCoordinate(0, 0).getFace());
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

        assertEquals(p1.getPair().get(0), p1.getObjectiveCard());
        assertEquals(p2.getPair().get(0), p2.getObjectiveCard());
        assertEquals(p3.getPair().get(0), p3.getObjectiveCard());
        assertEquals(p4.getPair().get(0), p4.getObjectiveCard());
    }
}