package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VisibleElementsTest {


    @Test
    void getSymbol() {
        VisibleElements ve = new VisibleElements(0,0,0,0,0,0,0);

        assertEquals(0, ve.getSymbol(Symbol.ANIMAL));
        assertEquals(0, ve.getSymbol(Symbol.PLANT));
        assertEquals(0, ve.getSymbol(Symbol.INSECT));
        assertEquals(0, ve.getSymbol(Symbol.FUNGI));
        assertEquals(0, ve.getSymbol(Symbol.INKWELL));
        assertEquals(0, ve.getSymbol(Symbol.QUILL));
        assertEquals(0, ve.getSymbol(Symbol.MANUSCRIPT));
    }

    VisibleElements ve = new VisibleElements(0,0,0,0,0,0,0);
    @Test
    void increaseSymbol() {
        ve.increaseSymbol(Symbol.ANIMAL);
        ve.increaseSymbol(Symbol.PLANT);
        ve.increaseSymbol(Symbol.INSECT);
        ve.increaseSymbol(Symbol.FUNGI);
        ve.increaseSymbol(Symbol.INKWELL);
        ve.increaseSymbol(Symbol.QUILL);
        ve.increaseSymbol(Symbol.MANUSCRIPT);

        assertEquals(1, ve.getSymbol(Symbol.ANIMAL));
        assertEquals(1, ve.getSymbol(Symbol.PLANT));
        assertEquals(1, ve.getSymbol(Symbol.INSECT));
        assertEquals(1, ve.getSymbol(Symbol.FUNGI));
        assertEquals(1, ve.getSymbol(Symbol.INKWELL));
        assertEquals(1, ve.getSymbol(Symbol.QUILL));
        assertEquals(1, ve.getSymbol(Symbol.MANUSCRIPT));
    }

    @Test
    void IncreaseDecreaseSymbol() {
        ve.increaseSymbol(Symbol.ANIMAL, 2);
        assertEquals(2, ve.getSymbol(Symbol.ANIMAL));
        ve.increaseSymbol(Symbol.PLANT, 3);
        assertEquals(3, ve.getSymbol(Symbol.PLANT));
        ve.increaseSymbol(Symbol.INSECT, 1);
        assertEquals(1, ve.getSymbol(Symbol.INSECT));
        ve.increaseSymbol(Symbol.FUNGI, 4);
        assertEquals(4, ve.getSymbol(Symbol.FUNGI));
        ve.increaseSymbol(Symbol.INKWELL, 1);
        assertEquals(1, ve.getSymbol(Symbol.INKWELL));
        ve.increaseSymbol(Symbol.QUILL, 0);
        assertEquals(0, ve.getSymbol(Symbol.QUILL));
        ve.increaseSymbol(Symbol.MANUSCRIPT, 1);
        assertEquals(1, ve.getSymbol(Symbol.MANUSCRIPT));

        ve.increaseSymbol(Symbol.FUNGI, -1);
        assertEquals(3, ve.getSymbol(Symbol.FUNGI));
        ve.increaseSymbol(Symbol.FUNGI, -2);
        assertEquals(1, ve.getSymbol(Symbol.FUNGI));
    }
}