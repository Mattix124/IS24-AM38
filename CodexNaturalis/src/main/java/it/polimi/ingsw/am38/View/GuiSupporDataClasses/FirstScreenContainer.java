package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

public class FirstScreenContainer
{

	private ObjChoiceData objChoiceData;

	private StarterChoiceData starterChoiceData;

	public FirstScreenContainer(ObjChoiceData objChoiceData, StarterChoiceData starterChoiceData)
	{
		this.objChoiceData = objChoiceData;
		this.starterChoiceData = starterChoiceData;
	}

	public ObjChoiceData getObjChoiceData()
	{
		return objChoiceData;
	}

	public StarterChoiceData getStarterChoiceData()
	{
		return starterChoiceData;
	}
}
