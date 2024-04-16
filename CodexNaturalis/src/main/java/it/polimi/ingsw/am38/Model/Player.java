package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

public class Player //Pretty obvious class.
{
	private final String nickName;
	private Color color; //it contains the enum of the color assigned randomly to the player.

	private int score; //variable that contain the score achieved by the player during all game.
	private PlayableCard[] hand; //it contains the 3 cards ready to be played.

	private Field gameField;
	private boolean isPlaying;
	private Game game;
	private int gameID;

 //the next list of attributes displays the symbols visible on the player's gamefield.

	public Player(String nick){
		this.nickName = nick;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getNickname() {
		return nickName;
	}

	public boolean getIsPlaying() {
		return this.isPlaying;
	}
}
