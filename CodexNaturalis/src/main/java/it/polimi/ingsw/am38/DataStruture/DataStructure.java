package it.polimi.ingsw.am38.DataStruture;
import it.polimi.ingsw.am38.Miscellaneous.*;

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

	public LinkedList <Symbol> getVisibleSymbols()
	{
		LinkedList<Symbol> visibleSymbols = new LinkedList<Symbol>();
		LinkedList<Symbol> sortedVector = new LinkedList<Symbol>();
		//calcolo
		sortedVector = (LinkedList <Symbol>) cardVector.clone(); //probably is not good

		visibleSymbols.add(0,); //Fungi
		visibleSymbols.add(1,); //Insect
		visibleSymbols.add(2,); //Plant
		visibleSymbols.add(3,); //Animal
		visibleSymbols.add(4,); //Quill
		visibleSymbols.add(5,); //Inkwell
		visibleSymbols.add(6,); //Manuscript


	}


}
