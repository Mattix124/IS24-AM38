package Model;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;


import static it.polimi.ingsw.am38.Enum.Color.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Player p1, p2, p3, p4, p5;
    Game g;

    @BeforeEach
    void setUp(){
        p1 = new Player("Pierino");
        p2 = new Player("Tizio");
        p3 = new Player("Caio");
        p4 = new Player("Sempronio");
        p5 = new Player("Vattelappesca");
        g = new Game(1, 4, p1);
        g.setScoreBoard();
    }

    @Test
    void addPlayer() throws NumOfPlayersException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);

        assertEquals(p1.getGame(), g);
        assertEquals(p2.getGame(), g);
        assertEquals(p3.getGame(), g);
        assertEquals(p4.getGame(), g);
        assertTrue(g.getPlayers().contains(p1));
        assertTrue(g.getPlayers().contains(p2));
        assertTrue(g.getPlayers().contains(p3));
        assertTrue(g.getPlayers().contains(p4));
        assertThrows(NumOfPlayersException.class, ()->g.addPlayer(p5));
        assertFalse(g.getPlayers().contains(p5));
    }

    @Test
    void gameStartConstructorAndSetEndGame() throws NumOfPlayersException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);
        g.gameStartConstructor();

        assertEquals(38, g.getGoldDeck().getPool().size());
        assertEquals(38, g.getResourceDeck().getPool().size());
        assertEquals(2, g.getStarterDeck().getPool().size());
        assertEquals(16, g.getObjectiveDeck().getPool().size());
        assertFalse(g.getGoldDeck().getPool().contains(g.getGoldDeck().getGround0()));
        assertFalse(g.getGoldDeck().getPool().contains(g.getGoldDeck().getGround1()));
        assertFalse(g.getResourceDeck().getPool().contains(g.getResourceDeck().getGround0()));
        assertFalse(g.getResourceDeck().getPool().contains(g.getResourceDeck().getGround1()));
        assertFalse(g.getStarterDeck().getPool().contains(p1.getStarterCard()));
        assertFalse(g.getStarterDeck().getPool().contains(p2.getStarterCard()));
        assertFalse(g.getStarterDeck().getPool().contains(p3.getStarterCard()));
        assertFalse(g.getStarterDeck().getPool().contains(p4.getStarterCard()));
        assertNotNull(g.getScoreBoard());

        g.setEndGame(true);
        assertEquals(true, g.endGame);
    }

    @Test
    void drawSharedObjectiveCards() {
        g.setObjectiveDeck();
        ObjectiveCard oc1 = g.getObjectiveDeck().getPool().getFirst();
        ObjectiveCard oc2 = g.getObjectiveDeck().getPool().get(1);
        g.drawSharedObjectiveCards();

        assertTrue(g.getSharedObjectiveCards().contains(oc1));
        assertTrue(g.getSharedObjectiveCards().contains(oc2));
    }

    @Test
    void twoWinners() throws ColorTakenException, NumOfPlayersException, InvalidInputException {
//        g.addPlayer(p2);
//        g.addPlayer(p3);
//        g.addPlayer(p4);
//        g.gameStartConstructor();
//
//        p1.chooseStarterCardFace(true);
//        p2.chooseStarterCardFace(true);
//        p3.chooseStarterCardFace(true);
//        p4.chooseStarterCardFace(true);
//
//        p1.chooseColor(RED);
//        p2.chooseColor(GREEN);
//        p3.chooseColor(BLUE);
//        p4.chooseColor(YELLOW);
//
//        g.postColorSelectionSetUp();
//
//        p1.chooseObjectiveCard(1);
//        p2.chooseObjectiveCard(1);
//        p3.chooseObjectiveCard(1);
//        p4.chooseObjectiveCard(1);
//
//        g.getScoreBoard().addToPlayerScore(RED, 20);
//        g.getScoreBoard().addToPlayerScore(GREEN, 21);
//        g.getScoreBoard().addToPlayerScore(BLUE, 21);
//        g.getScoreBoard().addToPlayerScore(YELLOW, 19);
//
//        List<Player> winners = g.andTheWinnersAre();
//        assertEquals(false, winners.contains(p1));
//        assertEquals(true, winners.contains(p2));
//        assertEquals(true, winners.contains(p3));
//        assertEquals(false, winners.contains(p4));
    }

    @Test
    void oneWinner() throws ColorTakenException, NumOfPlayersException, InvalidInputException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);
        g.gameStartConstructor();

        p1.chooseStarterCardFace(true);
        p2.chooseStarterCardFace(true);
        p3.chooseStarterCardFace(true);
        p4.chooseStarterCardFace(true);

        p1.chooseColor(RED);
        p2.chooseColor(GREEN);
        p3.chooseColor(BLUE);
        p4.chooseColor(YELLOW);

        g.postColorSelectionSetUp();

        p1.chooseObjectiveCard(1);
        p2.chooseObjectiveCard(1);
        p3.chooseObjectiveCard(1);
        p4.chooseObjectiveCard(1);

        g.getScoreBoard().addToPlayerScore(RED, 20);
        g.getScoreBoard().addToPlayerScore(GREEN, 10);
        g.getScoreBoard().addToPlayerScore(BLUE, 21);
        g.getScoreBoard().addToPlayerScore(YELLOW, 19);

        List<Player> winners = g.andTheWinnersAre();
        assertEquals(false, winners.contains(p1));
        assertEquals(false, winners.contains(p2));
        assertEquals(true, winners.contains(p3));
        assertEquals(false, winners.contains(p4));
    }

    @Test
    void hashMapsGetters() throws NumOfPlayersException, ColorTakenException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);
        g.gameStartConstructor();

        p1.chooseStarterCardFace(true);
        p2.chooseStarterCardFace(true);
        p3.chooseStarterCardFace(true);
        p4.chooseStarterCardFace(true);

        p1.chooseColor(RED);
        p2.chooseColor(GREEN);
        p3.chooseColor(BLUE);
        p4.chooseColor(YELLOW);

        g.postColorSelectionSetUp();

        HashMap<String, Integer> nickAndStartersID = g.getNicksAndStartersIDs();
        HashMap<String, Symbol[]> playersAndColors = g.getPlayersCardsColors();
        HashMap<String, Boolean> playersStarterFacing = g.getPlayersStarterFacing();
        HashMap<String, Color> playersColors = g.getPlayersColors();
        assertNotNull(nickAndStartersID);
        assertNotNull(playersAndColors);
        assertNotNull(playersStarterFacing);
        assertNotNull(playersColors);
    }
}