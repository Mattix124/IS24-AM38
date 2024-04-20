package it.polimi.ingsw.am38.Model.Cards;

public abstract class Card {
    /** This attribute represent the universal number of a card */
    protected int cardID;
    /** These attributes are used to get the image from the json */
    protected String imgFront, imgBack;
    /** @return the path to the back image of the card */
    protected String getImgBack() { return imgBack; }
    /** @return the path to the back image of the card */
    protected String getImgFront() { return imgFront; }

    /** @return the universal number of a card */
    protected int getCardID() {return cardID; }
}
