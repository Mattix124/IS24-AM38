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

		LinkedList <Integer> v = new LinkedList <>();

		v.add(1);
		v.add(2);
		v.add(3);
		v.add(4);
		v.add(5);
		v.add(6);
		v.add(7);
		v.add(8);
		v.add(9);
		v.add(10);

		System.out.println(v);

		v.removeAll(v);
		System.out.println(v);




	}
}
