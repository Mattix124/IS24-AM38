package it.polimi.ingsw.am38.DataStruture;

import java.util.LinkedList;

public class DataStructure
{
	private final LinkedList <CardData> Vector;

	public DataStructure(LinkedList <CardData> vector) {Vector = vector;}

	public void addCard(CardData insertedCard)
	{
		Vector.add(insertedCard);
	}



}
