package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Player;

/**
 * This class represents the gold cards with their parameters
 */
public class GoldCard extends PlayableCard{

    /** This array contains the kingdoms needed to place a gold card */
    private Symbol playableCondition[] = {null,null,null,null,null};
    /** This attribute contains the type of condition in order to get points once the gold card is placed */
    private Symbol pointsCondition = null;

    /**
     * Constructor of gold cards that receives data from the class GoldDeck and put them in a card
     * all the parameters comes from goldCard.json
     *
     * @param id                    universal id of the card
     * @param kingdom               kingdom of the card(i.e. color)
     * @param imgFront              string that contains the path to the .jpg
     * @param imgBack               string that contains the path to the .jpg
     * @param condPointType         type of condition to get points
     * @param pointGiven            number of points obtained once played the card
     * @param FNW                   front north-west corner
     * @param FNE                   front north-est corner
     * @param FSW                   front south-west corner
     * @param FSE                   front south-west corner
     * @param BNW                   back north-west corner
     * @param BNE                   back north-est corner
     * @param BSW                   back south-west corner
     * @param BSE                   back south-west corner
     * @param first                 first kingdom needed to place the card(to be put in an array)
     * @param second                second kingdom needed to place the card(to be put in an array)
     * @param third                 third kingdom needed to place the card(to be put in an array)
     * @param fourth                fourth kingdom needed to place the card(to be put in an array)
     * @param fifth                 fifth kingdom needed to place the card(to be put in an array)
     */
    public GoldCard(int id,String kingdom,String imgFront,String imgBack,String condPointType,int pointGiven,String FNW,String FNE,String FSW,String FSE,
                    String BNW,String BNE,String BSW,String BSE,String first,String second,String third,String fourth,String fifth) {  //creating cards

        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;
            case "animal" : this.kingdom = Symbol.ANIMAL; break;
            case "plant" : this.kingdom = Symbol.PLANT; break;
            case "insect" : this.kingdom = Symbol.INSECT; break;
        }

        this.imgBack = imgBack;
        this.imgFront = imgFront;
        this.pointsPerCondition = pointGiven;
        
        switch(first){
            case "fungi" :  this.playableCondition[0] = Symbol.FUNGI; break;
            case "plant" :  this.playableCondition[0] = Symbol.PLANT; break;
            case "animal" : this.playableCondition[0] = Symbol.ANIMAL; break;
            case "insect" : this.playableCondition[0] = Symbol.INSECT; break;
            case "null" :   this.playableCondition[0] = null; break;
        }

        switch(second){
            case "fungi" :  this.playableCondition[1] = Symbol.FUNGI; break;
            case "plant" :  this.playableCondition[1] = Symbol.PLANT; break;
            case "animal" : this.playableCondition[1] = Symbol.ANIMAL; break;
            case "insect" : this.playableCondition[1] = Symbol.INSECT; break;
            case "null" :   this.playableCondition[1] = null; break;
        }

        switch(third){
            case "fungi":  this.playableCondition[2] = Symbol.FUNGI; break;
            case "plant":  this.playableCondition[2] = Symbol.PLANT; break;
            case "animal": this.playableCondition[2] = Symbol.ANIMAL; break;
            case "insect": this.playableCondition[2] = Symbol.INSECT; break;
            case "null":   this.playableCondition[2] = null; break;
        }

        switch(fourth){
            case "fungi":  this.playableCondition[3] = Symbol.FUNGI; break;
            case "plant":  this.playableCondition[3] = Symbol.PLANT; break;
            case "animal": this.playableCondition[3] = Symbol.ANIMAL; break;
            case "insect": this.playableCondition[3] = Symbol.INSECT; break;
            case "null":   this.playableCondition[3] = null; break;
        }

        switch(fifth){
            case "fungi" :  this.playableCondition[4] = Symbol.FUNGI; break;
            case "plant" :  this.playableCondition[4] = Symbol.PLANT; break;
            case "animal" : this.playableCondition[4] = Symbol.ANIMAL; break;
            case "insect" : this.playableCondition[4] = Symbol.INSECT; break;
            case "null" :   this.playableCondition[4] = null; break;
        }

        switch (condPointType){
            case "quill": this.pointsCondition = Symbol.QUILL; break;
            case "inkwell": this.pointsCondition = Symbol.INKWELL; break;
            case "manuscript": this.pointsCondition = Symbol.MANUSCRIPT; break;
            case "corner": this.pointsCondition = Symbol.CORNER; break;
            case "null": this.pointsCondition = null; break;
        }

        this.faceUpNW = FNW.equals("null") ? null : new Corner(FNW);
        this.faceUpNE = FNE.equals("null") ? null : new Corner(FNE);
        this.faceUpSW = FSW.equals("null") ? null : new Corner(FSW);
        this.faceUpSE = FSE.equals("null") ? null : new Corner(FSE);

        this.faceDownNW = BNW.equals("null") ? null : new Corner(BNW);
        this.faceDownNE = BNE.equals("null") ? null : new Corner(BNE);
        this.faceDownSW = BSW.equals("null") ? null : new Corner(BSW);
        this.faceDownSE = BSE.equals("null") ? null : new Corner(BSE);
    }

    /**
     * @return the array that contains the kingdom needed visible on a player field to play the gold card
     */
    public Symbol[] getGoldPlayableCondition(){
        return this.playableCondition;
    }

    /**
     * @return the condition type to get points after have played the card
     */
    public Symbol getGoldPointsCondition(){
        return this.pointsCondition;
    }

    /**
     * method used by the Player to play this PlayableCard
     * @param player the one who has the PlayableCard in Hand and wants to play it
     * @param coords the place they want to play it
     * @return the points scored by the Player (if any were scored)
     * @throws NotPlaceableException if the position chosen isn't valid
     */
    public int play(Player player, Coords coords) throws NotPlaceableException {
        int pts = player.getGameField().tryPlaceCard(this, coords);
        player.getHand().removeCard(this);
        return pts;
    }
}
