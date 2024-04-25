package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import static it.polimi.ingsw.am38.Enum.GameStatus.CREATION;
import static it.polimi.ingsw.am38.Enum.GameStatus.ENDGAME;

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
     * method that manages the play action of a Player
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
        Integer i = Integer.parseInt(inPut2);
        if (typeCard == "gold"){
            this.game.getGoldDeck().draw(p, i);
        }else if(typeCard == "resource"){
            this.game.getResourceDeck().draw(p, i);
        }else throw new InvalidInputException("it should be 'draw gold/resource nothing/1/2'");
    }

    /**
     * Method that allows the Player p to join this.Game.
     * @param p Player that joins
     * @throws NumOfPlayersException when there is no room left in this.Game
     */
    public void joinGame(Player p) throws NumOfPlayersException, EmptyDeckException {
        this.game.addPlayer(p);
        if(this.game.getPlayers().size() == numOfPlayers)
            this.game.gameStartConstructor();
    }

    /**
     * Lets the Player choose their StarterCard facing.
     * @param f true is Face-up, false is Face-down
     */
    public void chooseStarterCardFacing(boolean f) throws NotPlaceableException {
        for (Player p : this.game.getPlayers()) {
            p.chooseStartingCardFace(f);
        }
    }

    /**
     * Lets the Player choose their color.
     * @param c the color chosen by the Player
     * @throws ColorTakenException if the Color has been taken
     */
    public void chooseColor(Color c) throws ColorTakenException {
        for (Player p : this.game.getPlayers()) {
            p.chooseColor(c);
        }
    }

    /**
     * Lets the Player choose the ObjectiveCard they prefer out of the 2 drawn.
     * @param i 1= first one, 2 = second one
     * @throws InvalidInputException if the input isn't valid
     */
    public void choosePersonalObjectiveCard(int i) throws InvalidInputException {
        for (Player p : this.game.getPlayers()) {
            p.chooseObjectiveCard(i);
        }
    }
    //-----------------------------------------------------------------------------------PRIVATE METHODS

    /**
     * Turn handler, when a player draws it's triggered to change the currentPlayer to the next one.
     * @throws GameNotFoundException if the method used in it fails
     */
    private void passTurn() throws GameNotFoundException, InvalidInputException, EmptyDeckException, NotAFacingException, NotPlaceableException {
        if(noPlayersConnected()) {
            this.lobby.endAGame(this.gameID);
            return;
        }
        do {
            nextPlayer();
            if(currentPlayer == 0)
                currentTurn++;
        }
        while(!game.getCurrentPlayer().getIsPlaying());
        if (disconnections() == numOfPlayers-1)
            game.standby();
        playerAction();
        int lastTurn;
        if((this.game.getScoreBoard().getPlayerScores().get(game.getCurrentPlayer().getColor()) >= 20)
                || game.getGoldDeck().getPool().isEmpty() && game.getResourceDeck().getPool().isEmpty())
            lastTurn = currentTurn + 1;
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

    //-----------------------------------------------------------------------------------GETTERS

    /**
     * Getter method for this Game
     * @return the Game this GameController controls
     */
    public Game getGame() {
        return this.game;
    }
}
