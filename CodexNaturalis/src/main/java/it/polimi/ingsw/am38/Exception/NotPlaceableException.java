package it.polimi.ingsw.am38.Exception;

/**
 * Exception thrown if the Coords chosen aren't valid/available
 */
public class NotPlaceableException extends Exception {
	public NotPlaceableException(String message)
	{
		super(message);
	}
}
