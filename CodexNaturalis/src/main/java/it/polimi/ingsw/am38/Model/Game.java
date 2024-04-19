package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Exception.MaxNumberOfPlayersException;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import it.polimi.ingsw.am38.Model.Miscellaneous.ScoreBoard;

import java.util.ArrayList;
import java.util.Random;

import static it.polimi.ingsw.am38.Enum.GameStatus.CREATION;
import static it.polimi.ingsw.am38.Enum.GameStatus.START;

/**
 * the Game class, dedicated to all and each game related actions and information
 */
public class Game{
	/**
	 * array of Players taking part in this Game
	 */
	private ArrayList<Player> players;
	/**
	 * used to save the index (of the ArrayList of players) referring to the starting Player
	 */
	private final int startingPlayer;
	/**
	 * the ID of this Game
	 */
	private int gameID;
	/**
	 * the ScoreTrack linked to this Game, it keeps count of every Player's score
	 */
	private ScoreBoard scoreBoard;
	/**
	 * enum describing the phase the Game is in at any given time
	 */
	private GameStatus status;
	/**
	 * number of players allowed in this Game (chosen by the Player creating the Game)
	 */
	private final int numPlayers;
	/**
	 * array of 40 gold cards
	 */
	private GoldDeck goldDeck;
	/**
	 * array of 40 resource cards
	 */
	private ResourceDeck resourceDeck;
	/**
	 * array of 12 Objective cards
	 */
	private ObjectiveDeck objectiveDeck;
	/**
	 * array of 6 starting cards
	 */
	private StarterDeck starterDeck;

	/**
	 * constructor for the Game class
	 * @param gameID unique ID (who calls the method will make sure the ID chosen is not being used already)
	 * @param numPlayers  maximum amount of players allowed in the Game
	 */
    public Game(int gameID, int numPlayers) {
        startingPlayer = (int)(Math.random() * numPlayers);
		this.gameID = gameID;
        this.numPlayers = numPlayers;
		this.status = CREATION;
	}

	/**
	 * getter for gameID
	 * @return gameID
	 */

	public int getGameID() {
		return gameID;
	}

	/**
	 * method used to link a Player to this Game, when the last player joins the decks are generated and shuffled,
	 * the Game also changes State (from Creation to Start)
	 * @param player the Player who's trying to join this Game
	 * @throws MaxNumberOfPlayersException tells the Player there's no more room in this Game
	 */
	public void joinGame(Player player) throws MaxNumberOfPlayersException {
		if(this.getStatus() == CREATION){
			player.setGame(this);
			this.players.add(player);
			if (players.size() == numPlayers) {
				goldDeck = new GoldDeck();
				resourceDeck = new ResourceDeck();
				starterDeck = new StarterDeck();
				objectiveDeck = new ObjectiveDeck();
				this.setStatus(START);
			}
		}else
			throw new MaxNumberOfPlayersException("It's too late to join this game, try a different one!");
	}

	/**
	 * getter of gameStatus attribute
	 * @return status of the Game
	 */
	public GameStatus getStatus() {
		return status;
	}

	/**
	 * setter of gameStatus attribute
	 * @param status of the Game
	 */
	private void setStatus(GameStatus status) {
		this.status = status;
	}
}
