package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Model.Game;

import static it.polimi.ingsw.am38.Enum.GameStatus.CREATION;

public class GameController {
    private final Game game;
    private final int gameID;
    private final int numOfPlayers;

    public GameController(int gameID, int numOfPlayers) {
        this.game = new Game(gameID, numOfPlayers);
        this.gameID = gameID;
        this.numOfPlayers = numOfPlayers;
    }

    public Game getGame() {
        return game;
    }
}
