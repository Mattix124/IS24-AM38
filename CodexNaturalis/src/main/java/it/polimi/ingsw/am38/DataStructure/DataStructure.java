package it.polimi.ingsw.am38.DataStructure;

import java.util.LinkedList;


public class DataStructure
{
	private final LinkedList <CardData> cardVector;

	public DataStructure(LinkedList <CardData> vector) {cardVector = vector;}

	public void addCard(CardData insertedCard)
	{
		cardVector.add(insertedCard);
	}

	public DataStructure()
	{
		cardVector = new LinkedList<CardData>();
	}

	public LinkedList <Integer> getVisibleSymbols()
	{
		LinkedList<Integer> visibleSymbols = new LinkedList<Integer>();
		LinkedList<CardData> sortedVector = new LinkedList<CardData>();
		//calcolo
		sortedVector = (LinkedList <CardData>) cardVector.clone(); //probably is not good

		visibleSymbols.add(0,5); //Fungi
		visibleSymbols.add(1,5); //Insect
		visibleSymbols.add(2,5); //Plant
		visibleSymbols.add(3,5); //Animal
		visibleSymbols.add(4,5); //Quill
		visibleSymbols.add(5,5); //Inkwell
		visibleSymbols.add(6,5); //Manuscript

		return visibleSymbols;
	}


}
