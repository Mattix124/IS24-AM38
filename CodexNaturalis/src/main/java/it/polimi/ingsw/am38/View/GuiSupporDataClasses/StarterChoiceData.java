package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

/**
 * Class that contains the info to choose the face of the starter card and the color of the pawn
 */
public final class StarterChoiceData
{
	/**
	 * StarterCard of the player
	 */
	private final StarterCard starterCard;
	/**
	 * Card on top of the gold deck
	 */
	private final Symbol goldTop;
	/**
	 * Card on top of the resource deck
	 */
	private final Symbol resourceTop;
	/**
	 * Gold card on the ground
	 */
	private final GoldCard gold1;
	/**
	 * Gold card on the ground
	 */
	private final GoldCard gold2;
	/**
	 * Resource card on the ground
	 */
	private final ResourceCard res1;
	/**
	 * Resource card on the ground
	 */
	private final ResourceCard res2;

	/**
	 * Constructor method that set the info in order to let the player choose the face of the starter
	 * card and the color of the pawn
	 *
	 * @param starterCard
	 * @param goldTop
	 * @param resourceTop
	 * @param gold1
	 * @param gold2
	 * @param res1
	 * @param res2
	 */
	public StarterChoiceData(StarterCard starterCard, Symbol goldTop, Symbol resourceTop, GoldCard gold1, GoldCard gold2, ResourceCard res1, ResourceCard res2)
	{
		this.starterCard = starterCard;
		this.goldTop = goldTop;
		this.resourceTop = resourceTop;
		this.gold1 = gold1;
		this.gold2 = gold2;
		this.res1 = res1;
		this.res2 = res2;
	}

	/**
	 *Getter for the attribute starterCard
	 *
	 * @return starterCard
	 */
	public StarterCard getStarterCard()
	{
		return starterCard;
	}

	/**
	 *Getter for the attribute goldTop
	 *
	 * @return goldTop
	 */
	public Symbol getGoldTop()
	{
		return goldTop;
	}

	/**
	 *Getter for the attribute resourceTop
	 *
	 * @return resourceTop
	 */
	public Symbol getResourceTop()
	{
		return resourceTop;
	}

	/**
	 *Getter for the attribute gold1
	 *
	 * @return gold1
	 */
	public GoldCard getGold1()
	{
		return gold1;
	}

	/**
	 *Getter for the attribute gold2
	 *
	 * @return gold2
	 */
	public GoldCard getGold2()
	{
		return gold2;
	}

	/**
	 *Getter for the attribute res1
	 *
	 * @return res1
	 */
	public ResourceCard getRes1()
	{
		return res1;
	}

	/**
	 *Getter for the attribute res2
	 *
	 * @return res2
	 */
	public ResourceCard getRes2()
	{
		return res2;
	}
}
