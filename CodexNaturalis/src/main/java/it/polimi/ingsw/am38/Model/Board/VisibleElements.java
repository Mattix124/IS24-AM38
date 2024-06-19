package it.polimi.ingsw.am38.Model.Board;

import it.polimi.ingsw.am38.Enum.Symbol;

import java.io.Serial;
import java.io.Serializable;

/**
 * Visible is a Class that contains the number of all visible symbols presents on the field of each player.
 */
public class VisibleElements implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754354768L;
	private int fungi;
	private int insect;
	private int plant;
	private int animal;
	private int manuscript;
	private int quill;
	private int inkwell;

	/**
	 * Constructor that initialize every symbol count to 0.
	 */
	public VisibleElements()
	{
		this.fungi = 0;
		this.insect = 0;
		this.plant = 0;
		this.animal = 0;
		this.manuscript = 0;
		this.quill = 0;
		this.inkwell = 0;
	}

	/**
	 * This method is used to get count of a given symbol.
	 *
	 * @param s the symbol.
	 * @return the number of time it appears.
	 */
	public int getSymbol(Symbol s)
	{
		return switch (s)
		{
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

	/**
	 * The method is used to increase (or decrease) the value of a given symbol.
	 *
	 * @param s         the symbol which number will be incremented.
	 * @param increment is the number that will be added to the current score of a determinate symbol.
	 */
	public void increaseSymbol(Symbol s, int increment)
	{
		switch (s)
		{
			case FUNGI -> fungi += increment;
			case PLANT -> plant += increment;
			case INSECT -> insect += increment;
			case ANIMAL -> animal += increment;
			case MANUSCRIPT -> manuscript += increment;
			case QUILL -> quill += increment;
			case INKWELL -> inkwell += increment;
		}
	}

	/**
	 * The method is used to increase by 1 the value of a given symbol.
	 *
	 * @param s the symbol which number will be incremented.
	 */
	public void increaseSymbol(Symbol s)
	{
		if (s != null)
			switch (s)
			{
				case FUNGI -> fungi++;
				case PLANT -> plant++;
				case INSECT -> insect++;
				case ANIMAL -> animal++;
				case MANUSCRIPT -> manuscript++;
				case QUILL -> quill++;
				case INKWELL -> inkwell++;
			}
	}
}