package it.polimi.ingsw.am38.View;

import javafx.scene.image.ImageView;

public class GuiData
{
	private String nickname;
	private ImageView starter;
	private ImageView firstRes1;
	private ImageView firstRes2;
	private ImageView firstGold1;
	private ImageView FirstGold2;

	public ImageView getStarter()
	{
		return starter;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setStarter(ImageView starter)
	{
		this.starter = starter;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public ImageView getFirstRes1()
	{
		return firstRes1;
	}

	public void setFirstRes1(ImageView firstRes1)
	{
		this.firstRes1 = firstRes1;
	}

	public ImageView getFirstRes2()
	{
		return firstRes2;
	}

	public void setFirstRes2(ImageView firstRes2)
	{
		this.firstRes2 = firstRes2;
	}

	public ImageView getFirstGold1()
	{
		return firstGold1;
	}

	public void setFirstGold1(ImageView firstGold1)
	{
		this.firstGold1 = firstGold1;
	}

	public ImageView getFirstGold2()
	{
		return FirstGold2;
	}

	public void setFirstGold2(ImageView firstGold2)
	{
		FirstGold2 = firstGold2;
	}
}

