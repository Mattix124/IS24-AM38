package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Enum.Color.*;

/**
 * Class dedicated to represent each user that creates a session and can join a Game
 */
public class Player {
	/**
	 * Unique name that the Player chooses
	 */
	private final String nickName;
	/**
	 * Color chosen by the Player as soon as he joins a Game
	 */
	private Color color = NONE;
	/**
	 * The group of cards that the Players has (never more than 3)
	 */
	private final Hand hand;
	/**
	 * Personal StarterCard of the Player
	 */
	private StarterCard starterCard;
	/**
	 * The Player's own field, where he can develop his chain of cards
	 */
	private Field gameField;
	/**
	 * Personal ObjectiveCard of the Player, only they can score points with this ObjectiveCard
	 */
	private ObjectiveCard objectiveCard = null;
	/**
	 * Pair of ObjectiveCards to choose from to set this Player's personal ObjectiveCard
	 */
	private LinkedList<ObjectiveCard> pair;
	/**
	 * Boolean used to keep track of when a Player is in a Game or not
	 */
	private boolean hasPlayed = false;
	/**
	 * Boolean used to identify when a Player is online
	 */
	private boolean isAlive;
	/**
	 * Game in which the player is playing
	 */
	private Game game;
	/**
	 * Points gained by completing the ObjectiveCards tasks
	 */
	private int objectivePoints = 0;
	/**
	 * Boolean representing the possibility for this Player to be stuck and not able to play any PlayableCard on their field
	 */
	private boolean isStuck = false;

	/**
	 * Int for the id of a card drawn by a player disconnected
	 */
	private int hangingDrawId;

	private final boolean[] completedObjectives = new boolean[3];

	/**
	 * Constructor method for Player
	 *
	 * @param nick a String chose ìn by each user to represent themselves during the current session
	 */
	public Player(String nick){
		this.nickName = nick;
		this.hand = new Hand();
        for (int i = 0; i < 3; i++)
			this.completedObjectives[i] = false;
    }

	/**
	 * Getter method for the number of ObjectiveCards completed by this Player
	 *
	 * @return an int (from 0 to 3) containing the number of "true"s in the completedObjectives array
	 */
	public int getNumOfCompletedObjectives(){
		int n = 0;
		for(boolean b :completedObjectives)
			if(b)
				n++;
		return n;
	}

	/**
	 * Method used to set the completion of a given ObjectiveCard to True
	 *
	 * @param obj a String representing the ObjectiveCard completed by this Player (1 is the firstShared,
	 *               2 is the secondShared, p is the private/personal one)
	 */
	public void setObjectiveAsCompleted(String obj){
		switch (obj){
			case "1" -> completedObjectives[0] = true;
			case "2" -> completedObjectives[1] = true;
			case "p" -> completedObjectives[2] = true;
		}
	}

	/**
	 * Method used to count how many points this Player won by completing any of the 3 ObjectiveCards he can score with
	 * (2 shared by very Player and 1 personal)
	 */
	public void countObjectivePoints(int a, int b, int c){
		objectivePoints = a + b + c;
	}

	/**
	 * Method used to draw 2 ObjectiveCards from which this Player will have to choose 1 to keep
	 */
	public void drawPairObjectives(ObjectiveDeck objD){
		pair = objD.drawTwo();
	}

	/**
	 * Method that allows the Player to choose from the pair of ObjectiveCards which one he prefers, by using the
	 * choice parameter, throws an Exception if the Player gives an invalid input
	 * @param choice represents the index of the ObjectiveCard chosen by the Player (-1)
	 * @throws InvalidInputException lets the Player know when he gave an invalid input
	 */
	public void chooseObjectiveCard(int choice)throws InvalidInputException {
		if(choice == 1 || choice == 2)
			this.objectiveCard = this.pair.get(choice-1);
		else
			throw new InvalidInputException("Invalid input, choose between 1 and 2!");
	}

	/**
	 * Lets the Player choose their StarterCard facing
	 * @param face true is face-Up, false is face-Down
	 */
	public void chooseStarterCardFace(boolean face){
		this.starterCard.setFace(face);
		createGameField();
	}

	/**
	 * Method used by the Player to choose their Color (from the available ones)
	 * @param c the Color chosen by this Player
	 * @throws ColorTakenException if the Color hasn't been chosen already by someone else
	 */
	public void chooseColor(Color c)  throws ColorTakenException {
		if(game.getPlayers().stream()
				.map(Player::getColor)
				.noneMatch(x -> x.equals(c))){
			this.color = c;
		}else
			throw new ColorTakenException("This color is already taken, try a different one!");
	}

	/**
	 * Method used to play a PlayableCard on this Player gameField
	 * @param card the index of the PlayableCard in this Player Hand
	 * @param face the facing of the PlayableCard chosen
	 * @param coords the position in which the Player wants to play the PlayableCard
	 * @throws NoPossiblePlacement if the PlayableCard isn't playable with given parameters
	 * @throws InvalidInputException if the chosen card isn't from 1 to 3
	 */
	public void playACard(int card, boolean face, Coords coords) throws NotPlaceableException, NoPossiblePlacement
	{
		this.hand.getCard(card).setFace(face);
		int pts = this.hand.getCard(card).play(this, coords);
		pointsScored(pts);
	}

	//----------------------------------------------------------------------------------PRIVATE METHODS

	/**
	 * Method used to construct this Player's gameField attribute by placing their starterCard
	 */
	private void createGameField(){
		this.starterCard.play(this, null);
	}

	/**
	 * Method that increases this Player's score by the value given as a parameter
	 * @param pts points added to this Player's score
	 */
	private void pointsScored(int pts){
		this.game.getScoreBoard().addToPlayerScore(this.getColor(), pts);
	}
	//-------------------------------------------------------------------------------------------SETTERS
	/**
	 * Setter for the Game and gameID attributes, used by the join command present in the Game class to link Player
	 * with the Game they are playing
	 * @param game the Game this Player joins
	 */
	public void setGame(Game game){
		this.game = game;
	}

	/**
	 * Setter for the attribute isPlaying
	 *
	 * @param b
	 */
	public void setHasPlayed(boolean b){
		this.hasPlayed = b;
	}

	/**
	 * Setter for the attribute gameField
	 */
	public void setGameField(Field gameField){
		this.gameField = gameField;
	}

	/**
	 * Setter for the attribute starterCard, taken as argument
	 * @param sCard the StarterCard that will be assigned to the Player's starterCard attribute
	 */
	public void setStarterCard(StarterCard sCard){
		this.starterCard = sCard;
	}

	/**
	 * Sets up the first Hand for this Player
	 */
	public void setFirstHand(){
		try{
			this.game.getGoldDeck().draw(this, 0);
			this.game.getResourceDeck().draw(this, 0);
			this.game.getResourceDeck().draw(this, 0);
		} catch (EmptyDeckException e){
			throw new RuntimeException("this should never happen, because when the players draw their first hand the decks can't be empty", e);
		}
	}

	/**
	 * Setter method for isStuck attribute
	 * @param b the value to set the attribute to
	 */
	public void setStuck(boolean b){
		this.isStuck = b;
	}

	/**
	 * Setter for the id of the card drawn by the client disconnected
	 *
	 * @param id
	 */
	public void setHangingDrawId(int id)
	{
		hangingDrawId = id;
	}
	//------------------------------------------------------------------------------------------GETTERS
	/**
	 * Getter for the Player's nickName
	 * @return nickName
	 */
	public String getNickname() {
		return this.nickName;
	}

	/**
	 * Getter for the boolean referring to the state of the Player (in-Game = true, not in-Game = false)
	 * @return
	 */
	public boolean getHasPlayed() {
		return this.hasPlayed;
	}

	/**
	 * Getter for the Hand of this Player
	 *
	 * @return the hand of the player
	 */
	public Hand getHand() {
		return this.hand;
	}

	/**
	 * Getter for the kingdoms of the card in the hand
	 *
	 * @return an array that contains string to identify the kingdom of the cards
	 */
	public String[] getHandCardsColors(){
		String[] sc = new String[3];
		for(int i = 0; i < this.hand.getCardsInHand().size(); i++)
			sc[i] = toStringCode(this.hand.getCard(i).getKingdom(), this.hand.getCard(i).getCardID());
		return sc;
	}

	/**
	 * Method to convert the kingdom of a card into a string
	 *
	 * @param sy kingdom of the card
	 * @param id of the card
	 * @return the kingdom converted
	 */
	private String toStringCode(Symbol sy, int id){
		if(id < 41){
			return switch (sy) {
				case ANIMAL -> "RA";
				case FUNGI -> "RF";
				case PLANT -> "RP";
				case INSECT -> "RI";
				default -> null;
			};
		}
		return switch (sy){
			case ANIMAL -> "GA";
			case FUNGI -> "GF";
			case PLANT -> "GP";
			case INSECT -> "GI";
			default -> null;
		};
	}

	/**
	 * Getter for the Color of this Player
	 *
	 * @return the Color chosen by this Player
	 */
	public Color getColor(){
		return this.color;
	}

	/**
	 * Getter for the ObjectiveCard points scored by this Player
	 *
	 * @return this Player objectiveCardPoints
	 */
	public int getObjectivePoints() {
		return objectivePoints;
	}

	/**
	 * Getter for this Player's gameField
	 *
	 * @return this Player's gameField
	 */
	public Field getGameField(){
		return this.gameField;
	}

	/**
	 * Getter for this Player's Game
	 *
	 * @return game attribute
	 */
	public Game getGame(){
		return this.game;
	}

	/**
	 * Getter for this objectiveCard of this Player
	 *
	 * @return objectiveCard attribute
	 */
	public ObjectiveCard getObjectiveCard(){
		return this.objectiveCard;
	}

	/**
	 * Getter method for isStuck attribute
	 *
	 * @return isStuck attribute
	 */
	public boolean isStuck() {
		return isStuck;
	}

	/**
	 * Getter for the attribute hangingDrawId
	 *
	 * @return hangingDrawId
	 */
	public int getHangingDrawId()
	{
		return hangingDrawId;
	}

	/**
	 * Getter for the attribute starterCard
	 *
	 * @return starterCard
	 */
	public StarterCard getStarterCard(){return starterCard;}

	/**
	 * Getter for the attribute pair
	 *
	 * @return pair
	 */
	public LinkedList<ObjectiveCard> getPair(){return pair;}

	/**
	 * setter method for
	 *
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		isAlive = alive;
	}

	public boolean isAlive() {
		return isAlive;
	}

}