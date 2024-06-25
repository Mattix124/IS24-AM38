package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

public class GuiPlacedConfirm
{
	private final String nick;
	private final PlayableCard card;
	private final int x;
	private final int y;

	public GuiPlacedConfirm(String nick, PlayableCard card, int x, int y)
	{
		this.nick = nick;
		this.card = card;
		this.x = x;
		this.y = y;
	}

	public String getNick()
	{
		return nick;
	}

	public PlayableCard getCard()
	{
		return card;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
