package it.polimi.ingsw.am38.DataStructure;

import it.polimi.ingsw.am38.Cards.*;
import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Orientation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class Field
{

	private final VisibleElements visibleElements = new VisibleElements(0, 0, 0, 0, 0, 0, 0);

	private final LinkedList <CardData> cardVector = new LinkedList <>();
	private final LinkedList <CardData> sortedVector = new LinkedList <>();

	public Field(StarterCard starter)
	{
		cardVector.add(new CardData(new Coords(0, 0), starter));
		sortedVector.add(new CardData(new Coords(0, 0), starter));
	}

	private LinkedList <Coords> playable(PlayableCard card) throws NonPlaceableException
	{
		LinkedList <Coords> possiblePlacement = new LinkedList <Coords>();
		//if condizioni <= visibleElements


		//&& effettivamente piazzabile
		for (CardData c : cardVector)
		{
			for (int i = Orientation.SE.ordinal() ; i < 4 ; i++)
			{
				//if(!c.card().getCorner(i).equals(Null) && c.card.gerCorner.isOccupied == false)
				/*
				{

				}

*/
			}
		}
		if (possiblePlacement.isEmpty()) throw new NonPlaceableException("You can't place this card!"); //Stuck Player
		return possiblePlacement;
	}


	public boolean TryPlace(PlayableCard card, Coords coords)
	{
		return true;
	}

	private void addCard(CardData insertedCard)
	{
		cardVector.add(insertedCard);
		insertCard(insertedCard);
	}

	private void insertCard(CardData insertedCard)
	{
		int      indexElement;
		CardData L = sortedVector.getLast();
		for (CardData c : sortedVector)
		{
			indexElement = sortedVector.indexOf(c);
			if (insertedCard.coordinates().x() < c.coordinates().x())
			{

				sortedVector.add(indexElement, insertedCard);
				break;
			}
			else
			{
				if (insertedCard.coordinates().x() == c.coordinates().x())
				{
					if (insertedCard.coordinates().y() <= c.coordinates().y())
					{
						sortedVector.add(indexElement, insertedCard);

					}
					else
					{
						if (indexElement == sortedVector.size())
						{
							sortedVector.add(insertedCard);
						}
						else
						{
							sortedVector.add(indexElement + 1, insertedCard);
						}
					}
					break;
				}
				else
				{
					if (L.equals(c))
					{
						sortedVector.add(insertedCard);
						break;
					}
				}
			}
		}
	}


	private void removeCard(LinkedList <CardData> vector, CardData c)
	{
		vector.remove(c);
	}


}
