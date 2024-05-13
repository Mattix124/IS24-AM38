package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Network.Server.GameThread;
import it.polimi.ingsw.am38.Network.Server.ServerRMI;
import it.polimi.ingsw.am38.Network.Server.ServerTCP;

import java.util.LinkedList;

/**
 * It contains all the references for threads so the garbage collector doesn't clear them.
 */
public class ReferenceContainer
{
	private LinkedList <GameThread> gt;
	private  ServerTCP tcp;
	private  ServerRMI rmi;

	/**
	 * Constructor of ReferenceContainer
	 */
	public ReferenceContainer()
	{
		gt = new LinkedList <>();
	}

	/**
	 * Add a Gamethread to the list
	 * @param thread GameThread
	 */
	public void add(GameThread thread)
	{
		gt.add(thread);
	}

	/**
	 * Add a TcpServer to the list
	 * @param server tcp server
	 */
	public void add(ServerTCP server)
	{
		tcp = server;
	}

	/**
	 * Add a TcpServer to the list
	 * @param server rmi server
	 */
	public void add(ServerRMI server)
	{
		rmi = server;
	}

	public LinkedList<GameThread> getGameTreadList()
	{
		return gt;
	}
}
