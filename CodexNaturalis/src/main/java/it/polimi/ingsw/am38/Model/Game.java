package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

import java.util.*;

/**
 * The Game class, dedicated to all and each game related actions and information
 */
public class Game {
	public boolean endGame;
	/**
	 * Array of Players taking part in this Game
	 */
	private final ArrayList<Player> players;
	/**
	 * The ID of this Game
	 */
	private final int gameID;
	/**
	 * The ScoreTrack linked to this Game, it keeps count of every Player's score
	 */
	private ScoreBoard scoreBoard;
	/**
	 * Number of players allowed in this Game (chosen by the Player creating the Game)
	 */
	private final int numPlayers;
	/**
	 * Array of 40 gold cards
	 */
	private GoldDeck goldDeck;
	/**
	 * Array of 40 resource cards
	 */
	private ResourceDeck resourceDeck;
	/**
	 * Array of 16 Objective cards
	 */
	private ObjectiveDeck objectiveDeck;
	/**
	 * Array of 6 starting cards
	 */
	private StarterDeck starterDeck;
	/**
	 * List of the 2 shared ObjectiveCard, every Player can score points with them
	 */
	private LinkedList<ObjectiveCard> sharedObjectiveCards;
	/**
	 * The Player currently playing their turn
	 */
	private Player currentPlayer;

	/**
	 * Constructor for the Game class
	 *
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
	 * Method used to link a Player to this Game
	 *
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
	 * Initializes the scoreboard, all 4 decks (and shuffles them), the 2 gold and 2 resource Cards face-up on the table
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
	 * Method used to set each Player's hand and their pair of ObjectiveCards, also
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
	 * Method used to draw the 2 ObjectiveCards shared by all Players in this Game
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
	 * Method that decides the winner(s) based on the Game rules
	 * @return a List of all winning Players (at least one)
	 */
	public List<Player> andTheWinnersAre() {
		for (Player p : this.players) {
			//sums the points scored during the game and points won through the ObjectiveCards
			p.countObjectivePoints(p.getGameField().CheckObjectivePoints(p.getObjectiveCard()),
					p.getGameField().CheckObjectivePoints(getObjectiveCard(0)),
					p.getGameField().CheckObjectivePoints(getObjectiveCard(1)));
			this.scoreBoard.addToPlayerScore(p.getColor(), p.getObjectivePoints());
		}
		//creates a List of Players tied for first place (by overall points)
		int max = this.scoreBoard.getPlayerScores().entrySet()
				.stream()
				.max((c1, c2) -> c1.getValue() > c2.getValue() ? 1 : -1)
				.get()
				.getValue();
		List<Player> ps1 = this.players.stream()
				.filter(p -> this.scoreBoard.getPlayerScores()
						.entrySet()
						.stream()
						.filter(c -> c.getValue().equals(max))
						.map(Map.Entry::getKey)
						.toList().contains(p.getColor()))
				.toList();
		if (ps1.size() != 1) {//there's a group of Players tied for points scored
			setPlayersObjectiveCompletion(ps1);
			int highestNumOfObjectivesCompleted = ps1.stream()
					.map(Player::getNumOfCompletedObjectives)
					.max((n1,n2)-> n1 >= n2 ? 1 : -1)
					.get();
			ps1 = ps1.stream()
					.filter(p -> p.getNumOfCompletedObjectives() == highestNumOfObjectivesCompleted)
					.toList();
		}
		return ps1;
		/*
		old version: when a tie happened the Player with the most ObjectiveCard points would have won
		int max2 = this.players.stream()
				.map(Player::getObjectivePoints)
				.max((pts1, pts2) -> pts1 >= pts2 ? 1 : -1)
				.get();
		List<Player> ps2 = ps1.stream()
				.filter(p -> p.getObjectivePoints() == max2)
				.toList();
		return ps2;
		*/
	}

	//--------------------------------------------------------------------------------SETTERS

	/**
	 * Setter for currentPlayer
	 * @param p the value at which it's set to
	 */
	public void setCurrentPlayer(Player p){
		this.currentPlayer = p;
	}
	public void setEndGame(boolean b){
		this.endGame = b;
	}

	/**
	 * Method used when there's a group of Players tied for points scored, for each of them it counts and saves
	 * how many ObjectiveCards they completed
	 * @param players a List of Players containing all the Players tied for points scored
	 */
	private void setPlayersObjectiveCompletion(List<Player> players){
		players.forEach(p -> {
			if(p.getGameField().CheckObjectivePoints(getSharedObjectiveCards().getFirst()) != 0)
				p.setObjectiveAsCompleted("1");
			if(p.getGameField().CheckObjectivePoints(getSharedObjectiveCards().getLast()) != 0)
				p.setObjectiveAsCompleted("2");
			if(p.getGameField().CheckObjectivePoints(p.getObjectiveCard()) != 0)
				p.setObjectiveAsCompleted("p");
		});
	}

	//--------------------------------------------------------------------------------GETTERS

	/**
	 * Getter for gameID attribute
	 *
	 * @return gameID
	 */
	public int getGameID() {
		return this.gameID;
	}

	/**
	 * Getter for the ArrayList of Players in this Game
	 *
	 * @return the ArrayList of Players
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}

	/**
	 * Getter for goldDeck attribute
	 *
	 * @return goldDeck
	 */
	public GoldDeck getGoldDeck() {
		return this.goldDeck;
	}

	/**
	 * Getter for resourceDeck attribute
	 *
	 * @return resourceDeck
	 */
	public ResourceDeck getResourceDeck() {
		return resourceDeck;
	}

	/**
	 * Getter for objectiveDeck attribute
	 *
	 * @return objectiveDeck
	 */
	public ObjectiveDeck getObjectiveDeck() {
		return objectiveDeck;
	}

	/**
	 * Getter for one of the 2 ObjectiveCards shared by all Players
	 *
	 * @param i = 0 refers to the first ObjectiveCard, = 1 refers to the second ObjectiveCard
	 * @return the ObjectiveCard asked
	 */
	public ObjectiveCard getObjectiveCard(int i){
		return sharedObjectiveCards.get(i);
	}

	/**
	 * Getter for scoreBoard attribute
	 *
	 * @return scoreBoard
	 */
	public ScoreBoard getScoreBoard(){
		return this.scoreBoard;
	}

	/**
	 * Getter for currentPlayer attribute
	 *
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}

	/**
	 * Getter for numPlayers attribute
	 *
	 * @return numPlayers
	 */
	public int getNumPlayers(){
		return numPlayers;
	}

	/**
	 * Getter for the IDs of the starter cards of the players
	 *
	 * @return a hash map with the IDs of the starter cards of the players and their nicknames as a key
	 */
	public HashMap<String, Integer> getNicksAndStartersIDs(){
		HashMap<String, Integer> sc = new HashMap<>(numPlayers);
		players.forEach(p -> sc.put(p.getNickname(), p.getStarterCard().getCardID()));
		return sc;
	}

	/**
	 * Getter for the kingdoms of the cards in the hand of every player
	 *
	 * @return a hash map with the kingdoms of the hand of the players and their nicknames as a key
	 */
	public HashMap<String, String[]> getPlayersCardsColors(){
		HashMap<String, String[]> cc = new HashMap<>(numPlayers);
		players.forEach(p -> cc.put(p.getNickname(), p.getHandCardsColors()));
		return cc;
	}

	/**
	 * Getter for face of the starter card of the players
	 *
	 * @return a hash map with the face chosen for the starter card
	 *         of every player and their nicknames as a key
	 */
	public HashMap<String, Boolean> getPlayersStarterFacing(){
		HashMap<String, Boolean> sf = new HashMap<>(numPlayers);
		players.forEach(p -> sf.put(p.getNickname(), p.getStarterCard().getFace()));
		return sf;
	}

	/**
	 * Getter for the players colors
	 *
	 * @return a hash map with the colors of the players and their nicknames as a key
	 */
	public HashMap<String, Color> getPlayersColors(){
		HashMap<String, Color> colors = new HashMap<>();
		for(Player p : players)
			colors.put(p.getNickname(), p.getColor());
		return colors;
	}



	//--------------------------------------------------------------------------------FOR TESTING PURPOSES

	/**
	 * Setter for the attribute setGoldAndResourceDecks
	 */
	public void setGoldAndResourceDecks(){
		goldDeck = new GoldDeck();
		resourceDeck = new ResourceDeck();
	}

	/**
	 * Setter for the attribute scoreBoard
	 */
	public void setScoreBoard(){
		scoreBoard = new ScoreBoard();
	}

	/**
	 * Setter for the attribute objectiveDeck
	 */
	public void setObjectiveDeck(){
		objectiveDeck = new ObjectiveDeck();
	}

	/**
	 * Getter for the attribute starterDeck
	 *
	 * @return starterDeck
	 */
	public StarterDeck getStarterDeck(){
		return starterDeck;
	}

	/**
	 * Getter for the attribute sharedObjectiveCards
	 *
	 * @return sharedObjectiveCards
	 */
	public LinkedList<ObjectiveCard> getSharedObjectiveCards(){
		return sharedObjectiveCards;
	}
}
