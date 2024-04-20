package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Model.Cards.*;
import javafx.util.Pair;


import java.util.*;

import static it.polimi.ingsw.am38.Enum.Orientation.*;

/**
 * This class contains the board data of each player. The class allows to place cards, to calculate points, and manage to calculate the points of a placed card.
 */
public class Field
{
	/**
	 * visibleElements is the attribute that store the visible symbols of the board. Used to calculate points and checking the goldCard placement.
	 */
	private final VisibleElements visibleElements = new VisibleElements(0, 0, 0, 0, 0, 0, 0);
	/**
	 * sortedVector is the attribute that stores all the cards played by the player. Almost all the method use this attribute to solve their mansion.
	 */
	private final LinkedList <CardData> sortedVector = new LinkedList <>();
	/**
	 * possiblePlacement is an attributes that stores all the possible placement for a card.
	 */
	private LinkedHashSet <Coords> possiblePlacement = new LinkedHashSet <>();

	private int order;

	/**
	 * This is the constructor method that initializes the Field class. It needs a starting card and based on its parameters update the visibleElements data.
	 *
	 * @param starter The starting card needed to start a match.
	 */
	public Field(StarterCard starter) //Field class take a starter card to initiate itself.
	{
		this.order = 0;
		CardData c = new CardData(new Coords(0, 0), starter, new LinkedList <CardData>());
		starter.setOrder(order);
		sortedVector.add(c);
		Symbol[] sArr = starter.getCentralKingdom();
		if (starter.getFace())
			for (Symbol s : sArr)
			{
				visibleElements.increaseSymbol(s);
			}

		for (Orientation o : Orientation.values())
		{
			if (starter.getCorner(o) != null && !starter.getCorner(o).getSymbol().equals(Symbol.NULL))
			{
				visibleElements.increaseSymbol(starter.getCorner(o).getSymbol());
			}
		}
		try
		{
			checkPlacement();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
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
	public int tryPlaceCard(ResourceCard card, Coords coords) throws NotPlaceableException
	{
		int point = 0;
		if (possiblePlacement.contains(coords))
		{
			card.setOrder(order);
			updateFieldElements(card, coords);
			if (card.getPointsWon() == 0)
				point++;
			addOrderedCard(new CardData(coords, card, new LinkedList <CardData>()), sortedVector);
			System.out.println("Card placed");
			order++;
			checkPlacement();


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
				if (cd.card().getCorner(NW) != null)
				{
					cd.card().getCorner(NW).setOccupied(true);
					if (cd.card().getCorner(NW).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(NW).getSymbol(), -1);
				}
			}
			else if (cd.coordinates().x() - 1 == coords.x() && cd.coordinates().y() == coords.y())
			{
				if (cd.card().getCorner(SW) != null)
				{
					cd.card().getCorner(SW).setOccupied(true);
					if (cd.card().getCorner(SW).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(SW).getSymbol(), -1);
				}
			}
			else if (cd.coordinates().x() == coords.x() && cd.coordinates().y() + 1 == coords.y())
			{
				if (cd.card().getCorner(NE) != null)
				{
					cd.card().getCorner(NE).setOccupied(true);
					if (cd.card().getCorner(NE).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(NE).getSymbol(), -1);
				}
			}
			else if (cd.coordinates().x() == coords.x() && cd.coordinates().y() - 1 == coords.y())
			{
				if (cd.card().getCorner(SE) != null)
				{
					cd.card().getCorner(SE).setOccupied(true);
					if (cd.card().getCorner(SE).getSymbol() != Symbol.NULL)
						visibleElements.increaseSymbol(cd.card().getCorner(SE).getSymbol(), -1);
				}
			}
		}
		if (card.getFace())
		{
			for (Orientation o : values())
				if (card.getCorner(o) != null && card.getCorner(o).getSymbol() != Symbol.NULL)
					visibleElements.increaseSymbol(card.getCorner(o).getSymbol());
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
	public int tryPlaceCard(GoldCard card, Coords coords) throws NotPlaceableException
	{
		if (possiblePlacement.contains(coords) && checkGoldCardPlacementCondition(card))
		{
			card.setOrder(order);
			order++;
			updateFieldElements(card, coords);
			int point = checkGoldCardPoints(card, coords);
			addOrderedCard(new CardData(coords, card, new LinkedList <CardData>()), sortedVector);
			System.out.println("Card placed");
			checkPlacement();
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
		int points             = 0;
		int pointsPerCondition = card.getPointsWon();
		if (card.getGoldPointsCondition() != null)
			switch (card.getGoldPointsCondition())
			{
				case QUILL ->
				{
					points = visibleElements.getSymbol(Symbol.QUILL) * pointsPerCondition;
				}
				case INKWELL ->
				{
					points = visibleElements.getSymbol(Symbol.INKWELL) * pointsPerCondition;
				}
				case MANUSCRIPT ->
				{
					points = visibleElements.getSymbol(Symbol.MANUSCRIPT) * pointsPerCondition;
				}
				case CORNER ->
				{
					Coords   aroundCoords = new Coords(coords.x() + 1, coords.y());
					CardData aroundCard   = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)

						if (aroundCard.card().getCorner(Orientation.SW) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x() - 1);
					aroundCoords.setX(coords.y());
					aroundCard = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)

						if (aroundCard.card().getCorner(Orientation.NE) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x());
					aroundCoords.setX(coords.y() - 1);
					aroundCard = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)

						if (aroundCard.card().getCorner(Orientation.NW) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x());
					aroundCoords.setX(coords.y() + 1);
					aroundCard = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)
						if (aroundCard.card().getCorner(Orientation.SE) != null)
							points += pointsPerCondition;
				}
			}
		return points;
	}

	/**
	 * This private method help checkGoldCardPoints and CheckObjectivePoints to have given a card that has the given coordinates.
	 *
	 * @param coords The coordinate that identify a card location.
	 * @param vector The collection where to find the card.
	 * @return null if there is no card in the coordinates given, otherwise, the card found.
	 */
	private CardData coordsFinder(Coords coords, LinkedList <CardData> vector)
	{
		for (CardData cd : vector)
			if (cd.coordinates().equals(coords))
				return cd;

		return null;
	}

	/**
	 * This method tells if a GoldCard can be placed in the field using its requirements.
	 *
	 * @param card The GoldCard that (not) will be placed.
	 * @return outcome that determine if the card can be placed.
	 */
	private boolean checkGoldCardPlacementCondition(GoldCard card)
	{
		Symbol[] cond  = card.getGoldPlayableCondition();
		int      fungi = 0, animal = 0, plant = 0, insect = 0;
		for (int i = 0 ; i < 5 ; i++)
			if (cond[i] != null)
				switch (cond[i])
				{
					case FUNGI -> fungi++;
					case ANIMAL -> animal++;
					case PLANT -> plant++;
					case INSECT -> insect++;
				}

		return (visibleElements.getKingdom(Symbol.FUNGI) >= fungi && visibleElements.getKingdom(Symbol.INSECT) >= insect && visibleElements.getKingdom(Symbol.ANIMAL) >= animal && visibleElements.getKingdom(Symbol.PLANT) >= plant);
	}

	/**
	 * This method is an "automated method" and at the end of a player's turn refresh the possiblePlacement list.
	 *
	 * @throws NotPlaceableException If there are no possible coordinates for placing ANY card, an exception is thrown and the player will get stuck
	 */
	private void checkPlacement() throws NotPlaceableException
	{
		LinkedHashSet <Coords> list = new LinkedHashSet <Coords>();

		for (CardData cd : sortedVector)
			for (Orientation o : Orientation.values())
				if (cd.card().getCorner(o) != null && !cd.card().getCorner(o).isOccupied() && !cd.card().getCorner(o).isChecked())
				{
					cd.card().getCorner(o).setChecked(true);
					Coords position;
					for (CardData cTD : cd.twoDistanceCards())
					{
						Pair <Integer, Integer> relativeDistance = distance(cd.coordinates(), cTD.coordinates(), false);
						position = new Coords(cd.coordinates().x() - 1, cd.coordinates().y());
						if (o.equals(SW) && relativeDistance.getKey() <= -1)
						{
							switch (relativeDistance.getValue())
							{
								case -1:
								{
									if (!cTD.card().getCorner(NW).isOccupied())
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(NE).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(SE).isOccupied())
										list.add(position);
									break;
								}
							}
							continue;
						}
						if (o.equals(SE) && relativeDistance.getValue() <= -1)
						{
							position.setX(cd.coordinates().x());
							position.setY(cd.coordinates().y() - 1);
							switch (relativeDistance.getKey())
							{
								case -1:
								{
									if (!cTD.card().getCorner(NE).isOccupied())
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(NW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(SE).isOccupied())
										list.add(position);
									break;
								}
							}
							continue;
						}
						if (o.equals(NW) && relativeDistance.getValue() >= 1)
						{
							position.setX(cd.coordinates().x());
							position.setY(cd.coordinates().y() + 1);
							switch (relativeDistance.getKey())
							{
								case -1:
								{
									if (!cTD.card().getCorner(NE).isOccupied())
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(SW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(SE).isOccupied())
										list.add(position);
									break;
								}
							}
							continue;
						}
						if (o.equals(NE) && relativeDistance.getKey() >= 1)
						{
							position.setX(cd.coordinates().x() + 1);
							position.setY(cd.coordinates().y());
							switch (relativeDistance.getValue())
							{
								case -1:
								{
									if (!cTD.card().getCorner(NW).isOccupied())
										list.add(position);
									break;
								}
								case 0:
								{
									if (!cTD.card().getCorner(SW).isOccupied())
										list.add(position);
									break;
								}
								case 1:
								{
									if (!cTD.card().getCorner(SE).isOccupied())
										list.add(position);
									break;
								}
							}

						}
					}

				}

		possiblePlacement = list;
		setCheckedFalse();
		System.err.println(sortedVector);
		System.err.println(possiblePlacement);
		if (list.isEmpty())
			throw new NotPlaceableException("No possible placement, you're stuck");
	}

	/**
	 * This method is needed to set the checked attributes to false in every corner of every played cards.
	 * This help the optimization of the checkPlacement method.
	 */
	private void setCheckedFalse()
	{
		for (CardData cd : sortedVector)
			for (Orientation o : values())
				if (cd.card().getCorner(o) != null)
					cd.card().getCorner(o).setChecked(false);
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
			return new Pair <Integer, Integer>(Math.abs(c1.x() - c2.x()), Math.abs(c1.y() - c2.y()));
		else
			return new Pair <Integer, Integer>(c1.x() - c2.x(), c1.y() - c2.y());
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

	/**
	 * The method return the number of points gained by scoring a specific objective.
	 *
	 * @param obj The objective card.
	 * @return The number of points gained.
	 */
	public int CheckObjectivePoints(ObjectiveCard obj) //maybe the two "pattern" could be recursive to avoid strange code construct
	{
		int points             = 0;
		int pointsPerCondition = obj.getPointsGiven();
		switch (obj.getObjType())
		{
			case "diagonal" ->
			{
				LinkedList <CardData> vector = new LinkedList <>(sortedVector);
				Symbol                color  = obj.getKingdom();
				Orientation           or;

				if (obj.getDiagonalParameters()[0] > obj.getDiagonalParameters()[1])
					or = Orientation.SE;
				else
					or = Orientation.NE;

				vector = new LinkedList <CardData>(vector.stream().filter(x -> x.card().getKingdom().equals(color)).toList());
				LinkedList <CardData> toRemove = new LinkedList <>();
				CardData              cardFound1;
				CardData              cardFound2;
				do
				{
					for (CardData cd : vector)
					{
						if (cd.card().getCorner(or) == null || !cd.card().getCorner(or).isOccupied())
						{
							toRemove.add(cd);
							continue;
						}
						else
						{
							cardFound1 = coordsFinder(orientatioToRelativeCoords(or, cd.coordinates()), vector);
							if (cardFound1 != null && cardFound1.card().getCorner(or).isOccupied())
							{
								cardFound2 = coordsFinder(orientatioToRelativeCoords(or, cardFound1.coordinates()), vector);
								if (cardFound2 != null)
								{
									points += pointsPerCondition;
									toRemove.add(cd);
									toRemove.add(cardFound1);
									toRemove.add(cardFound2);
									break;
								}
								else
								{
									toRemove.add(cd);
									toRemove.add(cardFound1);
									break;
								}
							}
							else
							{
								if (cardFound1 != null)
									toRemove.add(cardFound1);
								toRemove.add(cd);
								break;
							}
						}
					}
					for (CardData cardRemoved : toRemove)
					{
						vector.remove(cardRemoved);
					}
					toRemove.removeAll(toRemove);
				} while (!vector.isEmpty());
			}

			case "shapeL" ->
			{
				LinkedList <CardData> vector = new LinkedList <>(sortedVector);
				Symbol                color  = obj.getKingdom();
				Symbol                color2 = obj.getKingdom2();
				Orientation           or     = obj.getPosition();

				vector = new LinkedList <CardData>(vector.stream().filter(x -> x.card().getKingdom().equals(color)).toList());
				LinkedList <CardData> toRemove    = new LinkedList <>();
				CardData              cardFound1;
				CardData              cardFound2;
				CardData              tempCardFound;
				Coords                tempCoords  = new Coords(0, 0);
				Coords                tempCoords2 = new Coords(0, 0);

				do
				{
					for (CardData cd : vector)
					{

						tempCoords.setX(cd.coordinates().x() + 1);
						tempCoords.setY(cd.coordinates().y() + 1);

						cardFound1 = coordsFinder(tempCoords, vector);
						if (cardFound1 == null)
						{
							toRemove.add(cd);
							continue;
						}
						else
						{
							if (or.equals(SE) || or.equals(SW))
							{
								tempCardFound = cd;
							}
							else
							{
								tempCardFound = cardFound1;
							}
							if (tempCardFound.card().getCorner(or).isOccupied())
							{
								cardFound2 = coordsFinder(orientatioToRelativeCoords(or, tempCardFound.coordinates()), sortedVector);
								if (cardFound2 != null && cardFound2.card().getKingdom().equals(color2))
								{
									points += pointsPerCondition;
									toRemove.add(cd);
									toRemove.add(cardFound1);
									break;
								}
								else
								{
									toRemove.add(cardFound1);
									toRemove.add(cd);
									break;
								}

							}
							else
							{
								toRemove.add(cd);
								continue;
							}
						}
					}
					for (CardData cardRemoved : toRemove)
					{
						vector.remove(cardRemoved);
					}
					toRemove.removeAll(toRemove);
				} while (!vector.isEmpty());
			}
			case "trio" ->
			{
				points = (visibleElements.getSymbol(obj.getKingdom())) / 3 * pointsPerCondition;
			}
			case "duo" ->
			{
				points = (visibleElements.getSymbol(obj.getKingdom())) / 2 * pointsPerCondition;
			}
			case "all" ->
			{
				LinkedList <Integer> elements = new LinkedList <>();
				elements.add(visibleElements.getSymbol(Symbol.QUILL));
				elements.add(visibleElements.getSymbol(Symbol.INKWELL));
				elements.add(visibleElements.getSymbol(Symbol.MANUSCRIPT));
				int min = elements.stream().min(Integer::compareTo).get();
				points = min * pointsPerCondition;
			}
		}
		return points;
	}

	/**
	 * This method helps checkObjectivePoint to get coordinates that are pointed by a corner direction from "starting" coordinates.
	 *
	 * @param o      The orientation given (NE,NW,SE,SW)
	 * @param coords The coordinates that are used to calculate the new coordinates.
	 * @return The coordinates "pointed" by the corner direction given.
	 */
	private Coords orientatioToRelativeCoords(Orientation o, Coords coords)
	{
		Coords c;
		switch (o)
		{
			case SW ->
			{
				return c = new Coords(coords.x() - 1, coords.y());
			}
			case SE ->
			{
				return c = new Coords(coords.x(), coords.y() - 1);
			}
			case NW ->
			{
				return c = new Coords(coords.x() + 1, coords.y());
			}
			case NE ->
			{
				return c = new Coords(coords.x(), coords.y() + 1);
			}
		}
		return null;
	}


}
