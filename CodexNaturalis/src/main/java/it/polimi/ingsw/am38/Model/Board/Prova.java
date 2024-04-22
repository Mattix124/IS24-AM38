package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

import java.util.LinkedList;

public class Prova
{
	private LinkedList <CardData> vettore = new LinkedList <CardData>();

	public static void main(String[] args) {
		Prova p = new Prova();
		p.start();
	}

	public void start() {
		StarterCard s = new StarterCard(1, null, null, "null", "none", "animal", "insect", "plant", "fungi", "none", "quill", "animal", "fungi", "plant");
		s.setFace(true);

		Field f = new Field(s);
		//f.getPossiblePlacement().stream().forEach(x-> System.out.println("X: "+x.x()+" Y: "+x.y()));
	/*
		LinkedList <CardData> sv = f.getSortedVector();
		f.addOrderedCard(new CardData(new Coords(7, -1), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(5, 8), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(-4, 2), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(3, -8), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(2, -5), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(-9, 3), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(10, -5), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(5, 4), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);
		f.addOrderedCard(new CardData(new Coords(50, 4), new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell")), sv);

	sv.stream().forEach(c -> System.out.println("X: "+c.coordinates().x()+" Y: "+c.coordinates().y()));
*/
		try
		{
			System.out.println("Start");
			f.getPossiblePlacement().stream().forEach(x-> System.out.println("X: "+x.x()+" Y: "+x.y()));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"),new Coords(-1,0));
			f.getPossiblePlacement().stream().forEach(x-> System.out.println("X: "+x.x()+" Y: "+x.y()));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"),new Coords(0,-1));
			f.getPossiblePlacement().stream().forEach(x-> System.out.println("X: "+x.x()+" Y: "+x.y()));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"),new Coords(-2,0));
			f.getPossiblePlacement().stream().forEach(x-> System.out.println("X: "+x.x()+" Y: "+x.y()));
			f.tryPlaceCard(new ResourceCard(1, "fungi", null, null, 3, "animal", "insect", "plant", "fungi", "none", "quill", "manuscript", "inkwell"),new Coords(-1,1));
			f.getPossiblePlacement().stream().forEach(x-> System.out.println("Final X: "+x.x()+" Y: "+x.y()));
			//f.getSortedVector().stream().forEach(x->);
		}
		catch (NotPlaceableException e)
		{
			throw new RuntimeException(e);
		}


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
