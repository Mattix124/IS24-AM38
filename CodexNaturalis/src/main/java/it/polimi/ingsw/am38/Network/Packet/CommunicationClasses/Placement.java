package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.util.LinkedList;

/**
 * Class that allow the sending of all possible placement inside Message Class
 */
public class Placement extends MessageContent
{
	/**
	 * Contains all the possible placement
	 */
	private final LinkedList <Coords> placements;

	/**
	 * Constructor of MPlacement
	 * @param placements all the placement
	 */
	public Placement(LinkedList <Coords> placements)
	{
		this.placements = placements;
	}

	/**
	 * Get all placement
	 * @return all placement
	 */
	public LinkedList <Coords> getPlacements()
	{
		return placements;
	}
}
