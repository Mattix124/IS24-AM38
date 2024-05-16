package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.am38.Enum.Color.NONE;

/**
 * controller class for a single Game
 */
public class GameController {
    private final LobbyManager lobby;
    /**
     * Game controlled by this class.
     */
    private final Game game;
    /**
     * Index of the current Player, the one that is playing his turn.
     */
    private int currentPlayer;
    /**
     * Keeps track of what turn is the Game on.
     */
    private int currentTurn = 0;
    /**
     * initialized at the start of the endGamePhase and used to check its end
     */
    private int lastTurn = 0;
    /**
     * flag used to know if evey Played has chosen a Color
     */
    private boolean f = false;
    /**
     * list of all the winners of this game
     */
    private List<Player> winners;

    /**
     * Constructor of GameController.
     * @param game the one this Controller manages
     */
    public GameController(LobbyManager lobby, Game game) {
        this.lobby = lobby;
        this.game = game;
        this.currentPlayer = game.getNumPlayers()-1;
    }

    //-----------------------------------------------------------------------------------PLAYER METHODS

    /**
     * method that manages the play (a PlayableCard) action of a Player
     * @throws InvalidInputException if the facing chosen for the PlayableCard is not valid
     * @throws NoPossiblePlacement if the positioning of chosen PlayableCard is not valid
     */
    public void playerPlay(int card, int x, int y, boolean b) throws InvalidInputException, NoPossiblePlacement, NotPlaceableException
	{
        game.getCurrentPlayer().playACard(card, b, new Coords(x, y));
    }

    /**
     * method that manages the draw action of a Player
     * @throws EmptyDeckException if the deck from which the player wants to draw from isEmpty
     * @throws InvalidInputException if the command given by the Player isn't a valid one
     */
    public void playerDraw(String type, int index) throws EmptyDeckException, InvalidInputException {
        if (type.equals("gold"))
            this.game.getGoldDeck().draw(game.getCurrentPlayer(), index);
        else if (type.equals("resource"))
            this.game.getResourceDeck().draw(game.getCurrentPlayer(), index);
        passTurn();
    }

    /**
     * Lets the Player choose their StarterCard facing
     * @param p Player whose choosing
     * @param f true is Face-up, false is Face-down
     */
    public void chooseStarterCardFacing(Player p, boolean f){
        p.chooseStarterCardFace(f);
    }

    /**
     * lets the Player choose their color and
     * calls postColorSelectionSetUp() if all Players have chosen their color
     * @param p Player that's choosing their color
     * @param c the color chosen by the Player
     */
    public synchronized void chooseColor(Player p, Color c) throws ColorTakenException {//tbd
        p.chooseColor(c);
        if (this.game.getPlayers().stream()
                .filter(x -> x.getColor() == NONE)
                .toList()
                .isEmpty()){
            this.game.postColorSelectionSetUp();
            f = true;
        }
    }

    /**
     * lets the Player choose the ObjectiveCard they prefer out of the 2 drawn and
     * calls randomPlayerTurnOrder() if all Players in the Game have chosen their personal ObjectiveCard
     * @param p Player that's choosing their personal ObjectiveCard
     * @param i 1= first one, 2 = second one
     * @throws Exception (look at chooseObjectiveCard and randomPlayerTurn)
     */
    public void choosePersonalObjectiveCard(Player p, int i) throws Exception {
        p.chooseObjectiveCard(i);
        if(this.game.getPlayers().stream()
                .filter(x -> x.getObjectiveCard() == null)
                .toList()
                .isEmpty())
            randomPlayerTurnOrder();
    }

    //-----------------------------------------------------------------------------------PRIVATE METHODS

    /**
     * method that manages the turns flow and execution: checks for end-Game phase conditions, shuts down
     * the Game if no Players are connected to it, handles the passing of turns and skips the turn of a
     * Player if they're not connected, starts a countdown timer if only one Player is connected and when the
     * end-Game phase ends announces the Winner(s)
     */
    private void passTurn(){
        if(((this.game.getScoreBoard().getPlayerScores().get(game.getCurrentPlayer().getColor()) >= 20)
                || game.getGoldDeck().getPool().isEmpty() && game.getResourceDeck().getPool().isEmpty()) && lastTurn != 0) {
            lastTurn = currentTurn + 1;//+ a message letting players know it's the end game phase (tbd)
            game.setEndGame(true);
        }
        if(noPlayersConnected())
            this.lobby.endAGame(this.game);
        do {
            nextPlayer();
            if(currentPlayer == 0)
                currentTurn++;
        }
        while((!game.getCurrentPlayer().isPlaying() || game.getCurrentPlayer().isStuck()) && (lastTurn >= currentTurn || lastTurn == 0));
        if (disconnections() == this.game.getNumPlayers()-1)
            game.standby();//tbd
        if (lastTurn < currentTurn || lastTurn == 0) {
            this.winners = this.game.andTheWinnersAre();
        }
    }

    /**
     * Changes the currentPlayer to the next one for this class and the Game class connected.
     */
    private void nextPlayer(){
        currentPlayer = (currentPlayer + 1) % this.game.getNumPlayers();
        this.game.setCurrentPlayer(this.game.getPlayers().get(currentPlayer));
    }

    /**
     * Used to know how many Players are disconnected.
     * @return the number of disconnected Players
     */
    private long disconnections(){
        return this.game.getPlayers().stream()
                .filter(p-> !p.isPlaying())
                .count();
    }

    /**
     * Used to check if all Players are connected.
     * @return true is all Players are connected, false if there's at least one disconnected Player
     */
    private boolean noPlayersConnected(){
        return game.getPlayers().stream()
                .noneMatch(Player::isPlaying);
    }

    /**
     * randomly decides the turn order of all Players and starts the first Player's first turn
     * @throws Exception (look at passTurn)
     */
    private void randomPlayerTurnOrder() throws Exception {
        Collections.shuffle(this.game.getPlayers());
        this.game.setCurrentPlayer(this.game.getPlayers().getFirst());
        passTurn();
    }

    //-----------------------------------------------------------------------------------GETTERS

    /**
     * Getter method for this Game
     * @return the Game this GameController controls
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * getter for f attribute
     * @return f
     */
    public boolean isF() {
        return f;
    }

    public List<Player> getWinners() {
        return winners;
    }
}
