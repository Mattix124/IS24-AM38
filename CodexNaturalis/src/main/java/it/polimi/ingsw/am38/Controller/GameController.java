package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import static it.polimi.ingsw.am38.Enum.GameStatus.CREATION;

public class GameController {
    /**
     * Game controlled by this class
     */
    private final Game game;
    /**
     * ID of the Game controlled by this class
     */
    private final int gameID;
    /**
     * maximum number of players that can participate in this.Game
     */
    private final int numOfPlayers;

    /**
     * Constructor of GameController
     * @param gameID pf the Game it controls
     * @param numOfPlayers max number of players, decided by the host that creates the game
     * @param host the Player creating this.Game
     */
    public GameController(int gameID, int numOfPlayers, Player host) {
        this.game = new Game(gameID, numOfPlayers, host);
        this.gameID = gameID;
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * method that allows the Player p to join this.Game
     * @param p Player that joins
     * @throws NumOfPlayersException when there is no room left in this.Game
     */
    public void joinGame(Player p) throws NumOfPlayersException {
        this.game.joinGame(p);
    }
    //-----------------------------------------------------------------------------------PLAYER METHODS

    /**
     * lets the Player choose their StarterCard facing
     * @param f true is Face-up, false is Face-down
     */
    public void chooseStarterCardFacing(boolean f){
        for (Player p : this.game.getPlayers()) {
            p.chooseStartingCardFace(f);
        }
    }

    /**
     * lets the Player choose their color
     * @param c the color chosen by the Player
     * @throws ColorTakenException if the Color has been taken
     */
    public void chooseColor(Color c) throws ColorTakenException {
        for (Player p : this.game.getPlayers()) {
            p.chooseColor(c);
        }
    }

    /**
     * lets the Player choose the ObjectiveCard they prefer out of the 2 drawn
     * @param i 1= first one, 2 = second one
     * @throws InvalidInputException if the input isn't valid
     */
    public void choosePersonalObjectiveCard(int i) throws InvalidInputException {
        for (Player p : this.game.getPlayers()) {
            p.chooseObjectiveCard(i);
        }
    }
    //-----------------------------------------------------------------------------------GETTERS

    /**
     * getter method for this Game
     * @return the Game this GameController controls
     */
    public Game getGame() {
        return this.game;
    }
}
