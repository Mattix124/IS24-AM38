package it.polimi.ingsw.am38.Model.Cards;

public abstract class Card {
    /**
     * This attribute represent the universal number of a card
     */
    protected int cardID;
    /**
     * These attributes are used to get the image from the json
     */
    protected String imgFront, imgBack;
    /**
     * This attribute is used to know if a card is  played face up or face down
     */
    protected boolean face = true; //true is face up, false is face down

    /**
     * Getter method for the face of this card
     * @return a String containing the path to obtain the image
     */
    public String getImg(){
        if(face)
            return imgFront;
        else
            return imgBack;
    }
    /** @return the universal number of a card */
    public int getCardID() {return cardID; }
}
