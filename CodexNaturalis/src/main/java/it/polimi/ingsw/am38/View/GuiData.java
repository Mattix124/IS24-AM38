package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;

public class GuiData
{
	private ObjChoiceData objd;
	private String nickname;
	private String starter;
	private String firstTopR;
	private String firstTopG;
	private String firstRes1;
	private String firstRes2;
	private String firstGold1;
	private String firstGold2;
	private int objective;

	public String getStarter()
	{
		return starter;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setStarter(String starter)
	{
		this.starter = starter;
	}

	public String getFirstRes1()
	{
		return firstRes1;
	}

	public void setFirstRes1(String firstRes1)
	{
		this.firstRes1 = firstRes1;
	}

	public String getFirstRes2()
	{
		return firstRes2;
	}

	public void setFirstRes2(String firstRes2)
	{
		this.firstRes2 = firstRes2;
	}

	public String getFirstGold1()
	{
		return firstGold1;
	}

	public void setFirstGold1(String firstGold1)
	{
		this.firstGold1 = firstGold1;
	}

	public String getFirstGold2()
	{
		return firstGold2;
	}

	public void setFirstGold2(String firstGold2)
	{
		this.firstGold2 = firstGold2;
	}

	public ObjChoiceData getObjd()
	{
		return objd;
	}

	public void setObjd(ObjChoiceData objd)
	{
		this.objd = objd;
		this.nickname = objd.getNickname();
	}

	public String getFirstTopG()
	{
		return firstTopG;
	}

	public void setFirstTopG(String firstTopG)
	{
		this.firstTopG = firstTopG;
	}

	public String getFirstTopR()
	{
		return firstTopR;
	}

	public void setFirstTopR(String firstTopR)
	{
		this.firstTopR = firstTopR;
	}

	public int getObjective()
	{
		return objective;
	}

	public void setObjective(int objective)
	{
		this.objective = objective;
	}

}

