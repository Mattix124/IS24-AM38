package it.polimi.ingsw.am38.Model.Cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    GoldCard goldCard = new GoldCard(01,"animal",null,null,"corner",2,"animal","insect","plant",
            "fungi","null","quill","manuscript","inkwell","animal","animal","fungi","null","null");


    @Test
    void getCardID() {
        assertEquals(01, goldCard.getCardID());
    }
}