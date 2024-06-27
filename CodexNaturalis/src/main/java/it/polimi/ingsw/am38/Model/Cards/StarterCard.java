package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Player;

/**
 * This class represents the starter cards with their parameters
 */
public class StarterCard extends PlayableCard{
	/**
	 * This attribute contains the kingdoms in the middle of the front side of the card *
	 */
	Symbol[] centralKingdom = {null,null,null};

	/**
	 * Constructor method
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
		this.imgBack = imgBack;
		this.imgFront = imgFront;

		this.faceUpNW = FNW.equals("null") ? null : new Corner(FNW);
		this.faceUpNE = FNE.equals("null") ? null : new Corner(FNE);
		this.faceUpSW = FSW.equals("null") ? null : new Corner(FSW);
		this.faceUpSE = FSE.equals("null") ? null : new Corner(FSE);

		this.faceDownNW = BNW.equals("null") ? null : new Corner(BNW);
		this.faceDownNE = BNE.equals("null") ? null : new Corner(BNE);
		this.faceDownSW = BSW.equals("null") ? null : new Corner(BSW);
		this.faceDownSE = BSE.equals("null") ? null : new Corner(BSE);

		switch(first){
			case "fungi" : this.centralKingdom[0] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[0] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[0] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[0] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[0] = null; break;
		}
		switch(second){
			case "fungi" : this.centralKingdom[1] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[1] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[1] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[1] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[1] = null; break;
		}
		switch(third){
			case "fungi" : this.centralKingdom[2] = Symbol.FUNGI; break;
			case "animal" : this.centralKingdom[2] = Symbol.ANIMAL; break;
			case "plant" : this.centralKingdom[2] = Symbol.PLANT; break;
			case "insect" : this.centralKingdom[2] = Symbol.INSECT; break;
			case "null" : this.centralKingdom[2] = null; break;
		}
	}

	/**
	 * Getter for the kingdoms in the center of the card
	 *
	 * @return the kingdoms in the middle of the front face of the card
	 */
	public Symbol[] getCentralKingdom() {
		if(face)
			return centralKingdom;
		else{
            return new Symbol[]{null,null,null};
		}
	}

	/**
	 * Method used to place the StarterCard of the Player given as parameter,
	 * this also creates the Player's gameField
	 *
	 * @param player the one who plays the StarterCard
	 * @param coords the position in which the Player wants to play it (in this case it doesn't matter
	 *               since it's their first PlayableCard played)
	 * @return the number of points scored by placing the StarterCard (always 0)
	 */
	public int play(Player player, Coords coords){
		player.setGameField(new Field(this));
		return 0;
	}
}
