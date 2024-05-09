package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

import java.util.*;

/**
 * the Game class, dedicated to all and each game related actions and information
 */
public class Game {
	/**
	 * array of Players taking part in this Game
	 */
	private ArrayList<Player> players;
	/**
	 * the ID of this Game
	 */
	private int gameID;
	/**
	 * the ScoreTrack linked to this Game, it keeps count of every Player's score
	 */
	private ScoreBoard scoreBoard;
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
	 * array of 16 Objective cards
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
	 * the Player currently playing their turn
	 */
	private Player currentPlayer;

	/**
	 * constructor for the Game class
	 * @param gameID unique ID (who calls the method will make sure the ID chosen is not being used already)
	 * @param numPlayers  maximum amount of players allowed in the Game
	 */
    public Game(int gameID, int numPlayers, Player host) {
		this.gameID = gameID;
		this.numPlayers = numPlayers;
		this.players = new ArrayList<>(numPlayers);
		host.setGame(this);
		this.players.add(host);
	}

	/**
	 * method used to link a Player to this Game
	 * @param player the Player who's trying to join this Game
	 * @throws NumOfPlayersException tells the Player there's no more room in this Game
	 */
	public void addPlayer(Player player) throws NumOfPlayersException {
		if(this.players.size() < numPlayers) {
			player.setGame(this);
			this.players.add(player);
		}else
			throw new NumOfPlayersException("It's too late to join this game, try a different one!");
	}

	/**
	 * initializes the scoreboard, all 4 decks (and shuffles them), the 2 gold and 2 resource Cards face-up on the table
	 * and gives a random StarterCards to each player
	 */
	public void gameStartConstructor(){
		this.scoreBoard = new ScoreBoard();
		this.goldDeck = new GoldDeck();
		this.resourceDeck = new ResourceDeck();
		this.starterDeck = new StarterDeck();
		this.objectiveDeck = new ObjectiveDeck();
		this.goldDeck.setUpGround();
		this.resourceDeck.setUpGround();
		players.forEach(p->p.setStarterCard(this.starterDeck.drawStarterCard()));
    }

	/**
	 * method used to set each Player's hand and their pair of ObjectiveCards, also
	 * calls drawSharedObjectiveCards() to set the 2 ObjectiveCards shared by all Players
	 */
	public void postColorSelectionSetUp(){
		for (Player p : this.getPlayers()) {
			p.setFirstHand();
			p.drawPairObjectives(this.getObjectiveDeck());
		}
		this.drawSharedObjectiveCards();
	}

	/**
	 * method used to draw the 2 ObjectiveCards shared by all Players in this Game
	 */
	public void drawSharedObjectiveCards(){
		this.sharedObjectiveCards = objectiveDeck.drawTwo();
	}

	/**
	 * WIP
	 * the timeout timer used when only one Player is connected, if no other Player reconnects before
	 * the end of the timer said Player wins the Game
	 */
	public void standby(){//tbd
		Timer timer = new Timer();
	}

	/**
	 * method that decides the winner(s) based on the Game rules
	 * @return a List of all winning Players (at least one)
	 */
	public List<Player> andTheWinnersAre() {
		for (Player p : this.players) {
			//sums the points scored during the game and points won through the ObjectiveCards
			p.countObjectivePoints(currentPlayer.getGameField().CheckObjectivePoints(currentPlayer.getObjectiveCard()),
					currentPlayer.getGameField().CheckObjectivePoints(getObjectiveCard(0)),
					currentPlayer.getGameField().CheckObjectivePoints(getObjectiveCard(1)));
			this.scoreBoard.addToPlayerScore(p.getColor(), p.getObjectivePoints());
		}
		//creates a List of Players tied for first place (by overall points)
		int max = this.scoreBoard.getPlayerScores().entrySet()
				.stream()
				.max((c1, c2) -> c1.getValue() > c2.getValue() ? 1 : -1)
				.get()
				.getValue();
		List<Player> ps1 = this.players.stream()
				.filter(p -> p.getColor().equals(this.scoreBoard.getPlayerScores()
						.entrySet()
						.stream()
						.filter(c -> c.getValue().equals(max))
						.map(Map.Entry::getKey)
						.toList().getFirst()))
				.toList();
		if (ps1.size() == 1)
			//there's a Player with more points than anyone else
			return ps1;
		int max2 = this.players.stream()
				.map(Player::getObjectivePoints)
				.max((pts1, pts2) -> pts1 > pts2 ? 1 : -1)
				.get();
		List<Player> ps2 = ps1.stream()
				.filter(p -> p.getObjectivePoints() == max2)
				.toList();
		return ps2;
	}

	//--------------------------------------------------------------------------------SETTERS

	/**
	 * setter for currentPlayer
	 * @param p the value at which it's set to
	 */
	public void setCurrentPlayer(Player p){
		this.currentPlayer = p;
	}

	//--------------------------------------------------------------------------------GETTERS

	/**
	 * getter for gameID attribute
	 * @return gameID
	 */
	public int getGameID() {
		return this.gameID;
	}

	/**
	 * getter for the ArrayList of Players in this Game
	 * @return the ArrayList of Players
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}

	/**
	 * getter for goldDeck attribute
	 * @return goldDeck
	 */
	public GoldDeck getGoldDeck() {
		return goldDeck;
	}

	/**
	 * getter for resourceDeck attribute
	 * @return resourceDeck
	 */
	public ResourceDeck getResourceDeck() {
		return resourceDeck;
	}

	/**
	 * getter for objectiveDeck attribute
	 * @return objectiveDeck
	 */
	public ObjectiveDeck getObjectiveDeck() {
		return objectiveDeck;
	}

	/**
	 * getter for one of the 2 ObjectiveCards shared by all Players
	 * @param i = 0 refers to the first ObjectiveCard, = 1 refers to the second ObjectiveCard
	 * @return the ObjectiveCard asked
	 */
	public ObjectiveCard getObjectiveCard(int i){
		return sharedObjectiveCards.get(i);
	}

	/**
	 * getter for scoreBoard attribute
	 * @return scoreBoard
	 */
	public ScoreBoard getScoreBoard(){
		return this.scoreBoard;
	}

	/**
	 * getter for currentPlayer attribute
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}

	/**
	 * getter for numPlayers attribute
	 * @return numPlayers
	 */
	public int getNumPlayers(){
		return numPlayers;
	}
	//--------------------------------------------------------------------------------FOR TESTING PURPOSES
	public void setGoldAndResourceDecks(){
		goldDeck = new GoldDeck();
		resourceDeck = new ResourceDeck();
	}
	public void setScoreBoard(){
		scoreBoard = new ScoreBoard();
	}
	public void setObjectiveDeck(){
		objectiveDeck = new ObjectiveDeck();
	}
	public StarterDeck getStarterDeck(){
		return starterDeck;
	}
	public LinkedList<ObjectiveCard> getSharedObjectiveCards(){
		return sharedObjectiveCards;
	}
}
