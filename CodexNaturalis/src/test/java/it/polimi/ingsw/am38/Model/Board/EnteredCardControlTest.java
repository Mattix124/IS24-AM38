package it.polimi.ingsw.am38.Model.Board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnteredCardControlTest {
    EnteredCardControl ecc = new EnteredCardControl(0,0);

    @Test
    void getEnteredCard() {
        assertEquals(0, ecc.getEnteredCard());
    }

    @Test
    void increaseEnteredCard() {
        ecc.increaseEnteredCard();
        assertEquals(1, ecc.getEnteredCard());
    }

    @Test
    void getCheckedAngle() {
        assertEquals(0, ecc.getCheckedAngle());
    }

    @Test
    void increaseCheckedAngle() {
        ecc.increaseCheckedAngle();
        assertEquals(1, ecc.getCheckedAngle());
    }
}