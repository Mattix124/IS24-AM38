package it.polimi.ingsw.am38.Model.Cards;


import it.polimi.ingsw.am38.Enum.Symbol;

public class Corner
{
	private boolean occupied;
	private Symbol symbol;

	public Corner(String symbol)
	{  //create the corner
		occupied = false;
		switch(symbol){
			case "fungi" : this.symbol = Symbol.FUNGI; break;

			case "animal" : this.symbol = Symbol.ANIMAL; break;

			case "insect" : this.symbol = Symbol.INSECT; break;

			case "plant" : this.symbol = Symbol.PLANT; break;

			case "inkwell" : this.symbol = Symbol.INKWELL; break;

			case "manuscript" : this.symbol = Symbol.MANUSCRIPT; break;

			case "quill" : this.symbol = Symbol.QUILL; break;

			case "none" : this.symbol = Symbol.NULL; break;
		}
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
