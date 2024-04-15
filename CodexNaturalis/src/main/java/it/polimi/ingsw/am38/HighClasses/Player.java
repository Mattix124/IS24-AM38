package it.polimi.ingsw.am38.HighClasses;

import it.polimi.ingsw.am38.Cards.*;
import it.polimi.ingsw.am38.DataStructure.DataStructure;
import it.polimi.ingsw.am38.Enum.*;

public class Player //Pretty obvious class.
{
	private final String nickName;
	private final Color color; //it contains the enum of the color assigned randomly to the player.

	private int score; //variable that contain the score achieved by the player during all game.
	private PlayableCard[] hand; //it contains the 3 cards ready to be played.

	private DataStructure gameField;


 //the next list of attributes displays the symbols visible on the player's gamefield.
	private int visibleFungi;
	private int visibleInsect;
	private int visiblePlant;
	private int visibleAnimal;
	private int visibleManuscript;
	private int visibleQuill;
	private int visibleInkwell;

	public Player(Color color, String nick) //constructor for the player
	{
		this.color = color;
		this.nickName = nick;
	}

	//getters methods for visible symbols
	public int getVisibleInsect()
	{
		return visibleInsect;
	}

	public int getVisiblePlant()
	{
		return visiblePlant;
	}

	public int getVisibleAnimal()
	{
		return visibleAnimal;
	}

	public int getVisibleManuscript()
	{
		return visibleManuscript;
	}

	public int getVisibleQuill()
	{
		return visibleQuill;
	}

	public int getVisibleInkwell()
	{
		return visibleInkwell;
	}

	private void calculateVisibleSymbols()
	{

	}

}
