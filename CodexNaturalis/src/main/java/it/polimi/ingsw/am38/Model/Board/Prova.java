package it.polimi.ingsw.am38.Model.Board;

import javafx.util.Pair;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Prova
{
	private LinkedList <CardData> vettore = new LinkedList <CardData>();

	public static void main(String[] args)
	{
		Prova p = new Prova();
		p.start();
	}

	public void start()
	{

	/*	Field f = new Field(new StarterCard());
		f.insertCard(new CardData(new Coords(7, -1), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(5, 8), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(-4, 2), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(3, -8), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(2, -5), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(-9, 3), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(10, -5), new ResourceCard(Kingdom.ANIMAL, 3)));
		f.insertCard(new CardData(new Coords(5, 4), new ResourceCard(Kingdom.ANIMAL, 3)));

		System.out.println("BOH:" + f.getSortedVector());

		vettore.add(new CardData(new Coords(0, 0), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(7, -1), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(5, 8), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(-4, 2), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(3, -8), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(2, -5), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(-9, 3), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(10, -5), new ResourceCard(Kingdom.ANIMAL, 3)));
		vettore.add(new CardData(new Coords(5, 4), new ResourceCard(Kingdom.ANIMAL, 3)));

*/

		LinkedHashSet <Integer> i = new LinkedHashSet <>();

		i.add(3);
		i.add(2);
		i.add(3);
		i.add(4);
		i.add(3);
		i.add(6);

		System.out.println(i);





	}
}
