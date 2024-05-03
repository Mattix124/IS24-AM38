package it.polimi.ingsw.am38.Network.TCP.Client;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

public class MessageInterpreterClient extends Thread
{

	private final LinkedList <String> queue;
	private final Object lock;

	private PrintWriter out;

	public MessageInterpreterClient(Object lock, PrintWriter cOut)
	{
		this.lock = lock;
		this.queue = new LinkedList <>();
		this.out = cOut;
	}

	public void run()
	{
		String   message;
		String[] stringV;
		while (true)
		{
			synchronized (lock)
			{

				while (queue.isEmpty())
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
				message = queue.removeFirst();

				stringV = message.split("/");
				if (stringV[0].equals("chat"))
				{
					message = "";
					if (stringV[1].equals("b"))
					{
						for (int i = 3 ; i < stringV.length ; i++)
							message += stringV[i];
						System.out.println(stringV[2] + ": " + message);
					}
					else
					{
						System.out.println(stringV[2] + " said to you: ");
						for (int i = 4 ; i < stringV.length ; i++)
							message += stringV[i];
						System.out.println(message);
					}

				}
				else if (stringV[0].equals("game"))
				{
					//parser in json
					return;
				}
			}
		}
	}

	public void addMessage(String m)
	{
		queue.add(m);
	}

}
