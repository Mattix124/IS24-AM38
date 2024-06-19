package Model.Decks;

import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class StarterDeckTest {

    StarterDeck starterDeck = new StarterDeck();
    @Test
    void getStarter() {
        StarterCard s1, s2;
        s1 = starterDeck.getPool().getFirst();
        s2 = starterDeck.drawStarterCard();
        assertEquals(s1, s2);
        assertNotEquals(s2, starterDeck.getPool().getFirst());
    }

    @Test
    void getPool() {
        LinkedList<StarterCard> SD = starterDeck.getPool();

        for (int i = 0; i < 6; i++) {
            assertEquals(starterDeck.getPool().get(i), SD.get(i));
        }
    }
}