package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

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
		/*StarterCard s = new StarterCard(1, null, null, "plant", "animal", "animal", "insect", "plant", "fungi", "plant", "quill", "animal", "fungi", "plant");
		s.setFace(false);
		ObjectiveCard o = new ObjectiveCard(3, "fungi", "diagonal", "s", "s", 3, "null", "NE", "null");
		//ObjectiveCard o2 = new ObjectiveCard(3, "fungi", "shapeL", "s", "s", 3, "plant", "SE", "null");

		Field f = new Field(s);

		ResourceCard c1 = new ResourceCard(1, "plant", null, null, 0, "animal", "insect", "plant", "fungi", "none", "none", "none", "none");
		c1.setFace(false);
		//CardData cd1 = new CardData(new Coords(1, 0),c1);

		ResourceCard c2 = new ResourceCard(2, "insect", null, null, 0, "animal", "insect", "plant", "fungi", "none", "none", "none", "none");
		c2.setFace(false);
		//CardData cd2 = new CardData(new Coords(0, 1),c2);

		ResourceCard c3 = new ResourceCard(3, "fungi", null, null, 0, "animal", "insect", "null", "fungi", "none", "quill", "manuscript", "inkwell");
		c3.setFace(true);
		//CardData cd3 = new CardData(new Coords(1, 1),c3);

		ResourceCard c4 = new ResourceCard(4, "animal", null, null, 0, "null", "none", "animal", "animal", "none", "quill", "manuscript", "inkwell");
		c4.setFace(true);
		// cd4 = new CardData(new Coords(0, 2),c4);

		ResourceCard c5 = new ResourceCard(5, "insect", null, null, 0, "null", "none", "animal", "animal", "none", "quill", "manuscript", "inkwell");
		c5.setFace(true);
		//CardData cd5 = new CardData(new Coords(-1, 0),c5);

		ResourceCard c6 = new ResourceCard(6, "fungi", null, null, 0, "none", "none", "null", "animal", "none", "quill", "manuscript", "inkwell");
		c6.setFace(true);
		// cd6 = new CardData(new Coords(2, 1),c6);

		ResourceCard c7 = new ResourceCard(7, "fungi", null, null, 0, "none", "none", "none", "null", "none", "quill", "manuscript", "inkwell");
		c7.setFace(true);
		//CardData cd7 = new CardData(new Coords(2, 2),c7);

		ResourceCard c8 = new ResourceCard(8, "fungi", null, null, 0, "none", "null", "none", "null", "none", "quill", "manuscript", "inkwell");
		c8.setFace(true);
		//CardData cd8 = new CardData(new Coords(2, 0),c8);

		ResourceCard c9 = new ResourceCard(9, "fungi", null, null, 0, "null", "none", "none", "animal", "none", "quill", "manuscript", "inkwell");
		c9.setFace(true);
		//CardData cd9 = new CardData(new Coords(3, 1),c9);

		int point = 0;
		try
		{
			//f.getPossiblePlacement().stream().forEach(x -> System.out.println("X: " + x.x() + " Y: " + x.y()));
			f.tryPlaceCard(c1, new Coords(1, 0));
			f.tryPlaceCard(c2, new Coords(0, 1));
			//f.getSortedVector().stream().forEach(x-> System.out.println(x));
			f.getPossiblePlacement().stream().forEach(x -> System.out.println(x));
			f.tryPlaceCard(c3, new Coords(1, 1));
			f.tryPlaceCard(c4, new Coords(0, 2));
			f.tryPlaceCard(c5, new Coords(-1, 0));
			f.tryPlaceCard(c6, new Coords(2, 1));
			f.tryPlaceCard(c7, new Coords(2, 2));
			f.tryPlaceCard(c8, new Coords(2, 0));
			f.tryPlaceCard(c9, new Coords(3, 1));

		/*	for (CardData cd : f.getSortedVector())
				System.out.println(cd.coordinates());
*/
		/*
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, 0));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-3, 0));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-1, 1));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, 1));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-3, 1));
			f.tryPlaceCard(new ResourceCard(1, "plant", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, -1));
			f.tryPlaceCard(new ResourceCard(1, "plant", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-3, -1));
			for (Symbol sy : Symbol.values())
			{
				System.out.println(sy);
				System.out.println(f.getVisibleElements().getSymbol(sy));
			}
			//	f.getPossiblePlacement().forEach(x-> System.out.println(x));

			//point += f.CheckObjectivePoints(o);
			point += f.CheckObjectivePoints(o);

		}
		catch (NotPlaceableException e)
		{
			throw new RuntimeException(e);
		}

		System.out.println(point);
		do{
			System.out.print("\033[H\033[2J");
			System.out.flush();

			try
			{
				System.out.print("Ciao");
				Thread.sleep(1000);
				System.out.print(".");
				Thread.sleep(1000);
				System.out.print(".");
				Thread.sleep(1000);
				System.out.print(".");
				Thread.sleep(1000);

			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}

		}
		while (true);

*/


/*
		LinkedList <Coords> v = new LinkedList <>();

		v.add(new Coords(0, 0));
		v.add(new Coords(2, 0));
		v.add(new Coords(0, 2));
		v.add(new Coords(0, 1));
		v.add(new Coords(0, 4));
		if (v.contains(new Coords(2, 0)))
			v.stream().forEach(x -> System.out.println("X: " + x.x() + " Y: " + x.y()));
*/
		String message = "Ciao 3 22";
		if(message.regionMatches(0," Ciao",1,4))
			System.out.println(message);

	}
}
