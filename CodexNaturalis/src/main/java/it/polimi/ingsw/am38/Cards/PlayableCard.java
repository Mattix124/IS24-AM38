package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;

import java.util.Objects;

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
			if((Objects.equals(orientation.toString(), "NW"))){			//that it may or may not contain
				return this.faceUpNW;										//an object
			}
			else if((Objects.equals(orientation.toString(), "NE"))){
				return this.faceUpNE;
			}
			else if((Objects.equals(orientation.toString(), "SW"))){
				return this.faceUpSW;
			}
			else if((Objects.equals(orientation.toString(), "SE"))){
				return this.faceUpSE;
			}
		}
		else if(face == false){
			if((Objects.equals(orientation.toString(), "NW"))){
				return this.faceDownNW;
			}
			else if((Objects.equals(orientation.toString(), "NE"))){
				return this.faceDownNE;
			}
			else if((Objects.equals(orientation.toString(), "SW"))){
				return this.faceDownSW;
			}
			else if((Objects.equals(orientation.toString(), "SE"))){
				return this.faceDownSE;
			}
		}

		return null;
	}
}
