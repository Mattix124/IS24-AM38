package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;


public class ResourceCard extends PlayableCard{

    private Symbol kingdom;

    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private int cardID, pointsWon;
    private String imgFront, imgBack;

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
