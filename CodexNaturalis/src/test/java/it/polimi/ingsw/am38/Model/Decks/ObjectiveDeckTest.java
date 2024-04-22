package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveDeckTest {
    ObjectiveDeck objectiveDeck = new ObjectiveDeck();

    @Test
    void drawTwo() {
        LinkedList<ObjectiveCard> objList1 = new LinkedList<>();
        objList1.add(objectiveDeck.getPool().get(0));
        objList1.add(objectiveDeck.getPool().get(1));
        LinkedList<ObjectiveCard> objList2;
        objList2 = objectiveDeck.drawTwo();
        assertEquals(objList1, objList2);
    }

    @Test
    void getPool() {
        LinkedList<ObjectiveCard> OD = objectiveDeck.getPool();

        for (int i = 0; i < 16; i++) {
            assertEquals(objectiveDeck.getPool().get(i), OD.get(i));
        }
    }
}