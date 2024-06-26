package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.Color;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.am38.Enum.Color.*;


/**
 * Class dedicated to saving the score of every Player
 */
public class ScoreBoard {
    /**
     * Map used to keep the score of each Player (identified by the Color they chose for this Game)
     */
    private final Map<Color, Integer> playerScores;

    /**
     * Constructor of this class, sets all player's scores to 0 since it'll be created at the start of a Game
     */
    public ScoreBoard(){
        playerScores = new HashMap<>();
        playerScores.put(RED, 0);
        playerScores.put(GREEN, 0);
        playerScores.put(BLUE, 0);
        playerScores.put(YELLOW, 0);
    }

    /**
     * Given a color it returns the score of the Player connected to that Color in this Game
     *
     * @param color
     * @return int representing the score
     */
    public int getScore(Color color) {
        return playerScores.get(color);
    }

    /**
     * Updates the score of the Player, which color is given as an argument of this method
     *
     * @param color color of the Player who scored this points
     * @param numOfPoints number of points that needs to be added to the Player's score
     */
    public void addToPlayerScore(Color color, int numOfPoints) {
        this.playerScores.put(color, this.playerScores.get(color) + numOfPoints);
    }

    /**
     * Getter for the playerScores attribute
     *
     * @return the map of scores, which key is the Color of the player who owns those points
     */
    public Map<Color, Integer> getPlayerScores(){
        return this.playerScores;
    }
}
