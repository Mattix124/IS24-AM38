package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * CardData is a "container" for a PlayableCard and its coordinates added when the card is played.
 *
 * @param coordinates The coordinates where the card is placed.
 * @param card The card played.
 */
public record CardData(Coords coordinates, PlayableCard card)
{
	/**
	 * Return a "simple" string for debugging scopes
	 *
	 * @return
	 */
	@Override
	public String toString()
	{
		return "X:\t"+coordinates.x()+ "\tY:\t"+coordinates.y()+"\tCardType:\t"+card.getClass().getClass();
	}
}
