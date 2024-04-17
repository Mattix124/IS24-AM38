package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NonPlaceableException;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
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


	/**
	 * This method calculate the "2 far" card for placement control.
	 *
	 * @param c The method requires a CardData. Then the param and the "2 far" cards field "twoDistanceCards" get updated.
	 */
	private void calculateTwoDistance(CardData c)
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

	/**
	 * This method is exposed to allow the placing (or not) of Resource card .
	 *
	 * @param card   This parameter contain a ResourceCard that will be placed if the coordinates chosen by the player are correct.
	 * @param coords This parameter contain the Coordinates chosen by the player to place the card
	 */
	public int tryPlaceCard(ResourceCard card, Coords coords)
	{
		int point = 0;
		if (possiblePlacement.contains(coords))
		{
			updateFieldElements(card, coords);
			if (card.getPointsWon() == 0)
				point++;
			addOrderedCard(new CardData(coords, card, new LinkedList <CardData>()), sortedVector);
			System.out.println("Card placed");
			return point;
		}
		else
		{
			System.out.println("You can't place here");
			return point;
		}
	}


	/**
	 * This method update the visibleElements class which contain the visible symbols on the player field.
	 *
	 * @param card   The card which will covers at least one corner so a possible element
	 * @param coords The coordinates that indicate where the card is inserted
	 */
	private void updateFieldElements(PlayableCard card, Coords coords)
	{


		for (CardData cd : sortedVector)
		{
			if (cd.coordinates().x() + 1 == coords.x() && cd.coordinates().y() == coords.y())
			{
				if (cd.card().getCorner(Orientation.NW) != null)
				{
					cd.card().getCorner(Orientation.NW).setOccupied(true);
					if (cd.card().getCorner(Orientation.NW).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(Orientation.NW).getSymbol(), -1);
				}

			}
			else if (cd.coordinates().x() - 1 == coords.x() && cd.coordinates().y() == coords.y())
			{
				if (cd.card().getCorner(Orientation.SW) != null)
				{
					cd.card().getCorner(Orientation.SW).setOccupied(true);
					if (cd.card().getCorner(Orientation.SW).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(Orientation.SW).getSymbol(), -1);
				}

			}
			else if (cd.coordinates().x() == coords.x() && cd.coordinates().y() + 1 == coords.y())
			{
				if (cd.card().getCorner(Orientation.NE) != null)
				{
					cd.card().getCorner(Orientation.NE).setOccupied(true);
					if (cd.card().getCorner(Orientation.NE).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(Orientation.NE).getSymbol(), -1);
				}
			}
			else if (cd.coordinates().x() == coords.x() && cd.coordinates().y() - 1 == coords.y())
			{
				if (cd.card().getCorner(Orientation.SE) != null)
				{
					cd.card().getCorner(Orientation.SE).setOccupied(true);
					if (cd.card().getCorner(Orientation.SE).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(Orientation.SE).getSymbol(), -1);
				}

			}


		}
		if (card.getFace())
		{
			for (Orientation o : Orientation.values())
			{
				if (card.getCorner(o) != null && card.getCorner(o).getSymbol() != Symbol.NULL)
					visibleElements.increaseSymbol(card.getCorner(o).getSymbol());
			}
		}
		else
			visibleElements.increaseSymbol(card.getKingdom());

	}

	/**
	 * This method is exposed to allow the placing (or not) of Gold card.
	 *
	 * @param card   This parameter contain a GoldCard that will be placed if the coordinates chosen by the player are correct.
	 * @param coords This parameter contain the Coordinates chosen by the player to place the card
	 */
	public int tryPlaceCard(GoldCard card, Coords coords)
	{
		if (possiblePlacement.contains(coords) && checkGoldCardPlacementCondition(card))
		{
			updateFieldElements(card, coords);
			int point = checkGoldCardPoints(card, coords);
			addOrderedCard(new CardData(coords, card, new LinkedList <CardData>()), sortedVector);
			System.out.println("Card placed");
			return point;
		}
		else
		{
			System.out.println("You can't place here");
			return -1;
		}
	}

	/**
	 * The method gives the amount of point for the placement of a GoldaCard in the given coordinates.
	 *
	 * @param card   The GoldCard that will be placed.
	 * @param coords The coordinates where the GoldCard will be placed.
	 * @return The amount of points.
	 */
	private int checkGoldCardPoints(GoldCard card, Coords coords)
	{
		int points = 0;
		//if ()
		return points;
	}

	/**
	 * This method tells if a GoldCard can be placed in the field using its requirements.
	 *
	 * @param card The GoldCard that (not) will be placed.
	 * @return outcome that determine if the card can be placed.
	 */
	private boolean checkGoldCardPlacementCondition(GoldCard card)
	{
		Kingdom[] cond  = card.getPlayableCondition().getGoldPlayableCondition();
		int       fungi = 0, animal = 0, plant = 0, insect = 0;
		for (int i = 0 ; i < 5 ; i++)
		{
			if (cond[i] != null)

				switch (cond[i])
				{
					case FUNGI -> fungi++;
					case ANIMAL -> animal++;
					case PLANT -> plant++;
					case INSECT -> insect++;
				}

		}
		return visibleElements.getKingdom(Kingdom.FUNGI) >= fungi && visibleElements.getKingdom(Kingdom.INSECT) >= insect && visibleElements.getKingdom(Kingdom.ANIMAL) >= animal && visibleElements.getKingdom(Kingdom.PLANT) >= plant;
	}

	/**
	 * This method is an "automated method" and at the end of a player's turn refresh the possiblePlacement list.
	 *
	 * @throws NonPlaceableException If there are no possible coordinates for placing ANY card, an exception is thrown and the player will get stuck
	 */
	private void checkPlacement() throws NonPlaceableException
	{
		LinkedHashSet <Coords> list = new LinkedHashSet <Coords>();

		for (CardData cd : sortedVector)
		{
			for (Orientation o : Orientation.values())

			{
				if (cd.card().getCorner(o) != null && !cd.card().getCorner(o).isOccupied() && !cd.card().getCorner(o).isChecked())
				{
					cd.card().getCorner(o).setChecked(true);
					Coords position;
					for (CardData cTD : cd.twoDistanceCards())
					{
						Pair <Integer, Integer> relativeDistance = distance(cd.coordinates(), cTD.coordinates(), false);

						if (o.equals(Orientation.SW) && relativeDistance.getKey() <= -1)
						{
							position = new Coords(cd.coordinates().x() - 1, cd.coordinates().y());
							switch (relativeDistance.getValue())
							{
								case -1:
								{
									if (!cTD.card().getCorner(Orientation.NW).isOccupied())
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.NE).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
										list.add(position);
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
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.NW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
										list.add(position);
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
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.SW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
										list.add(position);

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
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(Orientation.SW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(Orientation.SE).isOccupied())
										list.add(position);
									break;
								}
							}
						}
					}
				}
			}
		}


		possiblePlacement = list;
		setCheckedFalse();
		if (list.isEmpty())
			throw new NonPlaceableException("No possible placement, you're stuck");
	}

	/**
	 * This method is needed to set the checked attributes to false in every corner of every played cards.
	 * This help the optimization of the checkPlacement method.
	 */
	private void setCheckedFalse()
	{
		for (CardData cd : sortedVector)
		{
			for (Orientation o : Orientation.values())
			{
				if (cd.card().getCorner(o) != null)
					cd.card().getCorner(o).setChecked(false);
			}
		}


	}

	/**
	 * The method gives a Pair that contains the relative distance between two played cards.
	 *
	 * @param c2  One of the 2 cards needed.
	 * @param c1  One of the 2 cards needed.
	 * @param abs A boolean that allows the method to be more "fluid" and give the always positive distance for the method calculateTwoDistance, and, a "signed" distance for the checkPlacement method.
	 */
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

	/**
	 * The method will place the card only if the coordinates chosen by the player in the method tryPlaceCard are allowed, then place the card in an ordered array that contains all played cards.
	 *
	 * @param insertedCard Card chosen by the player
	 * @param v            This parameter is the vector where the card will be placed (sortedVector for the played card or the twoDistanceCard parameter of a card)
	 */
	private void addOrderedCard(CardData insertedCard, LinkedList <CardData> v)
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

	public int CheckObjectivePoints(ObjectiveCard obj)
	{
		int points = 0;
		/*switch (obj.getObjType())
		{
			case "diagonal":
			{
				break;
			}
			case "shapeL":
			{
				break;
			}
			case "trio":
			{
				break;
			}
			case "duo":
			{
				points = visibleElements.getSymbol(obj.);
				break;
			}
			case "all":
			{
				break;
			}
		}*/
		return points;
	}

	/**
	 * This method is used to remove a card from a "check condition vector" so it will be less heavy on calculate the point given by an objective
	 *
	 * @param vector The vector that will be affected
	 * @param c      The CardData that will be removed
	 */
	private void removeCard(LinkedList <CardData> vector, CardData c)
	{
		vector.remove(c);
	}


}
