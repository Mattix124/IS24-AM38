package Model.Cards.Decks.Miscellaneous.Board;

import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.Field;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

class FieldTest
{
	static Stream <Arguments> newCoords()
	{
		return Stream.of(
				/*giusti*/Arguments.arguments(new Coords(0, 1)), Arguments.arguments(new Coords(0, 2)), Arguments.arguments(new Coords(-1, 0)), Arguments.arguments(new Coords(-1, 1)));
	}

	private Field f;


	@ParameterizedTest
	@MethodSource("newCoords")
	void tryPlaceCardPlaceAndPointsGoldCard(Coords c)
	{
		Field        f = new Field(new StarterCard(0, "null", "33", "none", "plant", "insect", "animal", "fungi", "plant", "insect", "animal", "fungi", "fungi", "fungi"));
		ResourceCard g = new ResourceCard(41, "fungi", "333", "333", 1, "fungi", "fungi", "none", "none", "quill", "none", "none", "none");
		try
		{assert(1 == f.tryPlaceCard(g, c));
		}catch(NotPlaceableException e)
		{
			System.out.println(e.getMessage());
		}

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