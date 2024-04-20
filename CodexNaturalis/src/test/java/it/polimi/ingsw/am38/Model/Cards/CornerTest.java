package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"plant","animal","fungi","insect","quill","inkwell","manuscript","none","null"})
    void getSymbol(String s) {
        if(!s.equals("null")) {
            Corner corner = new Corner(s);
            assertInstanceOf(Symbol.class, corner.getSymbol());
        }else{
            Corner corner = new Corner(s);
            assertNull(corner.getSymbol());
        }
    }
}