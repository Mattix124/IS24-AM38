package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;

import java.util.Objects;

/**
 * This is the abstract class that represents the cards that the player could place in his field
 */
public abstract class PlayableCard extends Card
{

	/** This attribute is used to keep track in which turn the card has been played */
	protected int order = 0;
	/** This attribute contains the points assigned to a player if the card it's played */
	protected int pointsWon;
	/** This attribute represents the kingdom of a card (i.e. the color of the card) */
	protected Symbol kingdom;
	/** This attribute is used to know if a card is  played face up or face down */
	protected boolean face; //true is face up, false is face down
	/** These attributes are the 8 corner of each card */
	protected Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE, faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	/** @param orientation is the coordinate (NW,NE,SW,SE) of the corner needed
	 *
	 *  @return the corner in a specific position on the card
	 */
	public Corner getCorner(Orientation orientation) {    //based on how the card is placed function return a certain angle
		if (face) { // if face up							//that may or may not contain an object
			return switch (orientation) {
				case Orientation.NW -> this.faceUpNW;
				case Orientation.NE -> this.faceUpNE;
				case Orientation.SW -> this.faceUpSW;
				case Orientation.SE -> this.faceUpSE;
			};
		} else { // if face down
			return switch (orientation) {
				case Orientation.NW -> this.faceDownNW;
				case Orientation.NE -> this.faceDownNE;
				case Orientation.SW -> this.faceDownSW;
				case Orientation.SE -> this.faceDownSE;
			};
		}
	}

	/** @return the int that represents the turn in which the card has been played */
	public int getOrder() {
		return order;
	}

	/** This method set the parameter that represents the turn in which the card has been played */
	public void setOrder(int order) {
		this.order = order;
	}

	/** @return the side of the card visible once it has been played */
	public boolean getFace() {
		return face;
	}

	/** This method set the parameter that represents the side visible of the card on the field */
	public void setFace(boolean face) {
		this.face = face;
	}

	/** @return the kingdom of a card (i.e. the color of the card)*/
	public Symbol getKingdom() {
		return kingdom;
	}

	/** @return the points given to the player once the card has been played */
	public int getPointsWon() {
		return pointsWon;
	}
}
