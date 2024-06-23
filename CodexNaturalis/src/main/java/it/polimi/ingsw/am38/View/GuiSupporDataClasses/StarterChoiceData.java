package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

public final class StarterChoiceData
{
	private final StarterCard starterCard;
	private final Symbol goldTop;
	private final Symbol resourceTop;
	private final GoldCard gold1;
	private final GoldCard gold2;
	private final ResourceCard res1;
	private final ResourceCard res2;

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

	public StarterCard getStarterCard()
	{
		return starterCard;
	}

	public Symbol getGoldTop()
	{
		return goldTop;
	}

	public Symbol getResourceTop()
	{
		return resourceTop;
	}

	public GoldCard getGold1()
	{
		return gold1;
	}

	public GoldCard getGold2()
	{
		return gold2;
	}

	public ResourceCard getRes1()
	{
		return res1;
	}

	public ResourceCard getRes2()
	{
		return res2;
	}
}
