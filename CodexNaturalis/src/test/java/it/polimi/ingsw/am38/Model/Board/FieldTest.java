package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest
{
	private Field f;
	private ResourceCard rC;
	private GoldCard gC;

	@BeforeEach
	void setup()
	{
		StarterCard s = new StarterCard(81, "null", "null", "none", "plant", "insect", "none", "fungi", "plant", "insect", "animal", "insect", "null", "null");
		s.setFace(false);
		f = new Field(s);
		rC = new ResourceCard(01, "animal", "null", "null", 0, "fungi", "none", "fungi", "null", "none", "none", "none", "none");
		gC = new GoldCard(01, "animal", null, null, "corner", 2, "animal", "insect", "plant", "fungi", "null", "quill", "manuscript", "inkwell", "animal", "animal", "fungi", "null", "null");

	}


	static private Stream <Coords> coordsMaker()
	{
		return Stream.of( new Coords(-1, 0), new Coords(1, 0));
	}

	@ParameterizedTest
	@MethodSource("coordsMaker")
	void tryPlaceCardAtVariousCorrectCoordinateTest(Coords c)
	{
		try
		{
			f.tryPlaceCard(rC,c);
		}
		catch (NotPlaceableException e)
		{
			throw new RuntimeException(e);
		}
		assertEquals(f.getCardFromCoordinate(c).card(),rC);
	}

	@Test
	void testTryPlaceCard()
	{
	}

	@Test
	void checkObjectivePoints()
	{
	}

	@Test
	void getCardFromCoordinatesTest()
	{

		try
		{
			f.tryPlaceCard(rC, new Coords(-1, 0));
		}
		catch (NotPlaceableException e)
		{
			System.err.println(e.getMessage());
		}
		assertEquals(f.getCardFromCoordinate(new Coords(-1, 0)).card(), rC);
	}
}