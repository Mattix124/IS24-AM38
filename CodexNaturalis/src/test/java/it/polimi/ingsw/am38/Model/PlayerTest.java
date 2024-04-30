package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.ColorTakenException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Model.Board.CardData;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static it.polimi.ingsw.am38.Enum.Color.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p;
    Game g;

    @BeforeEach
    void setUp(){
        p = new Player("Pierino");
        g = new Game(0, 3, p);
    }

    @Test
    void countObjectivePoints() {
        p.countObjectivePoints(6, 8, 3);

        assertEquals(p.getObjectivePoints(), 6 + 8 + 3);
    }

    @Test
    void drawPairObjectives() {
        LinkedList<ObjectiveCard> objList = new LinkedList<>();
        ObjectiveDeck OD = new ObjectiveDeck();
        objList.add(OD.getPool().getFirst());
        objList.add(OD.getPool().get(1));
        p.drawPairObjectives(OD);

        assertEquals(objList, p.getPair());
    }

    @Test
    void chooseObjectiveCard() throws InvalidInputException {
        ObjectiveDeck OD = new ObjectiveDeck();
        p.drawPairObjectives(OD);
        p.chooseObjectiveCard(1);
        assertEquals(p.getObjectiveCard(), p.getPair().getFirst());

        p.chooseObjectiveCard(2);
        assertEquals(p.getObjectiveCard(), p.getPair().get(1));

        assertThrows(InvalidInputException.class, ()->p.chooseObjectiveCard(7));
    }

    @Test
    void chooseStarterCardFace() {
        StarterDeck SD = new StarterDeck();
        p.setStarterCard(SD.drawStarterCard());
        p.chooseStarterCardFace(true);
        assertTrue(p.getStarterCard().getFace());

        p.chooseStarterCardFace(false);
        assertFalse(p.getStarterCard().getFace());
    }

    @Test
    void chooseColor() throws NumOfPlayersException, ColorTakenException {
        Player p2 = new Player("Tizio");
        Player p3 = new Player("Caio");
        Player p4 = new Player("Sempronio");
        g.addPlayer(p2);
        g.addPlayer(p3);
        p.chooseColor(RED);
        p2.chooseColor(YELLOW);

        assertEquals(p.getColor(), RED);
        assertThrows(ColorTakenException.class, ()->p3.chooseColor(RED));
        assertThrows(NumOfPlayersException.class, ()-> g.addPlayer(p4));
    }

    @Test
    void playACard(){
    }

    @Test
    void setFirstHand() {
        g.setGoldAndResourceDecks();
        ResourceCard r1 = g.getResourceDeck().getPool().getFirst();
        ResourceCard r2 = g.getResourceDeck().getPool().get(1);
        GoldCard g1 = g.getGoldDeck().getPool().getFirst();
        p.setFirstHand();

        assertEquals(p.getHand().getCard(0), g1);
        assertEquals(p.getHand().getCard(1), r1);
        assertEquals(p.getHand().getCard(2), r2);
    }

    @Test
    void getNickname() {
        assertEquals(p.getNickname(), "Pierino");
    }

    @Test
    void isPlaying() {

    }

    @Test
    void getGame() throws NumOfPlayersException {
        assertEquals(p.getGame(), g);
    }
}