package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;

import java.util.Objects;

public abstract class PlayableCard extends Card
{
	private int order;
	private boolean face; //true is face up, false is face down
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
	private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	public boolean checkPlacement() {
		return this.face; 		//return the visible side of the card that's been played
	}

	public Corner getCorner(Orientation orientation) {		//based on how the card is placed
		if(face){											//function return a certain angle
			if(orientation == Orientation.NW){				//that it may or may not contain
				return this.faceUpNW;						//an object
			}
			else if(orientation == Orientation.NE){
				return this.faceUpNE;
			}
			else if(orientation == Orientation.SW){
				return this.faceUpSW;
			}
			else if(orientation == Orientation.SE){
				return this.faceUpSE;
			}
		}
		else{
			if(orientation == Orientation.NW){
				return this.faceDownNW;
			}
			else if(orientation == Orientation.NE){
				return this.faceDownNE;
			}
			else if(orientation == Orientation.SW){
				return this.faceDownSW;
			}
			else if(orientation == Orientation.SE){
				return this.faceDownSE;
			}
		}

		return null;
	}
}
