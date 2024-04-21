package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    GoldCard goldCard = new GoldCard(01,"animal",null,null,"corner",2,"animal","insect","plant",
            "fungi","null","quill","manuscript","inkwell","animal","animal","fungi","null","null");

    @Test
    void getCorner() {
        goldCard.setFace(false);
        assertSame(Symbol.MANUSCRIPT, goldCard.getCorner(Orientation.SW).getSymbol());
    }

    @Test
    void getAndSetOrder() {
        assertEquals(0, goldCard.getOrder());

        goldCard.setOrder(1);
        assertEquals(1, goldCard.getOrder());
    }

    @Test
    void getFaceAndSetFace() {
        assertFalse(goldCard.getFace());

        goldCard.setFace(true);
        assertTrue(goldCard.getFace());

        goldCard.setFace(false);
        assertFalse(goldCard.getFace());
    }

    @Test
    void getKingdom() {
        assertEquals(Symbol.ANIMAL, goldCard.getKingdom());
    }

    @Test
    void getPointsWon() {
        assertEquals(2, goldCard.getPointsWon());
    }
}