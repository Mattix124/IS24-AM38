package Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    ResourceCard resourceCard = new ResourceCard(1,"fungi",null,null,3,"animal","insect","plant",
            "fungi","none","quill","manuscript","inkwell");


    @Test
    void resourceCardConstructor(){
        assertEquals(1,resourceCard.getCardID());
        assertEquals(Symbol.FUNGI, resourceCard.getKingdom());
        assertEquals(3, resourceCard.getPointsPerCondition());

        resourceCard.setFace(true);
        assertEquals(Symbol.ANIMAL, resourceCard.getCorner(Orientation.NW).getSymbol());
        assertEquals(Symbol.INSECT, resourceCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.PLANT, resourceCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.FUNGI, resourceCard.getCorner(Orientation.SE).getSymbol());

        resourceCard.setFace(false);
        assertEquals(Symbol.NULL, resourceCard.getCorner(Orientation.NW).getSymbol());
        assertEquals(Symbol.QUILL , resourceCard.getCorner(Orientation.NE).getSymbol());
        assertEquals(Symbol.MANUSCRIPT , resourceCard.getCorner(Orientation.SW).getSymbol());
        assertEquals(Symbol.INKWELL , resourceCard.getCorner(Orientation.SE).getSymbol());
    }

}