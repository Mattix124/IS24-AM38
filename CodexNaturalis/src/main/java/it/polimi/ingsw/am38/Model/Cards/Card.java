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
    /** @return the universal number of a card */
    public int getCardID() {return cardID; }
}
