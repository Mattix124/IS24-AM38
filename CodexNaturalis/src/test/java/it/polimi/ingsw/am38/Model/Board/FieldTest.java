package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
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
    GoldCard gc6 = new GoldCard(9, "plant", "img", "img", "null", 1,
            "none", "manuscript", "null", "none",
            "none", "none", "none", "none",
            "null", "null", "null", "null", "null");
    ResourceCard rc4 = new ResourceCard(107,"animal","images/front/3-front.svgz", "images/front/3-back.svgz",0,
            "null", "none", "animal", "animal",
            "none","none","none","none");

    @Test
    void checkCorrectPointsFromCards() throws NotPlaceableException {
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

        h.addCard(gc6);
        gc6.setFace(true);
        c = new Coords(2,3);
        int p9 = f.tryPlaceCard((GoldCard) h.getCard(0), c);
        h.removeCard(gc6);

        assertEquals(0, p1); // rc1 is placed face down, so no points expected
        assertEquals(0, p2); // rc2 is placed face down, so no points expected
        assertEquals(4, p3); // gc1 covers 2 corners, so 2*2=4
        assertEquals(3, p4); // gc2 gives 3 inkwells (2 from starter card + his)
        assertEquals(1, p5); // gc3 gives 1 manuscript (only his)
        assertEquals(1, p6); // gc4 gives 1 quill (only his)
        assertEquals(3, p7); // gc5 gives 3 points when placed (no condition)
        assertEquals(1, p8); // rc3 gives 1 point when placed (no condition)
        assertEquals(1, p9); // gc6 gives 1 point (covers 1 manuscript and adds 1)
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
    
    @Test
    void checkCorrectPointsFromDiagonalObjectives() throws NotPlaceableException {
        ObjectiveCard fungiDiagCard = new ObjectiveCard(87, "fungi", "diagonal", "img", "img", 2,
                "null", "NE", "null");
        ObjectiveCard plantDiagCard = new ObjectiveCard(88, "plant", "diagonal", "img", "img", 2,
                "null", "NW", "null");
        ObjectiveCard animalDiagCard = new ObjectiveCard(89, "animal", "diagonal", "img", "img", 2,
                "null", "NE", "null");
        ObjectiveCard insectDiagCard = new ObjectiveCard(90, "insect", "diagonal", "img", "img", 2,
                "null", "NW", "null");
        fakeSC1.setFace(true);
        Field f = new Field(fakeSC1);
        Hand h = new Hand();
        int ignored = 0;

        // some random fungi cards
        ResourceCard f1 = new ResourceCard(1, "fungi", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard f2 = new ResourceCard(1, "fungi", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard f3 = new ResourceCard(1, "fungi", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard f4 = new ResourceCard(1, "fungi", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");

        // some random animal cards
        ResourceCard a1 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard a2 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard a3 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard a4 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard a5 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard a6 = new ResourceCard(1, "animal", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        

        // some random plant cards
        ResourceCard p1 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard p2 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard p3 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard p4 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard p5 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard p6 = new ResourceCard(1, "plant", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");

        // some random insect cards
        ResourceCard i1 = new ResourceCard(1, "insect", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard i2 = new ResourceCard(1, "insect", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard i3 = new ResourceCard(1, "insect", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");
        ResourceCard i4 = new ResourceCard(1, "insect", "img", "img", 0,
                "none", "none", "none", "none",
                "none", "none", "none", "none");

        h.addCard(f1); // add card to the hand
        f1.setFace(false); // set faces
        Coords c = new Coords(1,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(f1);

        h.addCard(f2); // add card to the hand
        f2.setFace(false); // set faces
        c = new Coords(2,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(f2);

        h.addCard(f3); // add card to the hand
        f3.setFace(false); // set faces
        c = new Coords(3,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(f3);

        h.addCard(f4); // add card to the hand
        f4.setFace(false); // set faces
        c = new Coords(4,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(f4);

        h.addCard(a1); // add card to the hand
        a1.setFace(false); // set faces
        c = new Coords(-1,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a1);

        h.addCard(a2); // add card to the hand
        a2.setFace(false); // set faces
        c = new Coords(-2,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a2);

        h.addCard(a3); // add card to the hand
        a3.setFace(false); // set faces
        c = new Coords(-3,0);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a3);

        h.addCard(a4); // add card to the hand
        a4.setFace(false); // set faces
        c = new Coords(-1,1);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a4);

        h.addCard(a5); // add card to the hand
        a5.setFace(false); // set faces
        c = new Coords(-2,1);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a5);

        h.addCard(a6); // add card to the hand
        a6.setFace(false); // set faces
        c = new Coords(-3,1);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(a6);

        h.addCard(i1); // add card to the hand
        i1.setFace(false); // set faces
        c = new Coords(-3,-1);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(i1);

        h.addCard(i2);
        i2.setFace(false);
        c = new Coords(-3,-2);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(i2);

        h.addCard(i3);
        i3.setFace(false);
        c = new Coords(-3,-3);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(i3);

        h.addCard(i4);
        i4.setFace(false);
        c = new Coords(-3,-4);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(i4);

        h.addCard(p1);
        p1.setFace(false);
        c = new Coords(3,1);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p1);

        h.addCard(p2);
        p2.setFace(false);
        c = new Coords(3,2);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p2);

        h.addCard(p3);
        p3.setFace(false);
        c = new Coords(3,3);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p3);

        h.addCard(p4);
        p4.setFace(false);
        c = new Coords(3,4);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p4);

        h.addCard(p5);
        p5.setFace(false);
        c = new Coords(3,5);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p5);

        h.addCard(p6);
        p6.setFace(false);
        c = new Coords(3,6);
        ignored = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(p6);
        /*
        h.addCard(rc1); // add card to the hand
        rc1.setFace(false); // set faces
        Coords c = new Coords(0,1);
        int p1 = f.tryPlaceCard((ResourceCard) h.getCard(0), c); // place rc1 in (0,1)
        h.removeCard(rc1);

        h.addCard(gc6);
        gc6.setFace(false); // set faces
        c = new Coords(1,0);
        int p2 = f.tryPlaceCard((GoldCard) h.getCard(0), c); // place gc6 in (1,0)
        h.removeCard(gc6);

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

        h.addCard(rc4);
        rc4.setFace(false);
        c = new Coords(0,2);
        int p10 = f.tryPlaceCard((ResourceCard) h.getCard(0), c);
        h.removeCard(rc4);
        */

        int op1 = f.CheckObjectivePoints(fungiDiagCard);
        assertEquals(2, op1);

        int op2 = f.CheckObjectivePoints(plantDiagCard);
        assertEquals(0, op2);

        int op3 = f.CheckObjectivePoints(animalDiagCard);
        assertEquals(4, op3);

        int op4 = f.CheckObjectivePoints(insectDiagCard);
        assertEquals(2, op4);
    }
}