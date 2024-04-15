package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

import java.util.Comparator;

public record CardData(Coords coordinates, PlayableCard card) implements Comparable <CardData>   //Class that is needed for the DataStructure array.
{
	@Override
	public int compareTo(CardData o)
	{
		return Comparator.comparing((CardData c) -> c.coordinates.x()).thenComparing(c -> c.coordinates.y()).compare(this, o); //Sorter method (Inefficient) Insertion sort is better (maybe later)

	}

}
