package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    GoldCard goldCard = new GoldCard(1,"animal",null,null,"corner",2,"animal","null","plant",
            "null","plant","animal","insect","fungi","animal","animal","fungi","null","null");



    @Test
    void getGoldPlayableCondition() {
        Symbol[] symbol = {Symbol.ANIMAL, Symbol.ANIMAL, Symbol.FUNGI, Symbol.NULL, Symbol.NULL};
        assertSame(symbol, goldCard.getGoldPlayableCondition());
    }

    @Test
    void getGoldPointsCondition() {
    }
}