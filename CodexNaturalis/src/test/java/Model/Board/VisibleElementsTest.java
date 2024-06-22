package Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisibleElementsTest {


    @Test
    void getSymbol() {
        VisibleElements ve = new VisibleElements();

        assertEquals(0, ve.get(Symbol.ANIMAL));
        assertEquals(0, ve.get(Symbol.PLANT));
        assertEquals(0, ve.get(Symbol.INSECT));
        assertEquals(0, ve.get(Symbol.FUNGI));
        assertEquals(0, ve.get(Symbol.INKWELL));
        assertEquals(0, ve.get(Symbol.QUILL));
        assertEquals(0, ve.get(Symbol.MANUSCRIPT));
    }

    VisibleElements ve = new VisibleElements();
    @Test
    void increaseSymbol() {
        ve.increaseSymbol(Symbol.ANIMAL);
        ve.increaseSymbol(Symbol.PLANT);
        ve.increaseSymbol(Symbol.INSECT);
        ve.increaseSymbol(Symbol.FUNGI);
        ve.increaseSymbol(Symbol.INKWELL);
        ve.increaseSymbol(Symbol.QUILL);
        ve.increaseSymbol(Symbol.MANUSCRIPT);

        assertEquals(1, ve.get(Symbol.ANIMAL));
        assertEquals(1, ve.get(Symbol.PLANT));
        assertEquals(1, ve.get(Symbol.INSECT));
        assertEquals(1, ve.get(Symbol.FUNGI));
        assertEquals(1, ve.get(Symbol.INKWELL));
        assertEquals(1, ve.get(Symbol.QUILL));
        assertEquals(1, ve.get(Symbol.MANUSCRIPT));
    }

    @Test
    void IncreaseDecreaseSymbol() {
        ve.increaseSymbol(Symbol.ANIMAL, 2);
        assertEquals(2, ve.get(Symbol.ANIMAL));
        ve.increaseSymbol(Symbol.PLANT, 3);
        assertEquals(3, ve.get(Symbol.PLANT));
        ve.increaseSymbol(Symbol.INSECT, 1);
        assertEquals(1, ve.get(Symbol.INSECT));
        ve.increaseSymbol(Symbol.FUNGI, 4);
        assertEquals(4, ve.get(Symbol.FUNGI));
        ve.increaseSymbol(Symbol.INKWELL, 1);
        assertEquals(1, ve.get(Symbol.INKWELL));
        ve.increaseSymbol(Symbol.QUILL, 0);
        assertEquals(0, ve.get(Symbol.QUILL));
        ve.increaseSymbol(Symbol.MANUSCRIPT, 1);
        assertEquals(1, ve.get(Symbol.MANUSCRIPT));

        ve.increaseSymbol(Symbol.FUNGI, -1);
        assertEquals(3, ve.get(Symbol.FUNGI));
        ve.increaseSymbol(Symbol.FUNGI, -2);
        assertEquals(1, ve.get(Symbol.FUNGI));
    }
}