package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;

public abstract class PlayableCard extends Card
{
	private boolean face; //true is face up, false is face down
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
	private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	public boolean checkPlacement() {
		return this.face; 		//return the visible side of the card that's been played
	}

	public Corner getCorner(boolean face, Orientation orientation) {		//based on how the card is placed
		if(face == true){													//function return a certain angle
			if((orientation == Orientation.NW)&&(faceUpNW.getExists())){			//that it may or may not contain
				return this.faceUpNW;										//an object
			}
			else if((orientation == Orientation.NE)&&(faceUpNE.getExists())){
				return this.faceUpNE;
			}
			else if((orientation == Orientation.SW)&&(faceUpSW.getExists())){
				return this.faceUpSW;
			}
			else if((orientation == Orientation.SE)&&(faceUpSE.getExists())){
				return this.faceUpSE;
			}
		}
		else if(face == false){
			if((orientation == Orientation.NW)&&(faceDownNW.getExists())){
				return this.faceDownNW;
			}
			else if((orientation == Orientation.NE)&&(faceDownNE.getExists())){
				return this.faceDownNE;
			}
			else if((orientation == Orientation.SW)&&(faceDownSW.getExists())){
				return this.faceDownSW;
			}
			else if((orientation == Orientation.SE)&&(faceDownSE.getExists())){
				return this.faceDownSE;
			}
		}

		return null;
	}
}
