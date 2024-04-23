package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.*;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Enum.Color.*;

/**
 * class dedicated to represent each user that creates a session and can join a Game
 */
public class Player{
	/**
	 * unique name that the Player chooses
	 */
	private final String nickName;
	/**
	 * color chosen by the Player as soon as he joins a Game
	 */
	private Color color = NONE;
	/**
	 * the group of cards that the Players has (never more than 3)
	 */
	private Hand hand;
	/**
	 * personal StarterCard of the Player
	 */
	private StarterCard starterCard;
	/**
	 * the Player's own field, where he can develop his chain of cards
	 */
	private Field gameField;
	private LinkedList<ObjectiveCard> pair;
	/**
	 * personal ObjectiveCard of the Player, only they can score points with this ObjectiveCard
	 */
	private ObjectiveCard objectiveCard;
	/**
	 * boolean used to keep track of when a Player is in a Game or not
	 */
	private boolean isPlaying = false;
	/**
	 * Game in which the player is playing
	 */
	private Game game;
	/**
	 * gameID of the Game in which the Player is playing
	 */
	private int gameID;
	private int objectivePoints;
	/**
	 * constructor method for Player
	 * @param nick a String chose ìn by each user to represent themselves during the current session
	 */
	public Player(String nick){
		this.nickName = nick;
		this.hand = new Hand();
	}

	/**
	 * method used to count how many points this Player won by completing any of the 3 ObjectiveCards he can score with
	 * (2 shared by very Player and 1 personal)
	 */
	public void countObjectivePoints(){
		objectivePoints = this.gameField.CheckObjectivePoints(this.objectiveCard)
				+ this.gameField.CheckObjectivePoints(this.game.getObjectiveCard(1))
				+ this.gameField.CheckObjectivePoints(this.game.getObjectiveCard(2));
	}

	/**
	 * method that allows the Player to choose from the pair of ObjectiveCards which one he prefers, by using the
	 * choice parameter, throws an Exception if the Player gives an invalid input
	 * @param choice represents the index of the ObjectiveCard chosen by the Player (-1)
	 * @throws InvalidInputException lets the Player know when he gave an invalid input
	 */
	public void chooseObjectiveCard(int choice)throws InvalidInputException {
		if(choice == 1 || choice == 2)
			this.objectiveCard = this.pair.get(choice-1);
		else
			throw new InvalidInputException("Invalid input, choose between 1 and 2");
	}

	/**
	 * lets the Player choose their StarterCard facing
	 * @param face true is face-Up, false is face-Down
	 */
	public void chooseStartingCardFace(boolean face) throws NotPlaceableException {
		this.starterCard.setFace(face);
		createGameField();
	}

	/**
	 * lets the Player choose their Color
	 * @param c the Color chosen
	 */
	public void chooseColor(Color c)  throws ColorTakenException {
		if(!(c.equals(game.getPlayers().stream()
                        .map(p -> p.getColor())))){
			this.color = c;
		}else
			throw new ColorTakenException("This color is already taken, try a different one");
	}

	/**
	 * method used to play a PlayableCard on this Player gameField
	 * @param card the index of the PlayableCard in this Player Hand
	 * @param face the facing of the PlayableCard chosen
	 * @param coords the position in which the Player wants to play the PlayableCard
	 * @throws NotAFacingException if the input String isn't a valid option ('face up' or 'face down')
	 * @throws NotPlaceableException if the PlayableCard isn't playable with given parameters
	 */
	public void playACard(int card, String face, Coords coords) throws NotAFacingException, NotPlaceableException {
		boolean b;
		if(face == "face up")
			b = true;
		else if(face == "face down")
			b = false;
		else
			throw new NotAFacingException("You have to choose to play the card 'face up' or 'face down'");
		this.hand.getCard(card).setFace(b);
		int pts = this.hand.getCard(card).play(this, coords);
		pointsScored(pts);
	}
	public void createGameField() throws NotPlaceableException {
		this.starterCard.play(this, null);
	}
	public void pointsScored(int pts){
		this.game.getScoreBoard().addToPlayerScore(this.getColor(), pts);
	}
	//---------------------------------------------------------------------------------------------------SETTERS
	/**
	 * setter for the Game and gameID attributes, used by the join command present in the Game class to link Player
	 * with the Game they are playing
	 * @param game the Game this Player joins
	 */
	public void setGame(Game game){
		this.game = game;
		this.isPlaying = true;
		this.gameID = game.getGameID();
	}

	/**
	 * setter for the attribute gameField
	 */
	public void setGameField(Field gameField){
		this.gameField = gameField;
	}

	/**
	 * setter for the attribute starterCard, taken as argument
	 * @param sCard the StarterCard that will be assigned to the Player's starterCard attribute
	 */
	public void setStarterCard(StarterCard sCard){
		this.starterCard = sCard;
	}

	/**
	 * sets up the first Hand for this Player
	 */
	public void setFirstHand() throws EmptyDeckException {
		this.game.getGoldDeck().draw(this);
		this.game.getResourceDeck().draw(this);
		this.game.getResourceDeck().draw(this);
	}

	//---------------------------------------------------------------------------------------------GETTERS
	/**
	 * getter for the Player's nickName
	 * @return nickName
	 */
	public String getNickname() {
		return this.nickName;
	}

	/**
	 * getter for the boolean referring to the state of the Player (in-Game = true, not in-Game = false)
	 * @return
	 */
	public boolean getIsPlaying() {
		return this.isPlaying;
	}

	/**
	 * getter for the Hand of this Player
	 * @return
	 */
	public Hand getHand() {
		return this.hand;
	}

	/**
	 * getter for the Color of this Player
	 * @return the Color chosen by this Player
	 */
	public Color getColor(){
		return this.color;
	}

	/**
	 * getter for the ObjectiveCard points scored by this Player
	 * @return this Player objectiveCardPoints
	 */
	public int getObjectivePoints() {
		return objectivePoints;
	}

	/**
	 * getter for this Player's gameField
	 * @return this Player's gameField
	 */
	public Field getGameField(){
		return this.gameField;
	}

	/**
	 * getter for this Player's Game
	 * @return
	 */
	public Game getGame(){
		return this.game;
	}

	public boolean getIsYourTurn(){
		return (this.game.getCurrentPlayer() == this);
	}
}