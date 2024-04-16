package it.polimi.ingsw.am38.Enum;

public enum GameStatus {
    /**
     * when a Player creates a new Game and sets the number of Players needed to start (from 2 to 4),
     * ends when the number of expected Players is met
     */
    CREATION,
    /**
     * the Game starts and the decks/boards are set up, this phase includes: Players choosing which side to
     * play their starting card, choosing a color, drawing 2 resource and 1 gold card, choosing one of
     *  the 2 possible secret objectives and establishing who is the first Player to act
     */
    START,
    /**
     * phase of a turn where the Player has to place a Card on his Board
     */
    PLAY,
    /**
     * phase of a turn where the Player has to draw a card from the 6 options (2 random, 4 face-up)
     */
    DRAW,
    /**
     * triggered when a Player reaches 20 points or the 2 main decks are empty.
     * as soon as the starting Player starts his next turn, the Game will let each Player have one last
     * turn before the winner is declared
     */
    ENDGAME,
    /**
     * when the game is over or after only one Player is left and a timeout expires
     * every process and thread is ended/freed
     */
    SHUTDOWN,
    /**
     * whenever there's a single Player left (everyone else is disconnected), a timeout starts
     */
    STANDBY,
    /**
     * if the server crashes, Game ends and each Player is notified
     */
    CRASH,
}
