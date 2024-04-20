package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

/**
 * This class represents the resource cards with their parameters
 */
public class ResourceCard extends PlayableCard{

    /**
     * Constructor of resource cards that receives data from the class ResourceDeck and put them in a card
     * all the parameters comes from resourceCard.json
     *
     * @param id                    universal id of the card
     * @param kingdom               kingdom of the card(i.e. color)
     * @param imgFront              string that contains the path to the .jpg
     * @param imgBack               string that contains the path to the .jpg
     * @param pointGiven            number of points obtained once played the card
     * @param FNW                   front north-west corner
     * @param FNE                   front north-est corner
     * @param FSW                   front south-west corner
     * @param FSE                   front south-west corner
     * @param BNW                   back north-west corner
     * @param BNE                   back north-est corner
     * @param BSW                   back south-west corner
     * @param BSE                   back south-west corner
     */
    public ResourceCard(int id,String kingdom,String imgFront,String imgBack,int pointGiven,String FNW,String FNE,String FSW,String FSE,
                        String BNW,String BNE,String BSW,String BSE){
        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;
            case "animal" : this.kingdom = Symbol.ANIMAL; break;
            case "plant" : this.kingdom = Symbol.PLANT; break;
            case "insect" : this.kingdom = Symbol.INSECT; break;
        }

        if(imgBack.equals("null")){
            this.imgBack = null;
        }else this.imgBack = imgBack;

        if(imgFront.equals("null")){
            this.imgFront = null;
        }else this.imgFront = imgFront;

        this.pointsWon = pointGiven;

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
}
