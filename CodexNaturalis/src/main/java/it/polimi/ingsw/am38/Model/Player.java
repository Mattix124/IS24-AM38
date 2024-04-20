package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

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
	/**
	 * constructor method for Player
	 * @param nick a String chose ìn by each user to represent themselves during the current session
	 */
	public Player(String nick){
		this.nickName = nick;
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

	//---------------------------------------------------------------------------------------------------SETTERS
	/**
	 * setter for the Color the Player will be choosing once he joins a Game
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

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
	 * setter for the Player's Hand
	 */
	public void setHand() {
		this.hand = new Hand();
	}

	//---------------------------------------------------------------------------------------------GETTERS
	/**
	 * getter for the Player's nickName
	 * @return nickName
	 */
	public String getNickname() {
		return nickName;
	}

	/**
	 * getter for the boolean referring to the state of the Player (in-Game = true, not in-Game = false)
	 * @return
	 */
	public boolean getIsPlaying() {
		return this.isPlaying;
	}

	public Hand getHand() {
		return hand;
	}
}
