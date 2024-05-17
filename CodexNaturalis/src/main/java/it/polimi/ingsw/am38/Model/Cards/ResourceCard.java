package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NoPossiblePlacement;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Player;

/**
 * This class represents the resource cards with their parameters
 */
public class ResourceCard extends PlayableCard{

    /**
     * Constructor of resource cards that receives data from the class ResourceDeck and put them in a card
     * all the parameters comes from resourceCard.json
     *
     * @param id                    universal id of the card
     * @param kingdom               kingdom of the card(i.e. color)
     * @param imgFront              string that contains the path to the .jpg
     * @param imgBack               string that contains the path to the .jpg
     * @param pointGiven            number of points obtained once played the card
     * @param FNW                   front north-west corner
     * @param FNE                   front north-est corner
     * @param FSW                   front south-west corner
     * @param FSE                   front south-west corner
     * @param BNW                   back north-west corner
     * @param BNE                   back north-est corner
     * @param BSW                   back south-west corner
     * @param BSE                   back south-west corner
     */
    public ResourceCard(int id,String kingdom,String imgFront,String imgBack,int pointGiven,String FNW,String FNE,String FSW,String FSE,
                        String BNW,String BNE,String BSW,String BSE){
        this.cardID = id;

        switch(kingdom){
            case "fungi" : this.kingdom = Symbol.FUNGI; break;
            case "animal" : this.kingdom = Symbol.ANIMAL; break;
            case "plant" : this.kingdom = Symbol.PLANT; break;
            case "insect" : this.kingdom = Symbol.INSECT; break;
        }

        this.imgBack = imgBack;
        this.imgFront = imgFront;
        this.pointsPerCondition = pointGiven;

        this.faceUpNW = FNW.equals("null") ? null : new Corner(FNW);
        this.faceUpNE = FNE.equals("null") ? null : new Corner(FNE);
        this.faceUpSW = FSW.equals("null") ? null : new Corner(FSW);
        this.faceUpSE = FSE.equals("null") ? null : new Corner(FSE);

        this.faceDownNW = BNW.equals("null") ? null : new Corner(BNW);
        this.faceDownNE = BNE.equals("null") ? null : new Corner(BNE);
        this.faceDownSW = BSW.equals("null") ? null : new Corner(BSW);
        this.faceDownSE = BSE.equals("null") ? null : new Corner(BSE);
    }

    /**
     * method used by the Player to play this PlayableCard
     * @param player the one who has the PlayableCard in Hand and wants to play it
     * @param coords the place they want to play it
     * @return the points scored by the Player (if any were scored)
     * @throws NoPossiblePlacement if the position chosen isn't valid
     */
    public int play(Player player, Coords coords) throws NoPossiblePlacement, NotPlaceableException
	{
        int pts = player.getGameField().tryPlaceCard(this, coords);
        player.getHand().removeCard(this);
        return pts;
    }

    /**
     * getter method for cardID
     * @return this card's ID
     */
    public int getCardID(){
        return this.cardID;
    }
}