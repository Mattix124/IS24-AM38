package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    GoldCard goldCard = new GoldCard(1,"animal",null,null,"corner",2,"animal","null","plant",
            "none","insect","animal","insect","fungi","animal","animal","fungi","null","null");
    @Test
    void getCorner() {
        goldCard.setFace(false);
        System.out.printf(goldCard.getCorner(Orientation.NW).getSymbol().toString());
        assertSame(Symbol.INSECT, goldCard.getCorner(Orientation.SW).getSymbol());
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
       assertEquals(2, goldCard.getPointsWon());
    }
}