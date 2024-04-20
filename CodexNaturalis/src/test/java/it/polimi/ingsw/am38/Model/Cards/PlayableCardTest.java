package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    GoldCard goldCard = new GoldCard(1,"animal",null,null,"corner",2,"animal","null","plant",
                                    "null","plant","animal","insect","fungi","animal","animal","fungi","null","null");


    @Test
    void getCorner() {
        GoldCard goldCard = new GoldCard(1,"animal",null,null,"corner",2,"animal","null","plant",
                "null","plant","animal","insect","fungi","animal","animal","fungi","null","null");

        goldCard.setFace(true);
        assertSame(Symbol.ANIMAL, goldCard.getCorner(Orientation.NW));
    }

    @Test
    void getOrder() {
    }

    @Test
    void getFace() {
    }

    @Test
    void getKingdom() {
    }

    @Test
    void getPointsWon() {
        GoldCard goldCard = new GoldCard(1,"animal",null,null,"corner",2,"animal","null","plant",
                "null","plant","animal","insect","fungi","animal","animal","fungi","null","null");

        assertEquals(2, goldCard.getPointsWon());
    }
}