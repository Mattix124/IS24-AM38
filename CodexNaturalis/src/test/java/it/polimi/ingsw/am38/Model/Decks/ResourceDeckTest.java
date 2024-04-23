package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckTest {
    ResourceDeck resourceDeck = new ResourceDeck();
    /*
    @Test
    void setUpGround() throws EmptyDeckException {
        ResourceCard r1, r2;
        r1 = resourceDeck.getPool().get(0);
        r2 = resourceDeck.getPool().get(1);
        resourceDeck.setUpGround();

        assertEquals(r1, resourceDeck.getGround0());
        assertEquals(r2, resourceDeck.getGround1());
        assertNotEquals(r1, resourceDeck.getPool().getFirst());
        assertNotEquals(r2, resourceDeck.getPool().getFirst());
        assertNotEquals(r1, r2);
    }
    @Test
    void draw() {
        resourceDeck.setUpGround();
        ResourceCard r;
        assertEquals(resourceDeck.getPool().getFirst(),  r = resourceDeck.draw());
    }

    @Test
    void drawFromGround() {
        resourceDeck.setUpGround();
        ResourceCard r1, r2, r3, r4;
        r1 = resourceDeck.getGround0();
        r2 = resourceDeck.getGround1();
        r3 = resourceDeck.draw(0);
        r4 = resourceDeck.draw(1);
        assertEquals(r1, r3);
        assertEquals(r2, r4);
        assertNotEquals(resourceDeck.getPool().getFirst(), resourceDeck.getGround0());
        assertNotEquals(resourceDeck.getPool().getFirst(), resourceDeck.getGround1());
        assertNotEquals(resourceDeck.getPool().getFirst(), r3);
        assertNotEquals(resourceDeck.getPool().getFirst(), r4);
    }
    @Test
    void getPool() {
        LinkedList<ResourceCard> RD = resourceDeck.getPool();

        for (int i = 0; i < 40; i++) {
            assertEquals(resourceDeck.getPool().get(i), RD.get(i));
        }
    }

    @Test
    void getGround0AndGround1() {
        ResourceCard r1 = resourceDeck.getPool().get(0);
        ResourceCard r2 = resourceDeck.getPool().get(1);
        resourceDeck.setUpGround();

        assertEquals(r1, resourceDeck.getGround0());
        assertEquals(r2, resourceDeck.getGround1());
    }*/
}