package it.polimi.ingsw.am38.Model.Board;

import java.io.Serial;
import java.io.Serializable;

/**
 * Coords is a class that allows to relate two integer as coordinates of a placed card.
 */
public class Coords implements Serializable		//Simple pair of int that describes the position of a card (highly bounded with PlayableCard in the DataStructure)
{
	@Serial
	private static final long serialVersionUID = 176874654768L;

	/**
	 * The x coordinate of a card placed
	 */
	private int x;
	/**
	 * The y coordinate of a card placed
	 */
	private int y;

	/**
	 * The constructor of Coords class
	 *
	 * @param x assign x.
	 * @param y assign x.
	 */
	public Coords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the x coordinate of the class
	 *
	 * @return x
	 */
	public int x()
	{
		return x;
	}
	/**
	 * Return the y coordinate of the class
	 *
	 * @return y
	 */
	public int y()
	{
		return y;
	}
	/**
	 * Set the x coordinate of the class
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * Set the y coordinate of the class
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Overridden equals to simplify confrontation between two Coords class object
	 *
	 * @param obj the object to confront with
	 * @return the boolean value
	 */
	@Override
	public boolean equals(Object obj)
	{
		Coords c = (Coords) obj;
		return c.x() == x && c.y() == y;
	}

	/**
	 * Overridden toString to turn a more complex instance of the class Coords to a simple string
	 *
	 * @return a string that display the coordinates
	 */
	@Override
	public String toString()
	{
		return "Coords{" + "x=" + x + ", y=" + y + '}';
	}
}
