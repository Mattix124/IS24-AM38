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
     * ID of the Game controlled by this class.
     */
    private final int gameID;
    /**
     * Maximum number of players that can participate in this.Game.
     */
    private final int numOfPlayers;
    /**
     * Index of the current Player, the one that is playing his turn.
     */
    private int currentPlayer = 0;
    /**
     * Keeps track of what turn is the Game on.
     */
    private int currentTurn;
    /**
     * initialized at the start of the endGamePhase and used to check its end
     */
    private int lastTurn;

    /**
     * Constructor of GameController.
     * @param gameID pf the Game it controls
     * @param numOfPlayers max number of players, decided by the host that creates the game
     * @param host the Player creating this.Game
     */
    public GameController(LobbyManager lobby, int gameID, int numOfPlayers, Player host) {
        this.lobby = lobby;
        this.game = new Game(gameID, numOfPlayers, host);
        this.gameID = gameID;
        this.numOfPlayers = numOfPlayers;
    }

    //-----------------------------------------------------------------------------------PLAYER METHODS

    /**
     * Method that will manage Player's actions
     * @throws EmptyDeckException (look at playerDraw)
     * @throws InvalidInputException (look at playerDraw)
     * @throws GameNotFoundException (look at passTurn)
     * @throws NotAFacingException (look at playerPlay)
     * @throws NotPlaceableException (look at playerPlay)
     */
    public void playerAction() throws EmptyDeckException, InvalidInputException, GameNotFoundException, NotAFacingException, NotPlaceableException {
        if(this.game.getCurrentPlayer().getIsPlaying()) {
            //start of currentPlayer's turn
            playerPlay();
            //when the Player has played a PlayableCard == has 2 left in his Hand
            playerDraw();
        }
        passTurn();
    }

    /**
     * method that manages the play (a PlayableCard) action of a Player
     * @throws NotAFacingException if the facing chosen for the PlayableCard is not valid
     * @throws NotPlaceableException if the positioning of chosen PlayableCard is not valid
     */
    public void playerPlay() throws NotAFacingException, NotPlaceableException {//tbd
        //
        Player p = this.game.getCurrentPlayer();
        String inPut1 = null;
        Integer cardToPlay = Integer.parseInt(inPut1);
        String face = null;
        boolean b;
        if(face == "face up")
            b = true;
        else if(face == "face down")
            b = false;
        else
            throw new NotAFacingException("You have to choose to play the card 'face up' or 'face down'");
        Coords c = null;
        p.playACard(cardToPlay, b, c);
    }

    /**
     * method that manages the draw action of a Player
     * @throws EmptyDeckException if the deck from which the player wants to draw from isEmpty
     * @throws InvalidInputException if the command given by the Player isn't a valid one
     */
    public void playerDraw() throws EmptyDeckException, InvalidInputException {//tbd
        Player p = this.game.getCurrentPlayer();
        String inPut2 = null;
        String typeCard = null;
        int i = Integer.parseInt(inPut2);
        if (typeCard == "gold"){
            this.game.getGoldDeck().draw(p, i);
        }else if(typeCard == "resource"){
            this.game.getResourceDeck().draw(p, i);
        }else throw new InvalidInputException("it should be 'draw gold/resource nothing/1/2'");
    }

    /**
     * Lets the Player choose their StarterCard facing
     * @param f true is Face-up, false is Face-down
     */
    public void chooseStarterCardFacing(boolean f) throws NotPlaceableException {
        for (Player p : this.game.getPlayers())
            p.chooseStartingCardFace(f);
    }

    /**
     * lets the Player choose their color and
     * calls postColorSelectionSetUp() if all Players have chosen their color
     * @param p Player that's choosing their color
     * @param c the color chosen by the Player
     */
    public void chooseColor(Player p, Color c) throws ColorTakenException, EmptyDeckException {//tbd
        p.chooseColor(c);
        if(this.game.getPlayers().stream()
                .filter(x -> x.getColor() == NONE)
                .toList()
                .isEmpty())
            postColorSelectionSetUp();
    }

    /**
     * lets the Player choose the ObjectiveCard they prefer out of the 2 drawn and
     * calls randomPlayerTurnOrder() if all Players in the Game have chosen their personal ObjectiveCard
     * @param p Player that's choosing their personal ObjectiveCard
     * @param i 1= first one, 2 = second one
     * @throws InvalidInputException if the input isn't valid
     */
    public void choosePersonalObjectiveCard(Player p, int i) throws InvalidInputException {
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
     * Player if they're not connected, stats a countdown timer if only one Player is connected and when the
     * end-Game phase ends announces the Winner(s)
     * @throws GameNotFoundException (look at endAGame)
     * @throws InvalidInputException (look at playerAction)
     * @throws EmptyDeckException (look at playerAction)
     * @throws NotAFacingException (look at playerAction)
     * @throws NotPlaceableException (look at playerAction)
     */
    private void passTurn() throws GameNotFoundException, InvalidInputException, EmptyDeckException, NotAFacingException, NotPlaceableException {
        if((this.game.getScoreBoard().getPlayerScores().get(game.getCurrentPlayer().getColor()) >= 20)
                || game.getGoldDeck().getPool().isEmpty() && game.getResourceDeck().getPool().isEmpty())
            lastTurn = currentTurn + 1;
        if(noPlayersConnected()) {
            this.lobby.endAGame(this.gameID);
            return;
        }
        do {
            nextPlayer();
            if(currentPlayer == 0)
                currentTurn++;
        }
        while(!game.getCurrentPlayer().getIsPlaying() && lastTurn >= currentTurn);
        if (disconnections() == numOfPlayers-1)
            game.standby();//tbd
        if (lastTurn < currentTurn) {
            List<Player> winners;
            winners = this.game.andTheWinnersAre();
            //communication with clients to announce the winner missing
        }else
            playerAction();
    }

    /**
     * Changes the currentPlayer to the next one for this class and the Game class connected.
     */
    private void nextPlayer(){
        currentPlayer = (currentPlayer + 1) % numOfPlayers;
        this.game.setCurrentPlayer(this.game.getPlayers().get(currentPlayer));
    }

    /**
     * Used to know how many Players are disconnected.
     * @return the number of disconnected Players
     */
    private long disconnections(){
        return this.game.getPlayers().stream()
                .filter(p-> !p.getIsPlaying())
                .count();
    }

    /**
     * Used to check if all Players are connected.
     * @return true is all Players are connected, false if there's at least one disconnected Player
     */
    private boolean noPlayersConnected(){
        return game.getPlayers().stream()
                .noneMatch(Player::getIsPlaying);
    }

    /**
     * method used to set each Player's hand and their pair of ObjectiveCards, also
     * calls drawSharedObjectiveCards() to set the 2 ObjectiveCards shared by all Players
     */
    private void postColorSelectionSetUp() throws EmptyDeckException {
        for (Player p : this.game.getPlayers()) {
            p.setFirstHand();
            p.drawPairObjectives();
        }
        this.getGame().drawSharedObjectiveCards();
    }

    /**
     * randomly decides the turn order of all Players
     */
    private void randomPlayerTurnOrder(){
        Collections.shuffle(this.game.getPlayers());
    }

    //-----------------------------------------------------------------------------------GETTERS

    /**
     * Getter method for this Game
     * @return the Game this GameController controls
     */
    public Game getGame() {
        return this.game;
    }
}
