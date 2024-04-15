package it.polimi.ingsw.am38.DataStructure;

import it.polimi.ingsw.am38.Cards.PlayableCard;

public class CardData //Class that is needed for the DataStructure array.
{
	private final Coords Coordinates;

	private final PlayableCard Card;


	public CardData(Coords coordinates, PlayableCard card)
	{
		Coordinates = coordinates;
		Card = card;
	}

	public Coords getCoordinates()
	{
		return Coordinates;
	}

	public PlayableCard getCard()
	{
		return Card;
	}
}
