package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Exception.MaxNumberOfPlayersException;
import it.polimi.ingsw.am38.Exception.NotYourDrawPhaseException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import it.polimi.ingsw.am38.Model.Miscellaneous.ScoreBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;

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
	private Player startingPlayer;
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
	 * list of the 2 shared ObjectiveCard, every Player can score points with them
	 */
	private LinkedList<ObjectiveCard> sharedObjectiveCards;

	/**
	 * constructor for the Game class
	 * @param gameID unique ID (who calls the method will make sure the ID chosen is not being used already)
	 * @param numPlayers  maximum amount of players allowed in the Game
	 */
    public Game(int gameID, int numPlayers, Player host) {
		this.gameID = gameID;
        this.numPlayers = numPlayers;
		this.status = CREATION;
		this.players = new ArrayList<>(numPlayers);
		host.setGame(this);
		this.players.add(host);
	}


	/**
	 * method used to link a Player to this Game, when the last player joins the Game is STARTED
	 * @param player the Player who's trying to join this Game
	 * @throws MaxNumberOfPlayersException tells the Player there's no more room in this Game
	 */
	public void joinGame(Player player) throws MaxNumberOfPlayersException {
		if(this.getStatus() == CREATION){
			player.setGame(this);
			this.players.add(player);
			if (players.size() == numPlayers) {
				this.startGame();
			}
		}else
			throw new MaxNumberOfPlayersException("It's too late to join this game, try a different one!");
	}

	/**
	 * the Game stars:
	 * // scoreboard set up                                                            DONE
	 * // decks are generated and shuffled                                             DONE
	 * // 2 cards from resource and gold deck placed on the table                      DONE
	 * // each Player gets assigned a StarterCard                                      DONE
	 * // choosing which side to play their starting card
	 * // choosing a color
	 * // drawing 2 resource and 1 gold card                                           DONE
	 * // 2 shared objectives are drawn
	 * // choosing one of the 2 possible secret objectives
	 * // establishing who is the first Player to act                                  DONE
	 */
	private void startGame(){
		this.setStatus(START);
		this.scoreBoard = new ScoreBoard();
		this.goldDeck = new GoldDeck();
		this.resourceDeck = new ResourceDeck();
		this.starterDeck = new StarterDeck();
		this.objectiveDeck = new ObjectiveDeck();
		this.goldDeck.setUpGround();
		this.resourceDeck.setUpGround();
		this.players.forEach((p) -> {
            p.setStarterCard(this.starterDeck.getStarter());
			p.setGameField();
			p.setHand();
			getFirstHand(p);
        });
		this.sharedObjectiveCards.addAll(objectiveDeck.drawTwo());
		Collections.shuffle(players);
		startingPlayer = players.getFirst();
	}
	private void EndGame(){

	}

	//--------------------------------------------------------------------------------SETTERS
	/**
	 * setter of gameStatus attribute
	 * @param status of the Game
	 */
	private void setStatus(GameStatus status) {
		this.status = status;
	}

	/**
	 * sets up the first Hand for a Player p
	 * @param p the Player whose hand gets filled with 2 ResourceCards and 1 GoldCard
	 */
	private void getFirstHand(Player p){
		p.getHand().addCard(goldDeck.draw());
		p.getHand().addCard(resourceDeck.draw());
		p.getHand().addCard(resourceDeck.draw());
	}
	//--------------------------------------------------------------------------------GETTERS
	/**
	 * getter of gameStatus attribute
	 * @return status of the Game
	 */
	public GameStatus getStatus() {
		return status;
	}

	/**
	 * getter for gameID
	 * @return gameID
	 */
	public int getGameID() {
		return gameID;
	}
}
