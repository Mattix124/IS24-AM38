package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

import java.util.Objects;

/**
 * This class represents the gold cards with their parameters
 */
public class GoldCard extends PlayableCard{

    /** These attributes are used to get the image from the json */
    String imgFront, imgBack;
    /** This attribute represents the kingdom of a card (i.e. the color of the card) */
    private Symbol kingdom;
    /** These integers represent respectively the universal number of a card and the points it gives once played */
    private int cardID, pointsWon;
    /** This attribute is used to know if a card is  played face up or face down */
    private boolean face; //true is face up, false is face down
    /** These are the 8 corner of each card */
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE, faceDownNW, faceDownNE, faceDownSW, faceDownSE;
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
                    String BNW,String BNE,String BSW,String BSE,String first,String second,String third,String fourth,String fifth){  //creating cards

        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;

            case "animal" : this.kingdom = Symbol.ANIMAL; break;

            case "plant" : this.kingdom = Symbol.PLANT; break;

            case "insect" : this.kingdom = Symbol.INSECT; break;
        }

        if(Objects.equals(imgBack, "null")){
            this.imgBack = null;
        }else this.imgBack = imgBack;

        if(Objects.equals(imgFront, "null")){
            this.imgFront = null;
        }else this.imgFront = imgFront;

        this.pointsWon = pointGiven;
        
        switch(first){
            case("fungi"):  this.playableCondition[0] = Symbol.FUNGI; break;
            case("plant"):  this.playableCondition[0] = Symbol.PLANT; break;
            case("animal"): this.playableCondition[0] = Symbol.ANIMAL; break;
            case("insect"): this.playableCondition[0] = Symbol.INSECT; break;
            case("null"):   this.playableCondition[0] = null; break;
        }

        switch(second){
            case("fungi"):  this.playableCondition[1] = Symbol.FUNGI; break;
            case("plant"):  this.playableCondition[1] = Symbol.PLANT; break;
            case("animal"): this.playableCondition[1] = Symbol.ANIMAL; break;
            case("insect"): this.playableCondition[1] = Symbol.INSECT; break;
            case("null"):   this.playableCondition[1] = null; break;
        }

        switch(third){
            case("fungi"):  this.playableCondition[2] = Symbol.FUNGI; break;
            case("plant"):  this.playableCondition[2] = Symbol.PLANT; break;
            case("animal"): this.playableCondition[2] = Symbol.ANIMAL; break;
            case("insect"): this.playableCondition[2] = Symbol.INSECT; break;
            case("null"):   this.playableCondition[2] = null; break;
        }

        switch(fourth){
            case("fungi"):  this.playableCondition[3] = Symbol.FUNGI; break;
            case("plant"):  this.playableCondition[3] = Symbol.PLANT; break;
            case("animal"): this.playableCondition[3] = Symbol.ANIMAL; break;
            case("insect"): this.playableCondition[3] = Symbol.INSECT; break;
            case("null"):   this.playableCondition[3] = null; break;
        }

        switch(fifth){
            case("fungi"):  this.playableCondition[4] = Symbol.FUNGI; break;
            case("plant"):  this.playableCondition[4] = Symbol.PLANT; break;
            case("animal"): this.playableCondition[4] = Symbol.ANIMAL; break;
            case("insect"): this.playableCondition[4] = Symbol.INSECT; break;
            case("null"):   this.playableCondition[4] = null; break;
        }

        switch (condPointType){
            case "quill": this.pointsCondition = Symbol.QUILL; break;
            case "inkwell": this.pointsCondition = Symbol.INKWELL; break;
            case "manuscript": this.pointsCondition = Symbol.MANUSCRIPT; break;
            case "corner": this.pointsCondition = Symbol.CORNER; break;
            case "null": this.pointsCondition = null; break;
        }

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
    }

    /** @return the array that contains the kingdom needed visible on a player field to play the gold card */
    public Symbol[] getGoldPlayableCondition(){
        return this.playableCondition;
    }

    /** @return the condition type to get points after have played the card */
    public Symbol getGoldPointsCondition(){
        return this.pointsCondition;
    }

}
