package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

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
	 * the group of cards that the Players has (from 2 to 3)
	 */
	private PlayableCard[] hand;
	/**
	 * the Player's own field, where he can develop his chain of cards
	 */
	private Field gameField;
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
	 * setter for the Color the Player will be choosing once he joins a Game
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

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
}
