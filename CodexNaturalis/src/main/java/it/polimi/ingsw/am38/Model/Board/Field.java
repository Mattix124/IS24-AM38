package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Exception.NonPlaceableException;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import javafx.util.Pair;


import java.util.*;


public class Field
{

	private final VisibleElements visibleElements = new VisibleElements(0, 0, 0, 0, 0, 0, 0);

	private final LinkedList <CardData> sortedVector = new LinkedList <>();

	private LinkedHashSet <Coords> possiblePlacement = new LinkedHashSet <>();

	public Field(CardData starter) //Field class take a starter card to initiate itself.
	{
		sortedVector.add(starter);
	}


	//Già Piazzata
	private void calculateTwoDistance(CardData c) //This method calculate the 2 far card for placement control.
	{
		for (CardData cards : sortedVector)
		{
			Pair <Integer, Integer> t;
			t = distance(c.coordinates(), cards.coordinates(), true);
			if (t.getKey() + t.getValue() == 2)
			{
				addOrderedCard(cards, c.twoDistanceCards());
				addOrderedCard(c, cards.twoDistanceCards());
			}

		}
	}

	public void tryPlaceCard(PlayableCard card, Coords coords)
	{
		if (possiblePlacement.contains(coords))
		{
			addOrderedCard(new CardData(coords, card, new LinkedList <CardData>()), sortedVector);
			System.out.println("Card placed");
		}
		else
		{
			System.out.println("You can't place here");
		}
	}

	private void checkPlacement() throws NonPlaceableException //the methods check all possible placement
	{
		LinkedHashSet <Coords> list = new LinkedHashSet <Coords>();

		for (CardData cd : sortedVector)
		{
			for (Orientation o : Orientation.values())
			{
				if (cd.card().getCorner(o) != null && !cd.card().getCorner(o).isOccupied())
				{
					Coords position;
					for (CardData cTD : cd.twoDistanceCards())
					{
						Pair <Integer, Integer> relativeDistance = distance(cd.coordinates(), cTD.coordinates(), false);

						if (o.equals(Orientation.SW) && relativeDistance.getKey() <= -1)
						{//ora non mi vengono in mente ottimizzazioni

							position = new Coords(cd.coordinates().x() - 1, cd.coordinates().y());
							switch (relativeDistance.getValue())
							{
								case -1:
								{
									if (!cTD.card().getCorner(Orientation.NW).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.NE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
							}
							continue;
						}
						if (o.equals(Orientation.SE) && relativeDistance.getValue() <= -1)
						{

							position = new Coords(cd.coordinates().x(), cd.coordinates().y() - 1);
							switch (relativeDistance.getKey())
							{
								case -1:
								{
									if (!cTD.card().getCorner(Orientation.NE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.NW).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
							}
							continue;
						}
						if (o.equals(Orientation.NW) && relativeDistance.getValue() >= 1)
						{

							position = new Coords(cd.coordinates().x(), cd.coordinates().y() + 1);
							switch (relativeDistance.getKey())
							{
								case -1:
								{
									if (!cTD.card().getCorner(Orientation.NE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.SW).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
							}
							continue;
						}
						if (o.equals(Orientation.NE) && relativeDistance.getKey() >= 1)
						{

							position = new Coords(cd.coordinates().x() + 1, cd.coordinates().y());
							switch (relativeDistance.getValue())
							{
								case -1:
								{
									if (!cTD.card().getCorner(Orientation.NW).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.SW).isOccupied())
									{
										list.add(position);
									}
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
									{
										list.add(position);
									}
									break;
								}
							}
						}
					}
				}

			}
		}
		possiblePlacement = list;
		if (list.isEmpty()) throw new NonPlaceableException("No possible placement, you're stuck");
	}

	private Pair <Integer, Integer> distance(Coords c2, Coords c1, boolean abs)
	{
		if (abs)
		{
			return new Pair <Integer, Integer>(Math.abs(c1.x() - c2.x()), Math.abs(c1.y() - c2.y()));
		}
		else
		{
			return new Pair <Integer, Integer>(c1.x() - c2.x(), c1.y() - c2.y());
		}
	}


	private void addOrderedCard(CardData insertedCard, LinkedList <CardData> v) //The new card is inserted in the ordered vector and will update all other cards twoDistance list
	{
		int      indexElement;
		CardData L = v.getLast();
		calculateTwoDistance(insertedCard);
		for (CardData c : v)
		{
			indexElement = v.indexOf(c);
			if (insertedCard.coordinates().x() < c.coordinates().x())
			{

				v.add(indexElement, insertedCard);
				break;
			}
			else
			{
				if (insertedCard.coordinates().x() == c.coordinates().x())
				{
					if (insertedCard.coordinates().y() <= c.coordinates().y())
					{
						v.add(indexElement, insertedCard);

					}
					else
					{
						if (indexElement == v.size())
						{
							v.add(insertedCard);
						}
						else
						{
							v.add(indexElement + 1, insertedCard);
						}
					}
					break;
				}
				else
				{
					if (L.equals(c))
					{
						v.add(insertedCard);
						break;
					}
				}
			}
		}


	}

	public void addCard(CardData c)
	{
		addOrderedCard(c, sortedVector);
	}

	private void removeCard(LinkedList <CardData> vector, CardData c)
	{
		vector.remove(c);
	}


}
