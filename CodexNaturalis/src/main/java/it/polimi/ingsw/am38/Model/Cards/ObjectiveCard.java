package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;

import static it.polimi.ingsw.am38.Enum.Orientation.*;
import static it.polimi.ingsw.am38.Enum.Symbol.*;

public class ObjectiveCard extends Card{

    String objType, imgFront, imgBack;
    int cardID, pointsGiven, first = 0, second = 0, third = 0;
    Kingdom kingdom, kingdom2;

    Orientation position;

    Symbol item;
    boolean personalOrShared;
    public ObjectiveCard(int id, String kingdom, String objType, String imgFront, String imgBack,
                            int pointsGiven, String kingdom2, String position, String item, int first, int second, int third ){  //to be improved

        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Kingdom.FUNGI; break;

            case "animal" : this.kingdom = Kingdom.ANIMAL; break;

            case "plant" : this.kingdom = Kingdom.PLANT; break;

            case "insect" : this.kingdom = Kingdom.INSECT; break;

            case "null" : this.kingdom = null; break;
        }

        this.objType = objType;

        this.imgFront = imgFront;
        this.imgBack = imgBack;

        this.pointsGiven = pointsGiven;

        switch(kingdom2){
            case "fungi" : this.kingdom = Kingdom.FUNGI; break;

            case "animal" : this.kingdom = Kingdom.ANIMAL; break;

            case "plant" : this.kingdom = Kingdom.PLANT; break;

            case "insect" : this.kingdom = Kingdom.INSECT; break;

            case "null" : this.kingdom = null; break;
        }

        switch(item){
            case "quill" : this.item = QUILL; break;

            case "inkwell" : this.item = INKWELL; break;

            case "manuscript" : this.item = MANUSCRIPT; break;

            case "null" : this.item = null; break;
        }

        switch(position){
            case "NW" : this.position = NW; break;

            case "NE" : this.position = NE; break;

            case "SW" : this.position = SW; break;

            case "SE" : this.position = SE; break;

            case "null" : this.item = null; break;
        }

        this.first = first;
        this.second = second;
        this.third = third;

    }
    private boolean isPublic(){

        return personalOrShared;
    }

    public String getObjType()
    {
        return objType;
    }

    public int getPointsGiven()
    {
        return pointsGiven;
    }

    public boolean setVisibility(boolean visibility){
        return personalOrShared = visibility;
    }
}
