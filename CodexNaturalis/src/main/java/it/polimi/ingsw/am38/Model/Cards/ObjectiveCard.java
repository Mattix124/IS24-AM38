package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;

import static it.polimi.ingsw.am38.Enum.Orientation.*;
import static it.polimi.ingsw.am38.Enum.Symbol.*;

public class ObjectiveCard extends Card{

    /** This attribute contains the type of mission of the card */
    String objType;
    /** These attributes are used to get the image from the json */
    String imgFront, imgBack;
    /** These integers represent respectively the universal number of a card and the points it gives once completed the mission */
    int cardID, pointsGiven;
    /** This attribute is an array that contains three specific integer to recognize a mission pattern */
    int diagonalParameters[] = {0,0,0};
    /** This attribute is used to represent the kingdom needed in certain mission pattern  */
    Symbol kingdom;
    /** This attribute is used to distinguish the different card in what we called the shape L mission */
    Symbol kingdom2;
    /** This attribute is used to identify the relative position of the different card in the shape L mission */
    Orientation position;
    /** This attribute contains the item for certain mission (e.g. inkwell, manuscript or quill) */
    Symbol item;
    /** This attribute is used to know if a card is common to all the players or private */
    boolean personalOrShared;

    /**
     * Constructor of objective cards that receives data from the class ObjectiveCard and put them in a card
     * all the parameters comes from objectiveCard.json
     *
     * @param id                    universal id of the card
     * @param kingdom               kingdom of the card(i.e. color)
     * @param imgFront              string that contains the path to the .jpg
     * @param imgBack               string that contains the path to the .jpg
     * @param objType               type of the mission; from the json
     * @param pointsGiven           points gained after have played the card
     * @param kingdom2              kingdom of the different card in the L shaped mission
     * @param position              relative position of the different card in the L shaped mission
     * @param item                  item needed for the relative missions
     * @param first                 index for the pattern of the diagonal mission
     * @param second                index for the pattern of the diagonal mission
     * @param third                 index for the pattern of the diagonal mission
     */
    public ObjectiveCard(int id, String kingdom, String objType, String imgFront, String imgBack,
                            int pointsGiven, String kingdom2, String position, String item, int first, int second, int third ){  //to be improved

        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;

            case "animal" : this.kingdom = Symbol.ANIMAL; break;

            case "plant" : this.kingdom = Symbol.PLANT; break;

            case "insect" : this.kingdom = Symbol.INSECT; break;

            case "null" : this.kingdom = null; break;
        }

        this.objType = objType;

        this.imgFront = imgFront;
        this.imgBack = imgBack;

        this.pointsGiven = pointsGiven;

        switch(kingdom2){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;

            case "animal" : this.kingdom = Symbol.ANIMAL; break;

            case "plant" : this.kingdom = Symbol.PLANT; break;

            case "insect" : this.kingdom = Symbol.INSECT; break;

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

        this.diagonalParameters[0] = first;
        this.diagonalParameters[1] = second;
        this.diagonalParameters[2] = third;

    }
    /** @return true if the objective card is common, false if its personal */
    private boolean isPublic(){
        return personalOrShared;
    }
    /** @return the type of mission */
    public String getObjType() {
        return objType;
    }
    /** @return the points given once completed the mission */
    public int getPointsGiven() {
        return pointsGiven;
    }
    /** This method set the visibility of a card, i.e. true if is common, false if private */
    public boolean setVisibility(boolean visibility){
        return personalOrShared = visibility;
    }

    public int[] getDiagonalParameters()
    {
        return diagonalParameters;
    }

    public Symbol getKingdom()
    {
        return kingdom;
    }

    public Symbol getKingdom2()
    {
        return kingdom2;
    }

    public Orientation getPosition()
    {
        return position;
    }

    public Symbol getItem()
    {
        return item;
    }
}
