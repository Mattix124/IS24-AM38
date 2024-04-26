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
        GoldCard gc1 = new GoldCard(44, "fungi", "images/front/44-front.svgz", "images/back/44-back.svgz", "corner", 2,
                "none", "none", "null", "none",
                "none", "none", "none", "none",
                "null", "null", "null", "null", "null");
        GoldCard gc2 = new GoldCard(42, "fungi", "images/front/42-front.svgz", "images/back/42-back.svgz", "inkwell", 1,
                "none", "inkwell", "null", "none",
                "none", "none", "none", "none",
                "null", "null", "null", "null", "null");
        GoldCard gc3 = new GoldCard(0, "fungi", "images/front/42-front.svgz", "images/back/42-back.svgz", "manuscript", 1,
                "manuscript", "none", "none", "null",
                "none", "none", "none", "none",
                "null", "null", "null", "null", "null");
        GoldCard gc4 = new GoldCard(0, "fungi", "img", "img", "quill", 1,
                "null", "none", "none", "quill",
                "none", "none", "none", "none",
                "null", "null", "null", "null", "null");
        GoldCard gc5 = new GoldCard(0, "fungi", "img", "img", "null", 3,
                "none", "null", "inkwell", "null",
                "none", "none", "none", "none",
                "null", "null", "null", "null", "null");
        ResourceCard rc3 = new ResourceCard(107,"insect","images/front/3-front.svgz", "images/front/3-back.svgz",1,
                "plant", "none", "null", "none",
                "none","none","none","none");


        fakeSC1.setFace(true);
        Field f = new Field(fakeSC1);
        Hand h = new Hand();

        h.addCard(rc1); // add card to the hand
        rc1.setFace(false); // set faces
        Coords c = new Coords(0,1);
        int p1 = f.tryPlaceCard((ResourceCard) h.getCard(0), c); // place rc1 in (0,1)
        h.removeCard(rc1);

        h.addCard(rc2);
        rc2.setFace(false); // set faces
        c = new Coords(1,0);
        int p2 = f.tryPlaceCard((ResourceCard) h.getCard(0), c); // place rc2 in (1,0)
        h.removeCard(rc2);

        h.addCard(gc1);
        gc1.setFace(true); // set faces
        c = new Coords(1,1);
        int p3 = f.tryPlaceCard((GoldCard) h.getCard(0), c); // place gc1 in (1,1)
        h.removeCard(gc1);

        h.addCard(gc2);
        gc2.setFace(true);
        c = new Coords(2,1);
        int p4 = f.tryPlaceCard((GoldCard) h.getCard(0), c);
        h.removeCard(gc2);

        h.addCard(gc3);
        gc3.setFace(true);
        c = new Coords(2,2);
        int p5 = f.tryPlaceCard((GoldCard) h.getCard(0), c);
        h.removeCard(gc3);

        h.addCard(gc4);
        gc4.setFace(true);
        c = new Coords(3,1);
        int p6 = f.tryPlaceCard((GoldCard) h.getCard(0), c);
        h.removeCard(gc4);

        h.addCard(gc5);
        gc5.setFace(true);
        c = new Coords(2, 0);
        int p7 = f.tryPlaceCard((GoldCard) h.getCard(0), c);
        h.removeCard(gc5);

        h.addCard(rc3);
        rc3.setFace(true); // set faces
        c = new Coords(-1,0);
        int p8 = f.tryPlaceCard((ResourceCard) h.getCard(0), c); // place rc2 in (1,0)
        h.removeCard(rc3);

        assertEquals(0, p1); // rc1 is placed face down, so no points expected
        assertEquals(0, p2); // rc2 is placed face down, so no points expected
        assertEquals(4, p3); // gc1 covers 2 corners, so 2*2=4
        assertEquals(3, p4); // gc2 gives 3 inkwells (2 from starter card + his)
        assertEquals(1, p5); // gc3 gives 1 manuscript (only his)
        assertEquals(1, p6); // gc4 gives 1 quill (only his)
        assertEquals(3, p7); // gc5 gives 3 points when placed
        assertEquals(1, p8); // rc3 gives 1 point when placedgi
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
        assertEquals(p, h.getCard(0).getPointsPerCondition()); //check if return what's expected

        LinkedList <Coords> pp2 = f.getPossiblePlacement();
        assertNotEquals(pp1,pp2); //ensure that the place where to put a card after have played the previous one are changed

        gc.setFace(false);
        int p1 = f.tryPlaceCard((GoldCard) h.getCard(2), pp2.getFirst()); // place gc face down
        int animal3 = f.getVisibleElements().getSymbol(Symbol.ANIMAL);
        int plant3 = f.getVisibleElements().getSymbol(Symbol.PLANT);
        int insect3 = f.getVisibleElements().getSymbol(Symbol.INSECT);
        int fungi3 = f.getVisibleElements().getSymbol(Symbol.FUNGI); //check the visible elements are updated
        assertNotEquals(animal3, animal2);
        assertEquals(plant3, plant2);
        assertEquals(insect3, insect2);
        assertNotEquals(fungi3, fungi2);
        assertEquals(0, p1); //check if return what's expected

        LinkedList <Coords> pp3 = f.getPossiblePlacement();
        assertNotEquals(pp3,pp2); //ensure that the place where to put a card after have played the previous one are changed
    }
}