package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    StarterDeck sd = new StarterDeck();
    GoldDeck gd = new GoldDeck();
    ResourceDeck rd = new ResourceDeck();
    Field f = new Field(sd.getPool().get(0));

    @Test
    void tryPlaceCard() {
    }

    @Test
    void testTryPlaceCard() {
    }

    @Test
    void checkObjectivePoints() {
    }

    @Test
    void getCardFromCoordinate() {
        Coords coords = new Coords(1,1);
       // f.addOrderedCard(new CardData(coords, rd.getPool().get(0)), f.getSortedVector());
        assertEquals(f.getCardFromCoordinate(coords).card(), rd.getPool().get(0));
    }

    @Test
    void getVisibleElements() {
        f.getSortedVector().get(0).card().setFace(false);

        assertEquals(1, f.getVisibleElements().getSymbol(Symbol.ANIMAL));
        assertEquals(1, f.getVisibleElements().getSymbol(Symbol.FUNGI));
        assertEquals(1, f.getVisibleElements().getSymbol(Symbol.INSECT));
        assertEquals(1, f.getVisibleElements().getSymbol(Symbol.PLANT));
        assertEquals(0, f.getVisibleElements().getSymbol(Symbol.QUILL));
        assertEquals(0, f.getVisibleElements().getSymbol(Symbol.INKWELL));
        assertEquals(0, f.getVisibleElements().getSymbol(Symbol.MANUSCRIPT));
    }


    @Test
    void getPossiblePlacement() {
    }
}