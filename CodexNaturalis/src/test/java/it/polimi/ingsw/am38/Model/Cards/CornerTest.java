package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    void checkedGetterAndSetterTest() {
        Corner corner = new Corner("fungi");

        assertFalse(corner.isChecked());

        corner.setChecked(true);
        assertTrue(corner.isChecked());

        corner.setChecked(false);
        assertFalse(corner.isChecked());
    }

    @Test
    void occupiedGetterAndSetterTest() {
        Corner corner = new Corner("fungi");
        assertFalse(corner.isOccupied());

        corner.setOccupied(true);
        assertTrue(corner.isOccupied());

        corner.setOccupied(false);
        assertFalse(corner.isOccupied());
    }

    @Test
    void getSymbol() {
        Corner corner = new Corner("fungi");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("plant");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("animal");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("insect");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("quill");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("inkwell");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("manuscript");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("none");
        assertInstanceOf(Symbol.class, corner.getSymbol());

        corner = new Corner("null");
        assertNotEquals(Symbol.class, corner.getSymbol());
    }
}