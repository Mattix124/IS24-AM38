package it.polimi.ingsw.am38.Model.HighClasses;

import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.Enum.*;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

public class Player //Pretty obvious class.
{
	private final String nickName;
	private final Color color; //it contains the enum of the color assigned randomly to the player.

	private int score; //variable that contain the score achieved by the player during all game.
	private PlayableCard[] hand; //it contains the 3 cards ready to be played.

	private Field gameField;


 //the next list of attributes displays the symbols visible on the player's gamefield.

	public Player(Color color, String nick) //constructor for the player
	{
		this.color = color;
		this.nickName = nick;
	}


}
