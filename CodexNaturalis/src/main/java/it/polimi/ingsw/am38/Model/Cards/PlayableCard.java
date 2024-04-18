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
	private int order;
	/** This attribute contains the points assigned to a player if the card it's played */
	private int pointsWon;
	/** This attribute represents the kingdom of a card (i.e. the color of the card) */
	private Symbol kingdom;
	/** This attribute is used to know if a card is  played face up or face down */
	private boolean face; //true is face up, false is face down
	/** These attributes are the 8 corner of each card */
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE, faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	/** @return the visible side of the card placed on the field */
	public boolean checkPlacement() {
		return this.face; //return the visible side of the card that's been played
	}

	/** @param orientation is the coordinate (NW,NE,SW,SE) of the corner needed
	 *
	 *  @return the corner in a specific position on the card
	 */
	public Corner getCorner(Orientation orientation) {	//based on how the card is placed function return a certain angle
		if(face){ // if face up							//that it may or may not contain an object
            return switch (orientation) {
                case NW -> this.faceUpNW;
                case NE -> this.faceUpNE;
                case SW -> this.faceUpSW;
                case SE -> this.faceUpSE;
            };
		}
		else{ // if face down
            return switch (orientation) {
                case NW -> this.faceDownNW;
                case NE -> this.faceDownNE;
                case SW -> this.faceDownSW;
                case SE -> this.faceDownSE;
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

	/** This method set the parameter that represents the side visible of the card on te field */
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
