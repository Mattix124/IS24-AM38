package it.polimi.ingsw.am38.Network.TCP.Client;

import java.util.LinkedList;

public class MessageInterpreterClient extends Thread
{

	private final LinkedList <String> queue;

	public MessageInterpreterClient()
	{
		this.queue = new LinkedList <>();
	}

	public void run()
	{
		String   message;
		String[] stringV;
		while (true)
		{
			synchronized (queue)
			{
				while (queue.isEmpty())
				{
					try
					{
						queue.wait();
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
				}
				message = queue.removeFirst();
			}
			stringV = message.split("/");
			if (stringV[0].equals("chat"))
			{
				StringBuilder m = new StringBuilder();
				if (stringV[1].equals("b"))
				{
					for (int i = 3 ; i < stringV.length ; i++)
						m.append(stringV[i]);
					System.out.println(stringV[2] + ": " + m);
				}
				else
				{
					System.out.println(stringV[2] + " said to you: ");
					for (int i = 4 ; i < stringV.length ; i++)
						m.append(stringV[i]);
					System.out.println(m);
				}

			}
			else if (stringV[0].equals("game"))
			{
				//parser in json
				return;
			}
		}
	}

	public void addMessage(String m)
	{
		synchronized (queue)
		{
			queue.add(m);
			queue.notifyAll();
		}
	}

}
