package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;


public class ResourceCard extends PlayableCard{
    /** This attribute represents the kingdom of a card (i.e. the color of the card) */
    private Symbol kingdom;
    /** This attribute is used to know if a card is  played face up or face down */
    private boolean face; //true is face up, false is face down
    /** These are the 8 corner of each card */
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE, faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    /** These integers represent respectively the universal number of a card and the points it gives once played */
    private int cardID, pointsWon;
    /** These attributes are used to get the image from the json */
    private String imgFront, imgBack;

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
    }
}
