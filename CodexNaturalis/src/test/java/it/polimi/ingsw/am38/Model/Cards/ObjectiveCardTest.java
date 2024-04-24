package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Enum.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardTest {

    @Test
    void objectiveCardConstructorDiagonalMission(){
        ObjectiveCard objectiveCard = new ObjectiveCard(1,"animal","diagonal",null,null, 2,"null","SE","null");

        assertEquals(1, objectiveCard.getCardID());
        assertEquals(Symbol.ANIMAL, objectiveCard.getKingdom());
        assertEquals("diagonal", objectiveCard.getObjType());
        assertEquals(2, objectiveCard.getPointsGiven());
        assertNull(objectiveCard.getKingdom2());
        assertEquals(objectiveCard.getPosition(),Orientation.SE);
        assertNull(objectiveCard.getItem());
    }
    @Test
    void objectiveCardConstructorShapeLMission(){
        ObjectiveCard objectiveCard = new ObjectiveCard(1,"fungi","shapeL",null,null, 2,"insect","SE","null");

        assertEquals(1, objectiveCard.getCardID());
        assertEquals(Symbol.FUNGI, objectiveCard.getKingdom());
        assertEquals("shapeL", objectiveCard.getObjType());
        assertEquals(2, objectiveCard.getPointsGiven());
        assertEquals(Symbol.INSECT, objectiveCard.getKingdom2());
        assertEquals(Orientation.SE, objectiveCard.getPosition());
        assertNull(objectiveCard.getItem());
    }
    @Test
    void objectiveCardConstructorDuoMission(){
        ObjectiveCard objectiveCard = new ObjectiveCard(1,"null","duo",null,null, 2,"null","null","manuscript");

        assertEquals(1, objectiveCard.getCardID());
        assertNull(objectiveCard.getKingdom());
        assertEquals("duo", objectiveCard.getObjType());
        assertEquals(2, objectiveCard.getPointsGiven());
        assertNull(objectiveCard.getKingdom2());
        assertNull(objectiveCard.getPosition());
        assertEquals(Symbol.MANUSCRIPT, objectiveCard.getItem());
    }
    @Test
    void objectiveCardConstructorTrioKingdomMission(){
        ObjectiveCard objectiveCard = new ObjectiveCard(1,"null","all",null,null, 2,"null","null","null");

        assertEquals(1, objectiveCard.getCardID());
        assertNull(objectiveCard.getKingdom());
        assertEquals("all", objectiveCard.getObjType());
        assertEquals(2, objectiveCard.getPointsGiven());
        assertNull(objectiveCard.getKingdom2());
        assertNull(objectiveCard.getPosition());
        assertNull(objectiveCard.getItem());
    }
    @Test
    void objectiveCardConstructorTrioObjectMission(){
        ObjectiveCard objectiveCard = new ObjectiveCard(1,"animal","shapeL",null,null, 2,"plant","NW","manuscript");

        assertEquals(1, objectiveCard.getCardID());
        assertEquals(Symbol.ANIMAL, objectiveCard.getKingdom());
        assertEquals("shapeL", objectiveCard.getObjType());
        assertEquals(2, objectiveCard.getPointsGiven());
        assertEquals(Symbol.PLANT, objectiveCard.getKingdom2());
        assertEquals(Orientation.NW, objectiveCard.getPosition());
        assertEquals(Symbol.MANUSCRIPT, objectiveCard.getItem());
    }

    ObjectiveCard objectiveCard = new ObjectiveCard(1,"animal","shapeL",null,null, 2,"plant","NW","manuscript");
    @Test
    void getObjType() {
        assertEquals("shapeL", objectiveCard.getObjType());
    }

    @Test
    void getPointsGiven() {
        assertEquals(2, objectiveCard.getPointsGiven());
    }

    @Test
    void setAndGetVisibility() {
        objectiveCard.setVisibility(true);
        assertTrue(objectiveCard.getVisibility());

        objectiveCard.setVisibility(false);
        assertFalse(objectiveCard.getVisibility());
    }


    @Test
    void getKingdom() {
        assertEquals(Symbol.ANIMAL, objectiveCard.getKingdom());
    }

    @Test
    void getKingdom2() {
        assertEquals(Symbol.PLANT, objectiveCard.getKingdom2());
    }

    @Test
    void getPosition() {
        assertEquals(Orientation.NW, objectiveCard.getPosition());
    }

    @Test
    void getItem() {
        assertEquals(Symbol.MANUSCRIPT, objectiveCard.getItem());
    }
}