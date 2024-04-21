package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.GameStatus;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import it.polimi.ingsw.am38.Model.Miscellaneous.ScoreBoard;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static it.polimi.ingsw.am38.Enum.GameStatus.*;

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
	 * @throws NumOfPlayersException tells the Player there's no more room in this Game
	 */
	public void joinGame(Player player) throws NumOfPlayersException {
		if(this.getStatus() == CREATION){
			player.setGame(this);
			this.players.add(player);
			if (players.size() == numPlayers) {
				this.gameStartConstructor();
			}
		}else
			throw new NumOfPlayersException("It's too late to join this game, try a different one!");
	}

	private void gameStartConstructor(){
		this.setStatus(START);
		this.scoreBoard = new ScoreBoard();
		this.goldDeck = new GoldDeck();
		this.resourceDeck = new ResourceDeck();
		this.starterDeck = new StarterDeck();
		this.objectiveDeck = new ObjectiveDeck();
		this.goldDeck.setUpGround();
		this.resourceDeck.setUpGround();
        for (Player p : this.players) {
            p.setStarterCard(this.starterDeck.getStarter());
        }
    }
	public void standby(){
		Timer timer = new Timer();
	}
	private void EndGame(){
		this.setStatus(ENDGAME);
        this.andTheWinnerIs();
    }
	public void andTheWinnerIs(){
		for (Player p : this.players) {
			p.countObjectivePoints();
		}
		//int max = this.scoreBoard.getScore(this.players.removeFirst().getColor());
		//if(max < this.scoreBoard.getScore(this.players.removeFirst().getColor()))
		//	max = this.scoreBoard.getScore(this.players.removeFirst().getColor());
		//if(max < this.scoreBoard.getScore(this.players.removeFirst().getColor()))
		//	max = this.scoreBoard.getScore(this.players.removeFirst().getColor());
		//if(max < this.scoreBoard.getScore(this.players.removeFirst().getColor()))
		//	max = this.scoreBoard.getScore(this.players.removeFirst().getColor());

	}

	//--------------------------------------------------------------------------------SETTERS

	/**
	 * setter for the 3 shared ObjectiveCards
	 */
	private void setSharedObjectiveCards(){
		this.sharedObjectiveCards.addAll(objectiveDeck.drawTwo());
	}

	/**
	 * determines the first player to act and the order of all players
	 */
	private void setStartingPlayer(){
		Collections.shuffle(players);
		startingPlayer = players.getFirst();
	}

	/**
	 * setter of gameStatus attribute
	 * @param status of the Game
	 */
	private void setStatus(GameStatus status) {
		this.status = status;
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
	public ArrayList<Player> getPlayers(){
		return players;
	}

	public GoldDeck getGoldDeck() {
		return goldDeck;
	}

	public ResourceDeck getResourceDeck() {
		return resourceDeck;
	}

	public ObjectiveDeck getObjectiveDeck() {
		return objectiveDeck;
	}

	public StarterDeck getStarterDeck() {
		return starterDeck;
	}
	public ObjectiveCard getObjectiveCard(int i){
		return sharedObjectiveCards.get(i-1);
	}
}
