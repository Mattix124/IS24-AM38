package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
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
    void gameStartConstructor() throws NumOfPlayersException {
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
    void standby() {//tbd
    }

    /*@Test
    void andTheWinnersAre() throws ColorTakenException, NumOfPlayersException, InvalidInputException {
        g.addPlayer(p2);
        g.addPlayer(p3);
        g.addPlayer(p4);
        g.setObjectiveDeck();
        g.setScoreBoard();
        g.setGoldAndResourceDecks();

        p1.setStarterCard(new StarterCard(1,null,null,"none","none","animal","insect","plant",
                "fungi","none","quill","animal","fungi","plant"));
        p2.setStarterCard(new StarterCard(1,null,null,"none","none","animal","insect","plant",
                "fungi","none","quill","animal","fungi","plant"));
        p3.setStarterCard(new StarterCard(1,null,null,"manuscript","manuscript","manuscript","manuscript","plant",
                "fungi","none","quill","animal","fungi","plant"));
        p4.setStarterCard(new StarterCard(1,null,null,"fungi","fungi","fungi","fungi","plant",
                "fungi","none","quill","animal","fungi","plant"));
        p1.chooseStarterCardFace(true);
        p2.chooseStarterCardFace(true);
        p3.chooseStarterCardFace(true);
        p4.chooseStarterCardFace(true);
        p1.chooseColor(RED);
        p2.chooseColor(YELLOW);
        p3.chooseColor(BLUE);
        p4.chooseColor(GREEN);
        g.postColorSelectionSetUp();
        p1.setObjectiveCard(new ObjectiveCard(1,"animal","diagonal",null,null, 2,"null","SE","null"));
        p2.setObjectiveCard(new ObjectiveCard(1,"fungi","shapeL",null,null, 2,"insect","SE","null"));
        p3.setObjectiveCard(new ObjectiveCard(1,"null","duo",null,null, 2,"null","null","manuscript"));
        p4.setObjectiveCard(new ObjectiveCard(1,"fungi","trio",null,null, 2,"null","null","null"));

        g.getScoreBoard().addToPlayerScore(RED, 22);
        g.getScoreBoard().addToPlayerScore(YELLOW, 15);
        g.getScoreBoard().addToPlayerScore(BLUE, 10);
        g.getScoreBoard().addToPlayerScore(GREEN, 22);

        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p1.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());

        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());
        p2.getGameField().tryPlaceCard(new ResourceCard(1, "animal", null, null,
                0, "none","none", "none","none","none","none",
                "none","none"), new Coords());


    }*/

    @Test
    void setCurrentPlayer() {
    }

    @Test
    void getGameID() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getGoldDeck() {
    }

    @Test
    void getResourceDeck() {
    }

    @Test
    void getObjectiveDeck() {
    }

    @Test
    void getObjectiveCard() {
    }

    @Test
    void getScoreBoard() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void getNumPlayers() {
    }

    @Test
    void setGoldAndResourceDecks() {
    }

    @Test
    void setScoreBoard() {
    }
}