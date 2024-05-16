package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.MSimpleString;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static it.polimi.ingsw.am38.Network.Packet.Scope.INFOMESSAGE;

public class LockClass
{
	private boolean allColored = false;
	private final Object lock = new Object();
	private GameController gc;

	public LockClass(GameController gc)
	{
		this.gc = gc;
	}

	public void waitForColors(ObjectOutputStream objectOut) throws IOException
	{
		synchronized (lock)
		{
			if (gc.isF())
			{
				allColored = true;
				lock.notifyAll();
			}
			while (!allColored)
			{
				try
				{
					objectOut.writeObject(new Message(INFOMESSAGE, INFOMESSAGE, new MSimpleString("Waiting for other players...")));
					lock.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}

	}

	public void waitothers() throws IOException
	{

		synchronized (lock)
		{
			if (gc.isF())
			{
				allColored = true;
				lock.notifyAll();
			}
			while (!allColored)
			{
				try
				{
					lock.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}

	}
}
