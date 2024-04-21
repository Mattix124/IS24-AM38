package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Exception.NotYourMainPhaseException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

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
	private Color color;
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
	private boolean isPlaying;
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
	}
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
	public void chooseStartingCardFace(boolean face){
		this.starterCard.setFace(face);
		this.gameField = new Field(this.starterCard);
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

	public boolean isYourTurn(){
		if(true)
			return true;
		else
			return false;
	}

	//---------------------------------------------------------------------------------------------------SETTERS
	/**
	 * setter for the Game and gameID attributes, used by the join command present in the Game class to link Player
	 * with the Game they are playing
	 * @param game
	 */
	public void setGame(Game game){
		this.game = game;
		this.isPlaying = true;
		this.gameID = game.getGameID();
	}

	/**
	 * setter for the attribute gameField
	 */
	public void setGameField(){
		this.gameField = new Field(starterCard);
	}

	/**
	 * setter for the attribute starterCard, taken as argument
	 * @param sCard the StarterCard that will be assigned to the Player's starterCard attribute
	 */
	public void setStarterCard(StarterCard sCard){
		this.starterCard = sCard;
	}


	/**
	 * sets up the first Hand for a Player p
	 * @param p the Player whose hand gets filled with 2 ResourceCards and 1 GoldCard
	 */
	public void getFirstHand(Player p){
		p.getHand().addCard(game.getGoldDeck().draw());
		p.getHand().addCard(game.getResourceDeck().draw());
		p.getHand().addCard(game.getResourceDeck().draw());
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
	 * @return
	 */
	public Color getColor(){
		return this.color;
	}

	public int getObjectivePoints() {
		return objectivePoints;
	}
}
