package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NoPossiblePlacement;
import it.polimi.ingsw.am38.Enum.Orientation;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.am38.Enum.Orientation.*;

/**
 * This class contains the board data of each player. The class allows to place cards, to calculate points, and manage to calculate the points of a placed card
 */
public class Field implements Serializable
{
	@Serial
	private static final long serialVersionUID = -5134919055018198658L;
	/**
	 * visibleElements is the attribute that store the visible symbols of the board. Used to calculate points and checking the goldCard placement
	 */
	private final VisibleElements visibleElements = new VisibleElements();
	/**
	 * sortedVector is the attribute that stores all the cards played by the player. Almost all the method use this attribute to solve their mansion
	 */
	private final LinkedList <CardData> sortedVector = new LinkedList <>();
	/**
	 * possiblePlacement is an attributes that stores all the possible placement for a card
	 */
	private LinkedList <Coords> possiblePlacement = new LinkedList <>();
	/**
	 * This attribute establish the order of placement of card (it coincides with the turn number)
	 */
	private int order;

	/**
	 * This is the constructor method that initializes the Field class. It needs a starting card and based on its parameters update the visibleElements data
	 *
	 * @param starter The starting card needed to start a match
	 */
	public Field(StarterCard starter) //Field class take a starter card to initiate itself.
	{
		this.order = 0;
		CardData c = new CardData(new Coords(0, 0), starter);
		starter.setOrder(order);
		sortedVector.add(c);
		Symbol[] sArr = starter.getCentralKingdom();
		if (starter.getFace())
			for (Symbol s : sArr) //add the symbols located in the center of a starter card
			{
				visibleElements.increaseSymbol(s);
			}
		for (Orientation o : Orientation.values()) //add symbols of the corners
		{
			if (starter.getCorner(o) != null && !starter.getCorner(o).getSymbol().equals(Symbol.NULL))
			{
				visibleElements.increaseSymbol(starter.getCorner(o).getSymbol());
			}
		}
		try
		{
			checkPlacement(); //update the possiblePlacement vector
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This method is exposed to allow the placing (or not) of Resource card
	 *
	 * @param card   This parameter contain a ResourceCard that will be placed if the coordinates chosen by the player are correct
	 * @param coords This parameter contain the Coordinates chosen by the player to place the card
	 */
	public int tryPlaceCard(ResourceCard card, Coords coords) throws NoPossiblePlacement
	{
		int point = 0;

		if (possiblePlacement.contains(coords))
		{
			card.setOrder(order);
			updateFieldElements(card, coords);
			if (card.getPointsPerCondition() != 0 && card.getFace())
				point = card.getPointsPerCondition();
			addOrderedCard(new CardData(coords, card), sortedVector);
			order++;
			checkPlacement();
			return point;
		}
		else
		{
			throw new NoPossiblePlacement("You can't place there!");
		}
	}

	/**
	 * This method is exposed to allow the placing (or not) of Gold card
	 *
	 * @param card   This parameter contain a GoldCard that will be placed if the coordinates chosen by the player are correct
	 * @param coords This parameter contain the Coordinates chosen by the player to place the card
	 */
	public int tryPlaceCard(GoldCard card, Coords coords) throws NoPossiblePlacement, NotPlaceableException
	{
		int point = 0;
		if (possiblePlacement.contains(coords) && checkGoldCardPlacementCondition(card))
		{
			card.setOrder(order);
			order++;
			updateFieldElements(card, coords);
			if (card.getFace())
				point = checkGoldCardPoints(card, coords);
			addOrderedCard(new CardData(coords, card), sortedVector);
			checkPlacement();
			return point;
		}
		else
		{
			throw new NotPlaceableException("You can't place there!");
		}
	}

	/**
	 * The method return the number of points gained by scoring a specific objective
	 *
	 * @param obj The objective card
	 * @return The number of points gained
	 */
	public int CheckObjectivePoints(ObjectiveCard obj) //maybe the two "pattern" could be recursive to avoid strange code construct
	{
		int points             = 0;
		int pointsPerCondition = obj.getPointsGiven();
		switch (obj.getObjType())
		{
			case "diagonal" ->
			{
				Symbol      color = obj.getKingdom();
				Orientation or    = obj.getPosition();

				LinkedList <CardData> vector   = new LinkedList <>(sortedVector.stream().filter(x -> x.card().getKingdom() != null && x.card().getKingdom().equals(color)).toList());     //this line make a list that contain// all the placed card
				LinkedList <CardData> toRemove = new LinkedList <>();        //List that contains all the card needed to be removed by the algorithm.																										//all the placed card with the color given by the objective card
				CardData              cardFound1;
				CardData              cardFound2;
				do
				{
					for (CardData cd : vector) //for every colored card
					{
						if (cd.card().getCorner(or) == null || !cd.card().getCorner(or).isOccupied()) // that not have a corner in the direction of the objective and haven't a contiguous card in the same direction
							toRemove.add(cd);
						else
						{
							cardFound1 = coordsFinder(orientationToRelativeCoords(or, cd.coordinates()), vector);
							if (cardFound1 != null && cardFound1.card().getCorner(or) != null && cardFound1.card().getCorner(or).isOccupied()) //if the card (cd) checked has a contiguous card in the direction given by the objective
							{
								cardFound2 = coordsFinder(orientationToRelativeCoords(or, cardFound1.coordinates()), vector); //check if there is another
								if (cardFound2 != null) //if exists
								{
									points += pointsPerCondition; //give points and remove from the vector, so it won't count again in the same objective.
									toRemove.add(cd);
									toRemove.add(cardFound1);
									toRemove.add(cardFound2);
								}
								else // if 3rd card not exists remove 1st and 2nd cards (they can't be part of the objective)
								{
									toRemove.add(cd);
									toRemove.add(cardFound1);
								}
							}
							else //if the 1st card has not a contiguous card in the direction given by the objective or there isn't an angle that make the continuous direction possible remove them.
							{
								if (cardFound1 != null) //if 2nd card exist remove
									toRemove.add(cardFound1);
								toRemove.add(cd); //remove first card always.
							}
							break;
						}
					}
					vector.removeAll(toRemove); //the effective command that remove all the card above based on which "case" they pass through.
					toRemove.removeAll(toRemove); //empty the remover list.
				} while (!vector.isEmpty()); //if the vector is empty there's no more point to give.
			}

			case "shapeL" -> //similar to previous case
			{
				Symbol                color      = obj.getKingdom();
				Symbol                color2     = obj.getKingdom2();
				Orientation           or         = obj.getPosition();
				LinkedList <CardData> vector     = new LinkedList <>(sortedVector.stream().filter(x -> x.card().getKingdom() != null && x.card().getKingdom().equals(color)).toList()); //list that contains all the placed card with the "main color" (2 card vertical)
				LinkedList <CardData> toRemove   = new LinkedList <>();
				CardData              cardFound1;
				CardData              cardFound2;
				CardData              tempCardFound; //this variable helps to short the code of this algorithm.
				Coords                tempCoords = new Coords(0, 0);

				do
				{
					for (CardData cd : vector) //for every colored card
					{
						tempCoords.setX(cd.coordinates().x() + 1);  //search for the card directly above (this algorithm uses the order given by sortedVector (and vector) so you will check always first the most left-down card).
						tempCoords.setY(cd.coordinates().y() + 1);

						cardFound1 = coordsFinder(tempCoords, vector);
						if (cardFound1 == null) //if the card above not exists remove the card checked (not possible for that card to give a point)
							toRemove.add(cd);
						else
						{
							if (or.equals(SE) || or.equals(SW)) //using the orientation of the "secondary" card, choose the card that needs the corner check for the "secondary" card
								tempCardFound = cd;                // (if secondary card is at S-, the card needed to be checked will be cd (the first card you found) if secondary is N- the card needed to be checked will be the card above cd)

							else
								tempCardFound = cardFound1; //the card above cd

							if (tempCardFound.card().getCorner(or) != null && tempCardFound.card().getCorner(or).isOccupied()) //if the card checked has an angle and is occupied
							{
								cardFound2 = coordsFinder(orientationToRelativeCoords(or, tempCardFound.coordinates()), sortedVector); //search in ALL the placed card (so all the colored card)
								if (cardFound2 != null && cardFound2.card().getKingdom().equals(color2)) //if the card (secondary) found exists and has the color of the objective given.
								{
									points += pointsPerCondition;
									toRemove.add(cd);
									toRemove.add(cardFound1); //give the points and remove the cards used. (secondary card doesn't need to be removed)
								}
								else
								{
									toRemove.add(cardFound1); //remove because the two main cards cannot give points.
									toRemove.add(cd);
								}
								break;
							}
							else
								toRemove.add(cd);
						}
					}
					vector.removeAll(toRemove); //remove the cards added to toRemove list from vector (as said, they will be of the main color and no others)
					toRemove.removeAll(toRemove); //emptying the remover list.
				} while (!vector.isEmpty());
			}
			case "trio" -> points = (visibleElements.getSymbol(obj.getKingdom())) / 3 * pointsPerCondition;
			case "duo" -> points = (visibleElements.getSymbol(obj.getItem())) / 2 * pointsPerCondition;
			case "all" ->
			{ //find the minimum of the 3 items count. That will be multiplied by the score given by the objective
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
	 * This method update the visibleElements class which contain the visible symbols on the player field.
	 * Also, check every card around the card that will be placed to set the needed values (like Occupied in the Corner class)
	 *
	 * @param card   The card which will covers at least one corner so a possible element
	 * @param coords The coordinates that indicate where the card is inserted
	 */
	private void updateFieldElements(PlayableCard card, Coords coords)
	{
		CardData underCard = coordsFinder(orientationToRelativeCoords(NW, coords), sortedVector);  //the card at NW from the card placed
		if (underCard != null) //if exists
		{
			if (card.getCorner(NW) != null) //if the card placed has a corner it will be occupied now.
				card.getCorner(NW).setOccupied(true);
			underCard.card().getCorner(SE).setOccupied(true); //obviously the underCard has a corner and will be set to occupied
			if (underCard.card().getCorner(SE).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(SE).getSymbol(), -1); //remove the symbol count from visibleElements because the symbol now is covered,
		}
		underCard = coordsFinder(orientationToRelativeCoords(NE, coords), sortedVector);
		if (underCard != null)
		{
			if (card.getCorner(NE) != null)
				card.getCorner(NE).setOccupied(true);
			underCard.card().getCorner(SW).setOccupied(true);
			if (underCard.card().getCorner(SW).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(SW).getSymbol(), -1);
		}
		underCard = coordsFinder(orientationToRelativeCoords(SW, coords), sortedVector);
		if (underCard != null)
		{
			if (card.getCorner(SW) != null)
				card.getCorner(SW).setOccupied(true);
			underCard.card().getCorner(NE).setOccupied(true);
			if (underCard.card().getCorner(NE).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(NE).getSymbol(), -1);
		}
		underCard = coordsFinder(orientationToRelativeCoords(SE, coords), sortedVector);
		if (underCard != null)
		{
			if (card.getCorner(SE) != null)
				card.getCorner(SE).setOccupied(true);
			underCard.card().getCorner(NW).setOccupied(true);
			if (underCard.card().getCorner(NW).getSymbol() != Symbol.NULL)
				visibleElements.increaseSymbol(underCard.card().getCorner(NW).getSymbol(), -1);
		}
		if (card.getFace()) //if the card is face up, so its Corners can contain a Symbol
		{
			for (Orientation o : values()) //for every possible orientation
				if (card.getCorner(o) != null && card.getCorner(o).getSymbol() != Symbol.NULL)
					visibleElements.increaseSymbol(card.getCorner(o).getSymbol());
		}
		else
			visibleElements.increaseSymbol(card.getKingdom()); //if the placed card is face down, the central symbol will be increased.

	}

	/**
	 * The method gives the amount of point for the placement of a GoldaCard in the given coordinates
	 *
	 * @param card   The GoldCard that will be placed
	 * @param coords The coordinates where the GoldCard will be placed
	 * @return The amount of points
	 */
	private int checkGoldCardPoints(GoldCard card, Coords coords)
	{
		int points             = 0;
		int pointsPerCondition = card.getPointsPerCondition();
		if (card.getGoldPointsCondition() != null)
			switch (card.getGoldPointsCondition())
			{
				case QUILL -> points = visibleElements.getSymbol(Symbol.QUILL) * pointsPerCondition;
				case INKWELL -> points = visibleElements.getSymbol(Symbol.INKWELL) * pointsPerCondition;
				case MANUSCRIPT -> points = visibleElements.getSymbol(Symbol.MANUSCRIPT) * pointsPerCondition;
				case CORNER ->
				{

					CardData aroundCard = coordsFinder(orientationToRelativeCoords(NE, coords), sortedVector); //it controls for every corner if there is a card. (So a Corner that will be covered)
					if (aroundCard != null)
						points += pointsPerCondition;
					aroundCard = coordsFinder(orientationToRelativeCoords(SW, coords), sortedVector);
					if (aroundCard != null)
						points += pointsPerCondition;
					aroundCard = coordsFinder(orientationToRelativeCoords(SE, coords), sortedVector);
					if (aroundCard != null)
						points += pointsPerCondition;
					aroundCard = coordsFinder(orientationToRelativeCoords(NW, coords), sortedVector);
					if (aroundCard != null)
						points += pointsPerCondition;
				}
			}
		else
		{ // if the card does not have the condition (but still gives points)
			points = pointsPerCondition;
		}
		return points;
	}

	/**
	 * This private method help checkGoldCardPoints and CheckObjectivePoints to have given a card that has the given coordinates
	 *
	 * @param coords The coordinate that identify a card location
	 * @param vector The collection where to find the card
	 * @return null if there is no card in the coordinates given, otherwise, the card found
	 */
	private CardData coordsFinder(Coords coords, LinkedList <CardData> vector)
	{
		for (CardData cd : vector)
			if (cd.coordinates().equals(coords))
				return cd;

		return null;
	}

	/**
	 * This method tells if a GoldCard can be placed in the field using its requirements
	 *
	 * @param card The GoldCard that (not) will be placed
	 * @return outcome that determine if the card can be placed
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
	 * This method is an "automated method" and at the end of a player's turn refresh the possiblePlacement list
	 *
	 * @throws NoPossiblePlacement If there are no possible coordinates for placing ANY card, an exception is thrown and the player will get stuck
	 */
	private void checkPlacement() throws NoPossiblePlacement
	{
		LinkedList <Coords> list = new LinkedList <>();
		EnteredCardControl  eCC;
		for (CardData cd : sortedVector)
			for (Orientation o : Orientation.values())
			{
				if (cd.card().getCorner(o) != null && !cd.card().getCorner(o).isOccupied() && !cd.card().getCorner(o).isChecked())
				{
					Coords possibleCoords = orientationToRelativeCoords(o, cd.coordinates());
					eCC = chooseCornerCheck(o, possibleCoords);
					if (eCC.getEnteredCard() == eCC.getCheckedAngle())
						list.add(possibleCoords);
				}
			}
		possiblePlacement = list;
		setCheckedFalse();
		if (list.isEmpty())
			throw new NoPossiblePlacement("");
	}

	/**
	 * This method help checkPlacement method to assure there is a possible placement on the orientation of the checked card given as parameter
	 *
	 * @param o              orientation (NW,NE,SW,SE) on which a card could be placed referred to the cd card
	 * @param possibleCoords the possible position where a card could be placed
	 * @return return a EnteredCardControl. If the 2 attributes inside coincide, the possibleCords are a valid placement.
	 */
	private EnteredCardControl chooseCornerCheck(Orientation o, Coords possibleCoords)
	{
		EnteredCardControl enteredCardControl = new EnteredCardControl(); //a variable that stores the cards present and the Corner that the cards present have.
		switch (o) //for every orientation there are max 3 cards that are needed to be checked to control if a placement is possible.
		{
			case SW ->
			{
				CardData c = coordsFinder(orientationToRelativeCoords(NW, possibleCoords), sortedVector);
				if (c != null) //if the cards exists
				{
					enteredCardControl.increaseEnteredCard();
					if (c.card().getCorner(SE) != null) //if the cards has a Corner
					{
						enteredCardControl.increaseCheckedAngle(); //CornerNumber increase
						c.card().getCorner(SE).setChecked(true); //checked is for optimization purpose
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
		return enteredCardControl; //return the number of cards entered and the corner present, if both this number are equals then a placement is possible.
	}

	/**
	 * This method is needed to set the checked attributes to false in every corner of every played cards
	 * This help the optimization of the checkPlacement method
	 */
	private void setCheckedFalse()
	{
		for (CardData cd : sortedVector)
			for (Orientation o : values())
				if (cd.card().getCorner(o) != null)
					cd.card().getCorner(o).setChecked(false);
	}

	/**
	 * The method will place the card only if the coordinates chosen by the player in the method tryPlaceCard are allowed, then place the card in an ordered array that contains all played cards
	 *
	 * @param insertedCard Card chosen by the player
	 * @param v            This parameter is the vector where the card will be placed (sortedVector for the played card or the twoDistanceCard parameter of a card)
	 */
	private void addOrderedCard(CardData insertedCard, LinkedList <CardData> v)
	{
		int indexElement;
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
					if (insertedCard.coordinates().y() < c.coordinates().y())
					{
						v.add(indexElement, insertedCard);
						break;
					}
				}
			}
			if (indexElement == v.size() - 1)
			{
				v.add(insertedCard);
				break;
			}
		}
	}

	/**
	 * This method helps checkObjectivePoint to get coordinates that are pointed by a corner direction from "starting" coordinates
	 *
	 * @param o      The orientation given (NE,NW,SE,SW)
	 * @param coords The coordinates that are used to calculate the new coordinates
	 * @return The coordinates "pointed" by the corner direction given
	 */
	private Coords orientationToRelativeCoords(Orientation o, Coords coords)
	{

		switch (o)
		{
			case SW ->
			{
				return new Coords(coords.x() - 1, coords.y());
			}
			case SE ->
			{
				return new Coords(coords.x(), coords.y() - 1);
			}
			case NW ->
			{
				return new Coords(coords.x(), coords.y() + 1);
			}
			case NE ->
			{
				return new Coords(coords.x() + 1, coords.y());
			}
		}
		return null;
	}

	//GETTERS FOR TESTS--------------------------------------------------------------------------------------------------------------------------

	public PlayableCard getCardFromCoordinate(int x, int y)
	{
		Coords c = new Coords(x, y);
		for (CardData cd : sortedVector)
			if (cd.coordinates().equals(c))
				return cd.getCard();

		return null;
	}

	public VisibleElements getVisibleElements()
	{
		return visibleElements;
	}

	/**
	 * Getter for the card played by the player
	 * @return the vector container of cards
	 */
	public LinkedList <CardData> getSortedVector()
	{
		return sortedVector;
	}

	public LinkedList <Coords> getPossiblePlacement()
	{
		return possiblePlacement;
	}
}
