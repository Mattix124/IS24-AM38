package it.polimi.ingsw.am38.Model.Board;

import java.util.LinkedList;

public class EnteredCardControl
{
	private int enteredCard;
	private int checkedAngle;

	public EnteredCardControl(int enteredCard, int checkedAngle)
	{
		this.enteredCard = enteredCard;
		this.checkedAngle = checkedAngle;
	}

	public int getEnteredCard()
	{
		return enteredCard;
	}

	public void increaseEnteredCard()
	{
		enteredCard++;
	}

	public int getCheckedAngle()
	{
		return checkedAngle;
	}

	public void increaseCheckedAngle()
	{
		checkedAngle++;
	}
}
