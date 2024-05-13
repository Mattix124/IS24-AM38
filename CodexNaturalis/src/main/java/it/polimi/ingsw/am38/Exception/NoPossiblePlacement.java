package it.polimi.ingsw.am38.Exception;

/**
 * Exception thrown if the Coords chosen aren't valid/available
 */
public class NoPossiblePlacement extends Exception {
	public NoPossiblePlacement(String message)
	{
		super(message);
	}
}
