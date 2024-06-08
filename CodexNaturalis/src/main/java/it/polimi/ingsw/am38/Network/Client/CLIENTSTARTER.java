package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;

/**
 * This is the main client class that create the clients thread based on the connection chosen
 */
public class CLIENTSTARTER implements Serializable
{
	@Serial
	private static final long serialVersionUID = 749383786771428581L;
	private static final Object lock = new Object();
	private static boolean disconnectionHappened = false;
	private static boolean exit = false;

	/**
	 * Create the clients thread base on the connection chosen
	 *
	 * @param args are the input parameters
	 * @throws IOException
	 * @throws NotBoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, NotBoundException, InterruptedException
	{
		CLIENTSTARTER clientstarter = new CLIENTSTARTER();
		clientstarter.start(args);
	}

	private void start(String[] args)
	{
		do
		{
			if (args.length < 2)
			{
				System.out.println("Invalid input, try again: (TCP/RMI) (CLI/GUI)");
				return;
			}
			Gson       gson       = new Gson();
			JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ServerConfiguration.json"))));
			JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
			String     ip;

			ClientInterface clientInterface;

			if ((args.length == 3) && (args[2] != null))
			{
				ip = args[2];
			}
			else
			{
				ip = jsonObject.get("ip").getAsString();
			}

			if (args[0].equalsIgnoreCase("rmi"))
			{
				ClientRMI clientRMI = null;
				try
				{
					clientRMI = new ClientRMI(ip, jsonObject.get("RMI").getAsInt());
				}
				catch (RemoteException e)
				{
					throw new RuntimeException(e);
				}
				clientRMI.start();
				clientInterface = clientRMI;
				try
				{
					clientInterface.setSort(clientInterface);
				}
				catch (RemoteException e)
				{
					throw new RuntimeException(e);
				}
			}
			else if (args[0].equalsIgnoreCase("tcp"))
			{
				TCPClient cnClient = new TCPClient(ip, jsonObject.get("TCP").getAsInt());
				cnClient.setName("ClientCN");
				cnClient.setDaemon(true);
				cnClient.start();
			}
			else
			{
				System.out.println("Invalid input, try again: (TCP/RMI) (CLI/GUI)");
				return;
			}

			if (args[1].equalsIgnoreCase("CLI"))
			{
			}

			synchronized (lock)
			{
				while (!disconnectionHappened)
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
				disconnectionHappened = false;

			}

		} while (!exit);
	}

	public static void disconnectionHappenedSetter()
	{
		synchronized (lock)
		{
			disconnectionHappened = true;
			lock.notifyAll();
		}

	}

	public static void quit()
	{
		exit = true;
		disconnectionHappenedSetter();

	}
}
