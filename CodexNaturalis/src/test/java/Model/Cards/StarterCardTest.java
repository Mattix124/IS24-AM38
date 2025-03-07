package Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest {

    StarterCard starterCard = new StarterCard(1,null,null,
            "null","none","animal","insect",
            "plant","fungi","none","quill",
            "animal","fungi","plant");


    @Test
    void starterCardConstructor()
    {
        assertEquals(1,starterCard.getCardID());

        starterCard.setFace(true);
        assertNull(starterCard.getCorner(Orientation.NW));
        assertEquals(Symbol.NULL, starterCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.ANIMAL, starterCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.INSECT, starterCard.getCorner(Orientation.SE).getSymbol());

        starterCard.setFace(false);
        assertEquals(Symbol.PLANT, starterCard.getCorner(Orientation.NW).getSymbol());
        assertEquals(Symbol.FUNGI , starterCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.NULL , starterCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.QUILL , starterCard.getCorner(Orientation.SE).getSymbol());

        starterCard.setFace(true);
        Symbol[] sTrue = {Symbol.ANIMAL, Symbol.FUNGI, Symbol.PLANT};
        assertArrayEquals(sTrue, starterCard.getCentralKingdom());

        starterCard.setFace(false);
        Symbol[] sFalse = {null, null, null};
        assertArrayEquals(sFalse, starterCard.getCentralKingdom());
    }
}