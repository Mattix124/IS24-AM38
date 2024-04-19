package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

public class StarterCard extends PlayableCard{
	/** This attribute represent the universal number of a card */
	int cardID;
	/** This attribute contains the kingdoms in the middle of the front side of the card */
	Symbol centralKingdom[] = {null,null,null};
	/** These attributes are used to get the image from the json */
	String imgFront, imgBack;
	/** This attribute is used to know if a card is  played face up or face down */
	private boolean face; //true is face up, false is face down
	/** This attribute represents the kingdom of a card */
	private final Symbol kingdom = Symbol.NULL;
	/** These are the 8 corner of each card */
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE, faceDownNW, faceDownNE, faceDownSW, faceDownSE;

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

		if(FNW.equals("null")) {faceUpNW = null;}
		else{Corner faceUpNW = new Corner(FNW);}
		if(FNE.equals("null")) {faceUpNE = null;}
		else{Corner faceUpNE = new Corner(FNE);}
		if(FSW.equals("null")) {faceUpSW = null;}
		else{Corner faceUpSW = new Corner(FSW);}
		if(FSE.equals("null")) {faceUpSE = null;}
		else{Corner faceUpSE = new Corner(FSE);}

		if(BNW.equals("null")) {faceDownNW = null;}
		else{Corner faceDownNW = new Corner(BNW);}
		if(BNE.equals("null")) {faceDownNE = null;}
		else{Corner faceDownNE = new Corner(BNE);}
		if(BSW.equals("null")) {faceDownSW = null;}
		else{Corner faceDownSW = new Corner(BSW);}
		if(BSE.equals("null")) {faceDownSE = null;}
		else{Corner faceDownSE = new Corner(BSE);}

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
