package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

/**
 * This class represents the starter cards with their parameters
 */
public class StarterCard extends PlayableCard{
	/** This attribute contains the kingdoms in the middle of the front side of the card */
	Symbol centralKingdom[] = {null,null,null};

	/**
	 *
	 * @param id                    universal id of the card
	 * @param imgFront              string that contains the path to the .jpg
	 * @param imgBack               string that contains the path to the .jpg
	 * @param FNW                   front north-west corner
	 * @param FNE                   front north-east corner
	 * @param FSW                   front south-west corner
	 * @param FSE                   front south-east corner
	 * @param BNW                   back north-west corner
	 * @param BNE                   back north-est corner
	 * @param BSW                   back south-west corner
	 * @param BSE                   back south-east corner
	 * @param first					contains one possible kingdom in the middle of the front face of the card
	 * @param second				contains one possible kingdom in the middle of the front face of the card
	 * @param third					contains one possible kingdom in the middle of the front face of the card
	 */
	public StarterCard(int id, String imgFront, String imgBack, String FNW, String FNE, String FSW, String FSE,
					   		String BNW, String BNE, String BSW, String BSE, String first, String second, String third){
		this.cardID = id;

		if(imgBack.equals("null")){
			this.imgBack = null;
		}else this.imgBack = imgBack;

		if(imgFront.equals("null")){
			this.imgFront = null;
		}else this.imgFront = imgFront;

		if(FNW.equals("null")) {this.faceUpNW = null;}
		else{this.faceUpNW = new Corner(FNW);}
		if(FNE.equals("null")) {this.faceUpNE = null;}
		else{this.faceUpNE = new Corner(FNE);}
		if(FSW.equals("null")) {this.faceUpSW = null;}
		else{this.faceUpSW = new Corner(FSW);}
		if(FSE.equals("null")) {this.faceUpSE = null;}
		else{this.faceUpSE = new Corner(FSE);}

		if(BNW.equals("null")) {this.faceDownNW = null;}
		else{this.faceDownNW = new Corner(BNW);}
		if(BNE.equals("null")) {this.faceDownNE = null;}
		else{this.faceDownNE = new Corner(BNE);}
		if(BSW.equals("null")) {this.faceDownSW = null;}
		else{this.faceDownSW = new Corner(BSW);}
		if(BSE.equals("null")) {this.faceDownSE = null;}
		else{this.faceDownSE = new Corner(BSE);}

		switch(first){
			case "fungi" : this.centralKingdom[0] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[0] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[0] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[0] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[0] = Symbol.NULL; break;
		}
		switch(second){
			case "fungi" : this.centralKingdom[1] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[1] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[1] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[1] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[1] = Symbol.NULL; break;
		}
		switch(third){
			case "fungi" : this.centralKingdom[2] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[2] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[2] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[2] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[2] = Symbol.NULL; break;
		}
	}

	/** @return the kingdoms in the middle of the front face of the card */
	public Symbol[] getCentralKingdom() {
		return centralKingdom;
	}

}
