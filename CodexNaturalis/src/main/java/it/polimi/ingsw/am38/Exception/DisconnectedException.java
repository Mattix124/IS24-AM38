package it.polimi.ingsw.am38.Exception;

/**
 * Exception thrown if a client is disconnected
 */
public class DisconnectedException extends Exception
{
	public DisconnectedException(String message)
	{
		super(message);
	}
}
