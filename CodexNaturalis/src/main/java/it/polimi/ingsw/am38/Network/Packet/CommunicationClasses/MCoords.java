package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allow the sending of coordinates inside Message Class
 */
public class MCoords extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 4686714687L;

	/**
	 * Coordinates passed
	 */
	private final Coords coords;
	/**
	 * Constructor of MCoords
	 */
	public MCoords(int x, int y)
	{
		this.coords = new Coords(x, y);
	}

	/**
	 * Getter of Coords
	 * @return Coords
	 */
	public Coords getCoords()
	{
		return coords;
	}
}
