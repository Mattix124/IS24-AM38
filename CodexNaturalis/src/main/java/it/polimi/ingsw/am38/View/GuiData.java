package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;

/**
 * Class that contains data for the GUI
 */
public class GuiData
{
	/**
	 * ObjChoiceData instance
	 */
	private ObjChoiceData objd;
	/**
	 * Nickname of the player
	 */
	private String nickname;
	/**
	 * String that indicates the face of the starter card
	 */
	private String starter;
	/**
	 * Card on top of the resource deck
	 */
	private String firstTopR;
	/**
	 * Card on top of the gold deck
	 */
	private String firstTopG;
	/**
	 * Card on the resource ground
	 */
	private String firstRes1;
	/**
	 * Card on the resource ground
	 */
	private String firstRes2;
	/**
	 * Card on the gold ground
	 */
	private String firstGold1;
	/**
	 * Card on the gold ground
	 */
	private String firstGold2;
	/**
	 * Objective card
	 */
	private int objective;

	/**
	 * Getter for the attribute starter
	 *
	 * @return starter
	 */
	public String getStarter()
	{
		return starter;
	}

	/**
	 * Getter for the attribute nickname
	 *
	 * @return nickname
	 */
	public String getNickname()
	{
		return nickname;
	}

	/**
	 * Setter for the attribute starter
	 *
	 * @param starter
	 */
	public void setStarter(String starter)
	{
		this.starter = starter;
	}

	/**
	 * Getter for the attribute firstRes1
	 *
	 * @return firstRes1
	 */
	public String getFirstRes1()
	{
		return firstRes1;
	}

	/**
	 * Setter for the attribute firstRes1
	 *
	 * @param firstRes1
	 */
	public void setFirstRes1(String firstRes1)
	{
		this.firstRes1 = firstRes1;
	}

	/**
	 * Getter for the attribute firstRes2
	 *
	 * @return firstRes2
	 */
	public String getFirstRes2()
	{
		return firstRes2;
	}

	/**
	 * Setter for the attribute firstRes2
	 *
	 * @param firstRes2
	 */
	public void setFirstRes2(String firstRes2)
	{
		this.firstRes2 = firstRes2;
	}

	/**
	 * Getter for the attribute firstGold1
	 *
	 * @return firstGold1
	 */
	public String getFirstGold1()
	{
		return firstGold1;
	}

	/**
	 * Setter for the attribute firstGold1
	 *
	 * @param firstGold1
	 */
	public void setFirstGold1(String firstGold1)
	{
		this.firstGold1 = firstGold1;
	}

	/**
	 * Getter for the attribute firstGold2
	 *
	 * @return firstGold2
	 */
	public String getFirstGold2()
	{
		return firstGold2;
	}

	/**
	 * Setter for the attribute firstGold2
	 *
	 * @param firstGold2
	 */
	public void setFirstGold2(String firstGold2)
	{
		this.firstGold2 = firstGold2;
	}

	/**
	 * Getter for the attribute objd
	 *
	 * @return objd
	 */
	public ObjChoiceData getObjd()
	{
		return objd;
	}

	/**
	 * Setter for the attribute objd
	 *
	 * @param objd
	 */
	public void setObjd(ObjChoiceData objd)
	{
		this.objd = objd;
		this.nickname = objd.getNickname();
	}

	/**
	 * Getter for the attribute firstTopG
	 *
	 * @return firstTopG
	 */
	public String getFirstTopG()
	{
		return firstTopG;
	}

	/**
	 * Setter for the attribute firstTopG
	 *
	 * @param firstTopG
	 */
	public void setFirstTopG(String firstTopG)
	{
		this.firstTopG = firstTopG;
	}

	/**
	 * Getter for the attribute firstTopR
	 *
	 * @return firstTopR
	 */
	public String getFirstTopR()
	{
		return firstTopR;
	}

	/**
	 * Setter for the attribute firstTopR
	 *
	 * @param firstTopR
	 */
	public void setFirstTopR(String firstTopR)
	{
		this.firstTopR = firstTopR;
	}

	/**
	 * Getter for the attribute objective
	 *
	 * @return objective
	 */
	public int getObjective()
	{
		return objective;
	}

	/**
	 * Setter for the attribute objective
	 *
	 * @param objective
	 */
	public void setObjective(int objective)
	{
		this.objective = objective;
	}

}

