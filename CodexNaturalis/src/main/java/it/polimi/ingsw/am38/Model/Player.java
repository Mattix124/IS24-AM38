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
 * class dedicated to represent each user that creates a session and can join a Game
 */
public class Player {
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
	private final Hand hand;
	/**
	 * personal StarterCard of the Player
	 */
	private StarterCard starterCard;
	/**
	 * the Player's own field, where he can develop his chain of cards
	 */
	private Field gameField;
	/**
	 * personal ObjectiveCard of the Player, only they can score points with this ObjectiveCard
	 */
	private ObjectiveCard objectiveCard = null;
	/**
	 * pair of ObjectiveCards to choose from to set this Player's personal ObjectiveCard
	 */
	LinkedList<ObjectiveCard> pair;
	/**
	 * boolean used to keep track of when a Player is in a Game or not
	 */
	private boolean isPlaying = false;//how to set to false tbd
	/**
	 * Game in which the player is playing
	 */
	private Game game;
	/**
	 * points gained by completing the ObjectiveCards tasks
	 */
	private int objectivePoints = 0;
	/**
	 * boolean representing the possibility for this Player to be stuck and not able to play any PlayableCard on their field
	 */
	private boolean isStuck = false;
	private final boolean[] completedObjectives = new boolean[3];

	/**
	 * constructor method for Player
	 * @param nick a String chose ìn by each user to represent themselves during the current session
	 */
	public Player(String nick){
		this.nickName = nick;
		this.hand = new Hand();
        for (int i = 0; i < 3; i++)
			this.completedObjectives[i] = false;
    }

	/**
	 * getter method for the number of ObjectiveCards completed by this Player
	 * @return a int (from 0 to 3) containing the number of "true"s in the completedObjectives array
	 */
	public int getNumOfCompletedObjectives(){
		int n = 0;
		for(boolean b :completedObjectives)
			if(b)
				n++;
		return n;
	}

	/**
	 * method used to set the completion of a given ObjectiveCard to True
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
	 * method used to count how many points this Player won by completing any of the 3 ObjectiveCards he can score with
	 * (2 shared by very Player and 1 personal)
	 */
	public void countObjectivePoints(int a, int b, int c){
		objectivePoints = a + b + c;
	}

	/**
	 * method used to draw 2 ObjectiveCards from which this Player will have to choose 1 to keep
	 */
	public void drawPairObjectives(ObjectiveDeck objD){
		pair = objD.drawTwo();
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
			throw new InvalidInputException("Invalid input, choose between 1 and 2!");
	}

	/**
	 * lets the Player choose their StarterCard facing
	 * @param face true is face-Up, false is face-Down
	 */
	public void chooseStarterCardFace(boolean face){
		this.starterCard.setFace(face);
		createGameField();
	}

	/**
	 * method used by the Player to choose their Color (from the available ones)
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
	 * method used to play a PlayableCard on this Player gameField
	 * @param card the index of the PlayableCard in this Player Hand
	 * @param face the facing of the PlayableCard chosen
	 * @param coords the position in which the Player wants to play the PlayableCard
	 * @throws NoPossiblePlacement if the PlayableCard isn't playable with given parameters
	 * @throws InvalidInputException if the chosen card isn't from 1 to 3
	 */
	public void playACard(int card, Boolean face, Coords coords) throws InvalidInputException, NotPlaceableException, NoPossiblePlacement
	{
		this.hand.getCard(card).setFace(face);
		int pts = this.hand.getCard(card).play(this, coords);
		pointsScored(pts);
	}

	//----------------------------------------------------------------------------------PRIVATE METHODS

	/**
	 * method used to construct this Player's gameField attribute by placing their starterCard
	 */
	private void createGameField(){
		this.starterCard.play(this, null);
	}

	/**
	 * method that increases this Player's score by the value given as a parameter
	 * @param pts points added to this Player's score
	 */
	private void pointsScored(int pts){
		this.game.getScoreBoard().addToPlayerScore(this.getColor(), pts);
	}
	//-------------------------------------------------------------------------------------------SETTERS
	/**
	 * setter for the Game and gameID attributes, used by the join command present in the Game class to link Player
	 * with the Game they are playing
	 * @param game the Game this Player joins
	 */
	public void setGame(Game game){
		this.game = game;
		this.setIsPlaying(true);
	}
	
	public void setIsPlaying(boolean b){
		this.isPlaying = b;
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
	 * setter method for isStuck attribute
	 * @param b the value to set the attribute to
	 */
	public void setStuck(boolean b){
		this.isStuck = b;
	}
	//------------------------------------------------------------------------------------------GETTERS
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
	public boolean isPlaying() {
		return this.isPlaying;
	}

	/**
	 * getter for the Hand of this Player
	 * @return
	 */
	public Hand getHand() {
		return this.hand;
	}

	public Symbol[] getHandCardsColors(){
		Symbol[] sc = new Symbol[3];
		for(int i = 0; i < sc.length; i++)
			sc[i] = this.hand.getCard(i).getKingdom();
		return sc;
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
	 * @return game attribute
	 */
	public Game getGame(){
		return this.game;
	}

	/**
	 * getter for this objectiveCard of this Player
	 * @return objectiveCard attribute
	 */
	public ObjectiveCard getObjectiveCard(){
		return this.objectiveCard;
	}

	/**
	 * getter method for isStuck attribute
	 * @return isStuck attribute
	 */
	public boolean isStuck() {
		return isStuck;
	}

	public Field getField()
	{
		return gameField;
	}
	//--------------------------------------------------------------------------------FOR TESTING PURPOSES
	public void setObjectivePoints(int i){
		this.objectivePoints = i;
	}
	public void setObjectiveCard(ObjectiveCard oc){this.objectiveCard = oc;}
	public LinkedList<ObjectiveCard> getPair(){return pair;}
	public StarterCard getStarterCard(){return starterCard;}

}