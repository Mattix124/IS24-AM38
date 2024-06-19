package Model;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.ScoreBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.am38.Enum.Color.*;
import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {
    ScoreBoard sc;

    @BeforeEach
    void setUp(){
        sc = new ScoreBoard();
    }


    @Test
    void getScore() {
        assertEquals(sc.getScore(RED), 0);
        assertEquals(sc.getScore(YELLOW), 0);
        assertEquals(sc.getScore(GREEN), 0);
        assertEquals(sc.getScore(BLUE), 0);
    }

    @Test
    void addToPlayerScore() {
        sc.addToPlayerScore(RED, 10);
        sc.addToPlayerScore(YELLOW, 3);
        sc.addToPlayerScore(BLUE, 7);
        sc.addToPlayerScore(GREEN, 5);

        assertEquals(sc.getScore(RED), 10);
        assertEquals(sc.getScore(YELLOW), 3);
        assertEquals(sc.getScore(BLUE), 7);
        assertEquals(sc.getScore(GREEN), 5);
    }

    @Test
    void getPlayerScores() {
        Map<Color, Integer> scores = sc.getPlayerScores();

        assertTrue(scores.remove(RED, 0));
        assertTrue(scores.remove(YELLOW, 0));
        assertTrue(scores.remove(GREEN, 0));
        assertTrue(scores.remove(BLUE, 0));
    }
}