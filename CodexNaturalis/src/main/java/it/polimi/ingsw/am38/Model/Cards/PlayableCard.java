package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Orientation;

import java.util.Objects;

public abstract class PlayableCard extends Card
{
	private int order;

	private int pointsWon;

	private Kingdom kingdom;
	private boolean face; //true is face up, false is face down
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
	private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	public boolean checkPlacement() {
		return this.face; //return the visible side of the card that's been played
	}

	public Corner getCorner(Orientation orientation) {	//based on how the card is placed function return a certain angle
		if(face){ // if face up							//that it may or may not contain an object
			switch(orientation){
				case Orientation.NW: return this.faceUpNW;
				case Orientation.NE: return this.faceUpNE;
				case Orientation.SW: return this.faceUpSW;
				case Orientation.SE: return this.faceUpSE;
			}
		}
		else{ // if face down
			switch(orientation){
				case Orientation.NW: return this.faceDownNW;
				case Orientation.NE: return this.faceDownNE;
				case Orientation.SW: return this.faceDownSW;
				case Orientation.SE: return this.faceDownSE;
			}
		}
		return null;
	}

	public int getOrder()
	{
		return order;
	}
	public void setOrder(int order)
	{
		this.order = order;
	}
	public boolean getFace()
	{
		return face;
	}
	public void setFace(boolean face)
	{
		this.face = face;
	}
	public Kingdom getKingdom()
	{
		return kingdom;
	}
	public int getPointsWon()
	{
		return pointsWon;
	}
}
