package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;

import static it.polimi.ingsw.am38.Enum.Orientation.*;
import static it.polimi.ingsw.am38.Enum.Symbol.*;

/**
 * This class represents the objective cards with their parameters
 */
public class ObjectiveCard extends Card{

    /** This attribute contains the type of mission of the card */
    String objType;
    /** This integer represent the points it gives once completed the mission */
    private final int pointsGiven;
    /** This attribute is used to represent the kingdom needed in certain mission pattern  */
    private final Symbol kingdom;
    /** This attribute is used to distinguish the different card in what we called the shape L mission */
    private Symbol kingdom2;
    /** This attribute is used to identify the relative position of the different card in the shape L mission */
    private Orientation position;
    /** This attribute contains the item for certain mission (e.g. inkwell, manuscript or quill) */
    private Symbol item;
    /** This attribute is used to know if a card is common to all the players or private */
    boolean personalOrShared;
    /** Description of the objective, used by the CLI to print it on the screen */
    String description;

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
     * @param position              relative position of the different card in the L and D shaped mission
     * @param item                  item needed for the relative missions
     */
    public ObjectiveCard(int id, String kingdom, String objType, String imgFront, String imgBack,
                            int pointsGiven, String kingdom2, String position, String item, String description) {  //to be improved

        this.cardID = id;
        this.description = description;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;
            case "animal" : this.kingdom = Symbol.ANIMAL; break;
            case "plant" : this.kingdom = Symbol.PLANT; break;
            case "insect" : this.kingdom = Symbol.INSECT; break;
            default : this.kingdom = null; break;
        }

        this.objType = objType;
        this.imgBack = imgBack;
        this.imgFront = imgFront;

        this.pointsGiven = pointsGiven;

        switch(kingdom2){
            case "fungi" : this.kingdom2 = Symbol.FUNGI; break;
            case "animal" : this.kingdom2 = Symbol.ANIMAL; break;
            case "plant" : this.kingdom2 = Symbol.PLANT; break;
            case "insect" : this.kingdom2 = Symbol.INSECT; break;
            case "null" : this.kingdom2 = null; break;
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
            case "null" : this.position = null; break;
        }

    }
    /** @return true if the objective card is common, false if its personal */
    public boolean getVisibility(){
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
    /** @return the kingdom necessary to complete certain missions (e.g. trio of "animal", trio of "plant"
     * or even the kingdom of the card needed for the diagonal or shape L mission), (could be NULL)
     */
    public Symbol getKingdom() { return kingdom; }
    /** @return the kingdom of the different card in the shape L missions */
    public Symbol getKingdom2() { return kingdom2; }
    /** @return the relative position of the different card in the shape L missions */
    public Orientation getPosition() { return position; }
    /** @return the item needed for duo and trio missions (e.g. "quill", "manuscript" or even "all for the trio mission */
    public Symbol getItem() { return item; }
    public int getCardID() {return cardID; }
    public String getDescription(){
        return this.description;
    }
}
