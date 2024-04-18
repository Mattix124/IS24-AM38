package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

public class StarterCard extends PlayableCard{
	int cardID, first, second, third;

	String imgFront, imgBack;
	private boolean face; //true is face up, false is face down

	private final Symbol kingdom = Symbol.NULL;
	private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
	private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

	public StarterCard(int id, String imgFront, String imgBack, String FNW, String FNE, String FSW, String FSE,
					   		String BNW, String BNE, String BSW, String BSE, int first, int second, int third){
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

		this.first = first;
		this.second = second;
		this.third = third;
	}
}
