package Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    GoldCard goldCard = new GoldCard(01,"animal",null,null,"corner",2,"animal","insect","plant",
            "fungi","null","quill","manuscript","inkwell","animal","animal","fungi","null","null");


    @Test
    void goldCardConstructor(){
        assertEquals(1,goldCard.getCardID());
        assertEquals(Symbol.ANIMAL, goldCard.getKingdom());
        assertEquals(Symbol.CORNER, goldCard.getGoldPointsCondition());
        assertEquals(2, goldCard.getPointsPerCondition());

        goldCard.setFace(true);
        assertEquals(Symbol.ANIMAL , goldCard.getCorner(Orientation.NW).getSymbol());
        assertEquals(Symbol.INSECT , goldCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.PLANT , goldCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.FUNGI , goldCard.getCorner(Orientation.SE).getSymbol());

        goldCard.setFace(false);
        assertNull(goldCard.getCorner(Orientation.NW));
        assertEquals(Symbol.QUILL , goldCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.MANUSCRIPT , goldCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.INKWELL , goldCard.getCorner(Orientation.SE).getSymbol());

        Symbol[] s = {Symbol.ANIMAL, Symbol.ANIMAL, Symbol.FUNGI, null, null};
        assertArrayEquals(s, goldCard.getGoldPlayableCondition());
    }
}