package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;

public class VisibleElements
{
	private int fungi;
	private int insect;
	private int plant;
	private int animal;
	private int manuscript;
	private int quill;
	private int inkwell;

	public VisibleElements(int fungi, int insect, int plant, int animal, int manuscript, int quill, int inkwell)
	{
		this.fungi = fungi;
		this.insect = insect;
		this.plant = plant;
		this.animal = animal;
		this.manuscript = manuscript;
		this.quill = quill;
		this.inkwell = inkwell;
	}

	public int getSymbol(Symbol s)
	{
		return switch (s){
			case FUNGI -> fungi;
			case PLANT -> plant;
			case INSECT -> insect;
			case ANIMAL -> animal;
			case MANUSCRIPT -> manuscript;
			case QUILL -> quill;
			case INKWELL -> inkwell;
			default -> 0; // this needs to be defined
		};
	}
	public int getKingdom(Symbol s)
	{
		switch (s)
		{
			case FUNGI ->
			{
				return fungi;
			}
			case PLANT ->
			{
				return plant;
			}
			case INSECT ->
			{
				return insect;
			}
			case ANIMAL ->
			{
				return animal;
			}
		}
		return 0;
	}

	public void increaseSymbol(Symbol s, int increment){
		switch (s){
			case FUNGI -> fungi += increment;
			case PLANT -> plant += increment;
			case INSECT -> insect += increment;
			case ANIMAL -> animal += increment;
			case MANUSCRIPT -> manuscript += increment;
			case QUILL -> quill += increment;
			case INKWELL -> inkwell += increment;
		}
	}

	public void increaseSymbol(Symbol s){
		switch (s){
			case FUNGI -> fungi++;
			case PLANT -> plant++;
			case INSECT -> insect++;
			case ANIMAL -> animal++;
			case MANUSCRIPT -> manuscript++;
			case QUILL -> quill++;
			case INKWELL -> inkwell++;
		}
	}
	/*public void increaseSymbol(Symbol s)
	{
		switch (s)
		{
			case FUNGI ->
			{
				fungi++;
			}
			case PLANT ->
			{
				plant++;
			}
			case INSECT ->
			{
				insect++;
			}
			case ANIMAL ->
			{
				animal++;
			}

		}
	}*/
}




