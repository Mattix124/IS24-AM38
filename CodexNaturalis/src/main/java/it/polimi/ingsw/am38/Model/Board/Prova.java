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
		StarterCard s = new StarterCard(1, null, null, "plant", "animal", "animal", "insect", "plant", "fungi", "none", "quill", "animal", "fungi", "plant");
		s.setFace(false);
		ObjectiveCard o = new ObjectiveCard(3, "fungi", "diagonal", "s", "s", 3, "null", "NE", "null");
		ObjectiveCard o2 = new ObjectiveCard(3, "fungi", "shapeL", "s", "s", 3, "plant", "SE", "null");

		Field f = new Field(s);
		for (Symbol sy : Symbol.values())
		{
			System.out.println(sy);
			System.out.println(f.getVisibleElements().getSymbol(sy));
		}

		int point = 0;
		try
		{
			f.getPossiblePlacement().stream().forEach(x -> System.out.println("X: " + x.x() + " Y: " + x.y()));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-1, 0));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, 0));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-3, 0));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-1, 1));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, 1));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-3, 1));
			f.tryPlaceCard(new ResourceCard(1, "plant", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"), new Coords(-2, -1));


			point += f.CheckObjectivePoints(o);
			point += f.CheckObjectivePoints(o2);


		}
		catch (NotPlaceableException e)
		{
			throw new RuntimeException(e);
		}

		System.out.println(point);
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

	}
}
