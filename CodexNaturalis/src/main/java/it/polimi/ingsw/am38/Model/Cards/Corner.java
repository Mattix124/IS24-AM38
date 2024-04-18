package it.polimi.ingsw.am38.Model.Cards;


import it.polimi.ingsw.am38.Enum.Symbol;

/**
 * Class Corner is used to keep track of the 8 angles of each card,
 * 4 in the front and 4 in the back, with respective kingdom/item contained
 */
public class Corner
{
	/** occupied is used to know if a certain corner is covered by another card */
	private boolean occupied;
	/** symbol contains the object visualized in each corner */
	private Symbol symbol;

	private boolean checked;
	/** this constructor uses the data from the json and crate a new corner, setting the parameter occupied
	 * to false */
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

	/** This method used in Field class to check if a card could be placed on a corner */
	public boolean isChecked()
	{
		return checked;
	}
	/** This method used in Field once a check to see if a card could be placed is done */
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	/** @return if a corner is covered by another card */
	public boolean isOccupied(){
		return occupied;
	}

	/** @return the symbol contained in the corner(could be NULL) */
	public Symbol getSymbol(){
		return symbol;
	}

	/** @param occupied is set to true when a card cover the corner */
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
}
