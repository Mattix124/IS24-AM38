package it.polimi.ingsw.am38.View;

/**
 * implemented by both CLI/TUI and GUI, contains all the methods they implement
 */
public interface Viewable {
    /**
     * shows the field of the Player having the chosen nickname
     * @param nickname a String containing the name of the Player whose field is being requested
     */
    void showPlayerField(String nickname);

    /**
     * updates the score of the Player who just ended their turn (4 times each round)
     * @param nickname a String containing the name of the Player whose turn just ended
     * @param score an int representing the Score of the given Player
     */
    void updateScore(String nickname, int score);

    /**
     * updates the Hand of the Player after he plays a card and draws a card (tbd, maybe 2 different methods)
     */
    void updateHand(/*tbd*/);

    //...many more missing
}
