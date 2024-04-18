package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;

import java.util.Objects;


public class GoldCard extends PlayableCard{

    String imgFront, imgBack;
    private Symbol kingdom;
    private int cardID, pointsWon;
    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private Symbol playableCondition[] = {null,null,null,null,null};
    private Symbol pointsCondition = null;

    
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

    public Symbol[] getGoldPlayableCondition(){
        return this.playableCondition;
    }

    public Symbol getGoldPointsCondition(){
        return this.pointsCondition;
    }
}
