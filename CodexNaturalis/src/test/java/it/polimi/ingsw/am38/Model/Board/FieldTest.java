package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Hand;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    StarterCard sc = new StarterCard(1,"images/front/1-front.svgz", "images/front/1-back.svgz","null", "null", "fungi", "animal","animal","fungi","insect","plant","animal","fungi","null" );
    ResourceCard rc1 = new ResourceCard(2,"insect","images/front/2-front.svgz", "images/front/2-back.svgz",1,"null", "animal", "fungi", "none","none","none","none","none");
    ResourceCard rc2 = new ResourceCard(3,"insect","images/front/3-front.svgz", "images/front/3-back.svgz",2,"plant", "none", "null", "none","none","none","none","none");
    GoldCard gc = new GoldCard(4,"animal","images/front/4-front.svgz", "images/front/4-back.svgz","corner",3,"plant", "null", "fungi", "animal","none","none","none","none","animal","fungi","fungi", "null", "null" );

    @Test
    void checkCorrectPointsFromCards() throws NotPlaceableException {
        StarterCard fakeSC1 = new StarterCard(5, "images/front/5-front.svgz", "images/back/5-back.svgz",
                "inkwell", "inkwell", "inkwell", "inkwell",
                "quill", "quill", "quill", "quill",
                "animal", "fungi", "fungi");
        GoldCard gc1 = new GoldCard(44, "fungi", "img", "img", "corner", 2,
                "none", "none", "null", "none",
                "none", "none", "none", "none",
                "fungi", "fungi", "fungi", "animal", "null");
        GoldCard gc2 = new GoldCard(44, "fungi", "img", "img", "corner", 2,
                "none", "none", "null", "none",
                "none", "none", "none", "none",
                "fungi", "fungi", "fungi", "animal", "null");

        fakeSC1.setFace(true);
        Field f = new Field(fakeSC1);
        Hand h = new Hand();
        h.addCard(rc1); // add card to the hand
        h.addCard(rc2); // add card to the hand
        h.addCard(gc);  // add card to the hand
        gc.setFace(true);   // set faces
        rc1.setFace(false); // set faces
        rc2.setFace(false); // set faces
        Coords c = new Coords(0,1);
        int p1 = f.tryPlaceCard((ResourceCard) h.getCard(0), c); // place rc1 in (0,1)
        c = new Coords(1,0);
        int p2 = f.tryPlaceCard((ResourceCard) h.getCard(1), c); // place rc2 in (1,0)
        c = new Coords(1,1);
        int p3 = f.tryPlaceCard((GoldCard) h.getCard(2), c); // place gc in (1,1)

        assertEquals(0, p1); // rc1 is placed face down, so no points expected
        assertEquals(0, p2); // rc2 is placed face down, so no points expected
        assertEquals(6, p3); // gc covers 2 corners, so 3*2=6
    }

    @Test
    void placeCardsAndUpdateFieldAttributes() throws NotPlaceableException {
        sc.setFace(true);
        Field f = new Field(sc); //create the field

        Hand h = new Hand();
        h.addCard(rc1);
        h.addCard(rc2);
        h.addCard(gc); //till here create the hand
        LinkedList <Coords> pp1 = f.getPossiblePlacement();
        int animal1 = f.getVisibleElements().getSymbol(Symbol.ANIMAL);
        int plant1 = f.getVisibleElements().getSymbol(Symbol.PLANT);
        int insect1 = f.getVisibleElements().getSymbol(Symbol.INSECT);
        int fungi1 = f.getVisibleElements().getSymbol(Symbol.FUNGI);

        assertNotNull(pp1); //check if checkPlacement works

        rc1.setFace(true);
        int p = f.tryPlaceCard((ResourceCard) h.getCard(0), pp1.getFirst()); //place card
        int animal2 = f.getVisibleElements().getSymbol(Symbol.ANIMAL);
        int plant2 = f.getVisibleElements().getSymbol(Symbol.PLANT);
        int insect2 = f.getVisibleElements().getSymbol(Symbol.INSECT);
        int fungi2 = f.getVisibleElements().getSymbol(Symbol.FUNGI); //check the visible elements are updated
        assertNotEquals(animal1, animal2);
        assertEquals(plant1, plant2);
        assertEquals(insect1, insect2);
        assertEquals(fungi1, fungi2);
        assertEquals(p, h.getCard(0).getPointsWon()); //check if return what's expected

        LinkedList <Coords> pp2 = f.getPossiblePlacement();
        assertNotEquals(pp1,pp2); //ensure that the place where to put a card after have played the previous one are changed

        gc.setFace(false);
        int p1 = f.tryPlaceCard((GoldCard) h.getCard(2), pp2.getFirst()); //place card
        int animal3 = f.getVisibleElements().getSymbol(Symbol.ANIMAL);
        int plant3 = f.getVisibleElements().getSymbol(Symbol.PLANT);
        int insect3 = f.getVisibleElements().getSymbol(Symbol.INSECT);
        int fungi3 = f.getVisibleElements().getSymbol(Symbol.FUNGI); //check the visible elements are updated
        assertNotEquals(animal3, animal2);
        assertEquals(plant3, plant2);
        assertEquals(insect3, insect2);
        assertNotEquals(fungi3, fungi2);
        assertEquals(p1, h.getCard(2).getPointsWon()); //check if return what's expected

        LinkedList <Coords> pp3 = f.getPossiblePlacement();
        assertNotEquals(pp3,pp2); //ensure that the place where to put a card after have played the previous one are changed
    }
}