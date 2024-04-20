package Model.Cards.Decks.Miscellaneous.Board;

import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest
{
	@BeforeAll
	static void setUp()
	{
		Field f = new Field(new StarterCard(0,null,null,"none","plant","insect","animal","fungi","plant","insect","animal","fungi","insect","animal"));
	}


	@Test
	void tryPlaceCard()
	{

	}

	@Test
	void testTryPlaceCard()
	{
	}

	@Test
	void checkObjectivePoints()
	{
	}
}