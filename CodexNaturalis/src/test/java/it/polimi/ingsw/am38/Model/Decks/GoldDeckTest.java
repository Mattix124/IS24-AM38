package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GoldDeckTest {

    GoldDeck goldDeck = new GoldDeck();

    @Test
    void setUpGround(){
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
    void draw() throws EmptyDeckException{
        Player p = new Player("tommaso");
        goldDeck.setUpGround();
        GoldCard r1, r2, r3;
        r1 = goldDeck.getGround0();
        r2 = goldDeck.getGround1();
        goldDeck.draw(p, 0);
        goldDeck.draw(p,1);
        r3 = goldDeck.getPool().getFirst();
        goldDeck.draw(p);
        assertEquals(r1, p.getHand().getCard(0));
        assertEquals(r2, p.getHand().getCard(1));
        assertEquals(r3, p.getHand().getCard(2));
        assertNotEquals(goldDeck.getPool().getFirst(), goldDeck.getGround0());
        assertNotEquals(goldDeck.getPool().getFirst(), goldDeck.getGround1());
        assertNotEquals(goldDeck.getPool().getFirst(), p.getHand().getCard(0));
        assertNotEquals(goldDeck.getPool().getFirst(), p.getHand().getCard(1));
        assertNotEquals(goldDeck.getPool().getFirst(), p.getHand().getCard(2));
        goldDeck.getPool().removeAll(goldDeck.getPool());
        assertThrows(EmptyDeckException.class, ()->goldDeck.draw(p));
    }
    @Test
    void getPool() {
        LinkedList<GoldCard> RD = goldDeck.getPool();

        for (int i = 0; i < 40; i++) {
            assertEquals(goldDeck.getPool().get(i), RD.get(i));
        }
    }

    @Test
    void getGround0AndGround1(){
        GoldCard r1 = goldDeck.getPool().get(0);
        GoldCard r2 = goldDeck.getPool().get(1);
        goldDeck.setUpGround();

        assertEquals(r1, goldDeck.getGround0());
        assertEquals(r2, goldDeck.getGround1());
    }
}