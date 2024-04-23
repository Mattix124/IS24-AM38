package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GoldDeckTest {

    GoldDeck goldDeck = new GoldDeck();
/*
    @Test
    void setUpGround() throws EmptyDeckException {
        GoldCard r1, r2;
        r1 = goldDeck.getPool().get(0);
        r2 = goldDeck.getPool().get(1);
        goldDeck.setUpGround();

        assertEquals(r1, goldDeck.getGround0());
        assertEquals(r2, goldDeck.getGround1());
        assertNotEquals(r1, goldDeck.getPool().getFirst());
        assertNotEquals(r2, goldDeck.getPool().getFirst());
        assertNotEquals(r1, r2);
    }
    @Test
    void draw() {
        goldDeck.setUpGround();
        GoldCard r;
        assertEquals(goldDeck.getPool().getFirst(),  r = goldDeck.draw());
    }

    @Test
    void drawFromGround() {
        goldDeck.setUpGround();
        GoldCard r1, r2, r3, r4;
        r1 = goldDeck.getGround0();
        r2 = goldDeck.getGround1();
        r3 = goldDeck.draw(0);
        r4 = goldDeck.draw(1);
        assertEquals(r1, r3);
        assertEquals(r2, r4);
        assertNotEquals(goldDeck.getPool().getFirst(), goldDeck.getGround0());
        assertNotEquals(goldDeck.getPool().getFirst(), goldDeck.getGround1());
        assertNotEquals(goldDeck.getPool().getFirst(), r3);
        assertNotEquals(goldDeck.getPool().getFirst(), r4);
    }
    @Test
    void getPool() {
        LinkedList<GoldCard> RD = goldDeck.getPool();

        for (int i = 0; i < 40; i++) {
            assertEquals(goldDeck.getPool().get(i), RD.get(i));
        }
    }

    @Test
    void getGround0AndGround1() {
        GoldCard r1 = goldDeck.getPool().get(0);
        GoldCard r2 = goldDeck.getPool().get(1);
        goldDeck.setUpGround();

        assertEquals(r1, goldDeck.getGround0());
        assertEquals(r2, goldDeck.getGround1());
    }*/
}