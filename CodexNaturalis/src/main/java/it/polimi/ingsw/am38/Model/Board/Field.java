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
	private final VisibleElements visibleElements = new VisibleElements();
	/**
	 * sortedVector is the attribute that stores all the cards played by the player. Almost all the method use this attribute to solve their mansion.
	 */
	private final LinkedList <CardData> sortedVector = new LinkedList <>();
	/**
	 * possiblePlacement is an attributes that stores all the possible placement for a card.
	 */
	private LinkedList <Coords> possiblePlacement = new LinkedList <>();
	/**
	 * This attribute establish the order of placement of card (it coincides with the turn number)
	 */
	private int order;

	/**
	 * This is the constructor method that initializes the Field class. It needs a starting card and based on its parameters update the visibleElements data.
	 *
	 * @param starter The starting card needed to start a match.
	 */
	public Field(StarterCard starter) //Field class take a starter card to initiate itself.
	{
		this.order = 0;
		CardData c = new CardData(new Coords(0, 0), starter);
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
			if (card.getPointsWon() != 0)
				point = card.getPointsWon();
			addOrderedCard(new CardData(coords, card), sortedVector);
			System.out.println("Card placed");
			order++;
			checkPlacement();


			return point;
		}
		else
		{
			throw new NotPlaceableException("You can't place here!");
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
		CardData underCard = coordsFinder(orientationToRelativeCoords(NW, coords), sortedVector);
		if (underCard != null && card.getCorner(NW) != null)
		{
			card.getCorner(NW).setOccupied(true);
			underCard.card().getCorner(SE).setOccupied(true);
			if (underCard.card().getCorner(SE).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(SE).getSymbol(), -1);
		}
		underCard = coordsFinder(orientationToRelativeCoords(NE, coords), sortedVector);
		if (underCard != null && card.getCorner(NE) != null)
		{
			card.getCorner(NE).setOccupied(true);
			underCard.card().getCorner(SW).setOccupied(true);
			if (underCard.card().getCorner(SW).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(SW).getSymbol(), -1);
		}
		underCard = coordsFinder(orientationToRelativeCoords(SW, coords), sortedVector);
		if (underCard != null && card.getCorner(SW) != null)
		{
			card.getCorner(SW).setOccupied(true);
			underCard.card().getCorner(NE).setOccupied(true);
			if (underCard.card().getCorner(NE).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(NE).getSymbol(), -1);
		}
		underCard = coordsFinder(orientationToRelativeCoords(SE, coords), sortedVector);
		if (underCard != null && card.getCorner(SE) != null)
		{
			card.getCorner(SE).setOccupied(true);
			underCard.card().getCorner(NW).setOccupied(true);
			if (underCard.card().getCorner(NW).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(NW).getSymbol(), -1);
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
			addOrderedCard(new CardData(coords, card), sortedVector);
			System.out.println("Card placed");
			checkPlacement();
			return point;
		}
		else
		{
			throw new NotPlaceableException("You can't place here!");
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
			switch (card.getGoldPointsCondition()) {
				case QUILL -> points = visibleElements.getSymbol(Symbol.QUILL) * pointsPerCondition;
				case INKWELL -> points = visibleElements.getSymbol(Symbol.INKWELL) * pointsPerCondition;
				case MANUSCRIPT -> points = visibleElements.getSymbol(Symbol.MANUSCRIPT) * pointsPerCondition;
				case CORNER -> {
					Coords   aroundCoords = new Coords(coords.x() + 1, coords.y());
					CardData aroundCard   = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)
						if (aroundCard.card().getCorner(Orientation.SW) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x() - 1);
					aroundCoords.setY(coords.y());
					aroundCard = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)
						if (aroundCard.card().getCorner(Orientation.NE) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x());
					aroundCoords.setY(coords.y() - 1);
					aroundCard = coordsFinder(aroundCoords, sortedVector);
					if (aroundCard != null)
						if (aroundCard.card().getCorner(Orientation.NW) != null)
							points += pointsPerCondition;
					aroundCoords.setX(coords.x());
					aroundCoords.setY(coords.y() + 1);
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

		return (visibleElements.getSymbol(Symbol.FUNGI) >= fungi && visibleElements.getSymbol(Symbol.INSECT) >= insect && visibleElements.getSymbol(Symbol.ANIMAL) >= animal && visibleElements.getSymbol(Symbol.PLANT) >= plant);
	}

	/**
	 * This method is an "automated method" and at the end of a player's turn refresh the possiblePlacement list.
	 *
	 * @throws NotPlaceableException If there are no possible coordinates for placing ANY card, an exception is thrown and the player will get stuck
	 */
	private void checkPlacement() throws NotPlaceableException
	{
		LinkedList <Coords> list = new LinkedList <>();
		EnteredCardControl  eCC;
		for (CardData cd : sortedVector)
			for (Orientation o : Orientation.values())
			{
				if (cd.card().getCorner(o) != null && !cd.card().getCorner(o).isOccupied() && !cd.card().getCorner(o).isChecked())
				{
					Coords possibleCoords = orientationToRelativeCoords(o, cd.coordinates());
					eCC = chooseCornerCheck(o, cd, possibleCoords);
					if (eCC.getEnteredCard() == eCC.getCheckedAngle())
						list.add(possibleCoords);
				}
			}
		possiblePlacement = list;
		setCheckedFalse();
		if (list.isEmpty())
			throw new NotPlaceableException("");
	}

	/**
	 * This method help checkPlacement method to assure there is a possible placement on the orientation of the checked card given as parameter,
	 *
	 * @param o              orientation (NW,NE,SW,SE) on which a card could be placed referred to the cd card
	 * @param cd             the card which corners is being checked.
	 * @param possibleCoords the possible position where a card could be placed
	 * @return return a EnteredCardControl. If the 2 attributes inside coincide, the possibleCords are a valid placement.
	 */
	private EnteredCardControl chooseCornerCheck(Orientation o, CardData cd, Coords possibleCoords)
	{
		EnteredCardControl enteredCardControl = new EnteredCardControl();
		switch (o)
		{
			case SW ->
			{
				CardData c = coordsFinder(orientationToRelativeCoords(NW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SE).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(SW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NE).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(SE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NW).setChecked(true);
					}
				}
			}
			case SE ->
			{
				CardData c = coordsFinder(orientationToRelativeCoords(SW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NE).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(SE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NW).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(NE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SW).setChecked(true);
					}
				}
			}
			case NE ->
			{
				CardData c = coordsFinder(orientationToRelativeCoords(NW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SE).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(NE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SW).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(SE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NW).setChecked(true);
					}
				}
			}
			case NW ->
			{
				CardData c = coordsFinder(orientationToRelativeCoords(NW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SE).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(NE, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SW) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(SW).setChecked(true);
					}
				}
				c = coordsFinder(orientationToRelativeCoords(SW, possibleCoords), sortedVector);
				if (c != null)
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(NE) != null)
					{
						enteredCardControl.increaseCheckedAngle();
						c.card().getCorner(NE).setChecked(true);
					}
				}
			}
		}
		return enteredCardControl;
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
	 * The method will place the card only if the coordinates chosen by the player in the method tryPlaceCard are allowed, then place the card in an ordered array that contains all played cards.
	 *
	 * @param insertedCard Card chosen by the player
	 * @param v            This parameter is the vector where the card will be placed (sortedVector for the played card or the twoDistanceCard parameter of a card)
	 */
	private void addOrderedCard(CardData insertedCard, LinkedList <CardData> v)
	{
		int      indexElement;
		CardData L = v.getLast();
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
							cardFound1 = coordsFinder(orientationToRelativeCoords(or, cd.coordinates()), vector);
							if (cardFound1 != null && cardFound1.card().getCorner(or).isOccupied())
							{
								cardFound2 = coordsFinder(orientationToRelativeCoords(or, cardFound1.coordinates()), vector);
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
								cardFound2 = coordsFinder(orientationToRelativeCoords(or, tempCardFound.coordinates()), sortedVector);
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
	private Coords orientationToRelativeCoords(Orientation o, Coords coords)
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
				return c = new Coords(coords.x(), coords.y() + 1);
			}
			case NE ->
			{
				return c = new Coords(coords.x() + 1, coords.y());
			}
		}
		return null;
	}

	//GETTERS AND SETTERS FOR TESTS--------------------------------------------------------------------------------------------------------------------------
	public CardData getCardFromCoordinate(Coords c)
	{
		for (CardData cd : sortedVector)
			if (cd.coordinates().equals(c))
				return cd;

		return null;
	}

	public VisibleElements getVisibleElements()
	{
		return visibleElements;
	}

	public LinkedList <CardData> getSortedVector()
	{
		return sortedVector;
	}

	public LinkedList <Coords> getPossiblePlacement()
	{
		return possiblePlacement;
	}
}
