package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckTest {
    ResourceDeck resourceDeck = new ResourceDeck();
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
    void draw() throws EmptyDeckException {
        Player p = new Player("tommaso");
        resourceDeck.setUpGround();
        ResourceCard r1, r2;
        r1 = resourceDeck.getGround0();
        r2 = resourceDeck.getGround1();
        resourceDeck.draw(p, 0);
        resourceDeck.draw(p,1);
        assertEquals(r1, p.getHand().getCard(0));
        assertEquals(r2, p.getHand().getCard(1));
        assertNotEquals(resourceDeck.getPool().getFirst(), resourceDeck.getGround0());
        assertNotEquals(resourceDeck.getPool().getFirst(), resourceDeck.getGround1());
        assertNotEquals(resourceDeck.getPool().getFirst(), p.getHand().getCard(0));
        assertNotEquals(resourceDeck.getPool().getFirst(), p.getHand().getCard(1));
    }
    @Test
    void getPool() {
        LinkedList<ResourceCard> RD = resourceDeck.getPool();

        for (int i = 0; i < 40; i++) {
            assertEquals(resourceDeck.getPool().get(i), RD.get(i));
        }
    }

    @Test
    void getGround0AndGround1() throws EmptyDeckException {
        ResourceCard r1 = resourceDeck.getPool().get(0);
        ResourceCard r2 = resourceDeck.getPool().get(1);
        resourceDeck.setUpGround();

        assertEquals(r1, resourceDeck.getGround0());
        assertEquals(r2, resourceDeck.getGround1());
    }
}