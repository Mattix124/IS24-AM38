package it.polimi.ingsw.am38.Model.Board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordsTest {
    Coords c = new Coords(0,0);

    @Test
    void x() {
        assertEquals(0, c.x());
    }

    @Test
    void y() {
        assertEquals(0, c.y());
    }

    @Test
    void setX() {
        c.setX(2);
        assertEquals(2, c.x());
    }

    @Test
    void setY() {
        c.setY(2);
        assertEquals(2, c.y());
    }
}