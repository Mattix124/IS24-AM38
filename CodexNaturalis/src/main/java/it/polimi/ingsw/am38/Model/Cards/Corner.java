package it.polimi.ingsw.am38.Model.Cards;


import it.polimi.ingsw.am38.Enum.Symbol;

public class Corner
{
	private boolean occupied;
	private final Symbol symbol;

	public Corner(Symbol i)
	{  //create the corner
		occupied = false;
		symbol = i;
	}

	public boolean isOccupied(){

		return occupied;
	}

	public Symbol getSymbol(){

		return symbol;
	}

	public void setOccupied(boolean occupied) {

		this.occupied = occupied;
	}
}
