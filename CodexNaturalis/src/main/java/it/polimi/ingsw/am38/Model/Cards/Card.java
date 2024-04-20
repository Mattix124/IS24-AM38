package it.polimi.ingsw.am38.Model.Cards;

public abstract class Card {
    /** This attribute represent the universal number of a card */
    int cardID;
    /** These attributes are used to get the image from the json */
    String imgFront, imgBack;

    public String getImgBack() { return imgBack; }

    public String getImgFront() { return imgFront; }

    /** @return the universal number of a card */
    public int getCardID() {return cardID; }
}
