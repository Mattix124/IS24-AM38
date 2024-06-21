package it.polimi.ingsw.am38.Exception;

/**
 * Exception thrown if a card is not placeable in the coordinates chosen
 */
public class NotPlaceableException extends Exception
{
	public NotPlaceableException(String message)
	{
		super(message);
	}
}
