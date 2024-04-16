package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Model.Miscellaneous.GoldCardPlayableCondition;
import it.polimi.ingsw.am38.Model.Miscellaneous.GoldCardPointsCondition;


public class GoldCard extends PlayableCard{

    String imgFront, imgBack;
    private Kingdom kingdom;
    private Kingdom kingdomNeeded;
    private int cardID, pointsWon;
    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private GoldCardPointsCondition pointsCondition;
    private GoldCardPlayableCondition playableCondition;

    //pointsCondition miss here
    public GoldCard(int id,String kingdom,String imgFront,String imgBack,String condPointType,int pointGiven,String FNW,String FNE,String FSW,String FSE,
                    String BNW,String BNE,String BSW,String BSE,String first,String second,String third,String fourth,String fifth){  //creating cards

        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Kingdom.FUNGI; break;

            case "animal" : this.kingdom = Kingdom.ANIMAL; break;

            case "plant" : this.kingdom = Kingdom.PLANT; break;

            case "insect" : this.kingdom = Kingdom.INSECT; break;
        }

        if(imgBack == null){
            this.imgBack = null;
        }else this.imgBack = imgBack;

        if(imgFront == null){
            this.imgFront = null;
        }else this.imgFront = imgFront;

        this.pointsWon = pointGiven;

        Corner faceUpNW = new Corner(FNW);
        Corner faceUpNE = new Corner(FNE);
        Corner faceUpSW = new Corner(FSW);
        Corner faceUpSE = new Corner(FSE);

        Corner faceDownNW = new Corner(BNW);
        Corner faceDownNE = new Corner(BNE);
        Corner faceDownSW = new Corner(BSW);
        Corner faceDownSE = new Corner(BSE);

    }
    public int pointCheck(){
        return 0; //momentary
    }
}
