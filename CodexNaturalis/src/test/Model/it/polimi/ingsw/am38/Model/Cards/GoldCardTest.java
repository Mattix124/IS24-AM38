package it.polimi.ingsw.am38.Model.Cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    @Test
    void constructorTest(){
        GoldCard goldCard = new GoldCard(1, "animal",null,null,"corner",2,"fungi","fungi","fungi","fungi","fungi","fungi","fungi","fungi",
                            "plant","plant","plant","plant","plant");

        assertNull(goldCard.imgBack);
        assertNull(goldCard.imgFront);
        //assertEquals(1, goldCard.getCardID());
    }
}