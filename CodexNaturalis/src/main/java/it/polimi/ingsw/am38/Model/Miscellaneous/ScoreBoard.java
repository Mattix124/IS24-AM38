package it.polimi.ingsw.am38.Model.Miscellaneous;

import it.polimi.ingsw.am38.Enum.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * class dedicated to saving the score of every Player
 */
public class ScoreBoard {
    /**
     * map used to keep the score of each Player (identified by the Color they chose for this Game)
     */
    private Map<Color, Integer> playerScores;

    /**
     * constructor of this class, sets all player's scores to 0 since it'll be created at the start of a Game
     */
    public ScoreBoard(){
        playerScores = new HashMap<>();
        playerScores.put(Color.RED, 0);
        playerScores.put(Color.GREEN, 0);
        playerScores.put(Color.BLUE, 0);
        playerScores.put(Color.YELLOW, 0);
    }

    /**
     * given a color it returns the score of the Player connected to that Color in this Game
     * @param color
     * @return int representing the score
     */
    public int getScore(Color color) {
        return playerScores.get(color);
    }

    /**
     * updates the score of the Player, which color is given as an argument of this method
     * @param color color of the Player who scored this points
     * @param numOfPoints number of points that needs to be added to the Player's score
     */
    public void addPlayerScore(Color color, int numOfPoints) {
        this.playerScores.put(color, this.playerScores.get(color) + numOfPoints);
    }
}
