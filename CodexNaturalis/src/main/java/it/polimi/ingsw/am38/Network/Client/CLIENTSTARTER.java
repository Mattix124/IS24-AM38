package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.View.CLI;
import it.polimi.ingsw.am38.View.GUI;
import it.polimi.ingsw.am38.View.Viewable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * This is the main client class that create the clients thread based on the connection chosen
 */
public class CLIENTSTARTER
{

	/**
	 * Object used to perform a wait
	 */
	private static final Object lock = new Object();
	/**
	 * Boolean to check a disconnection
	 */
	private static boolean disconnectionHappened = false;
	/**
	 * Boolean used for the reconnection, the player can decide if exit ore reconnect
	 */
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

	/**
	 * Create RMI or TCP client and the type of view chosen(TUI or GUI)
	 *
	 * @param args
	 */
	private void start(String[] args)
	{

		if (args.length < 2 || (!args[1].equalsIgnoreCase("CLI") && !args[1].equalsIgnoreCase("GUI")) || (!args[0].equalsIgnoreCase("RMI") && !args[0].equalsIgnoreCase("TCP")))
		{
			System.out.println("Invalid input, try again: (TCP/RMI) (CLI/GUI)");
			return;
		}
		do
		{
			Gson       gson       = new Gson();
			JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ServerConfiguration.json"))));
			JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
			String     ip;

			if ((args.length == 3) && (args[2] != null))
				ip = args[2];
			else
				ip = jsonObject.get("ip").getAsString();

			Viewable viewInterface;
			if (args[1].equalsIgnoreCase("CLI"))
				viewInterface = new CLI();
			else
				viewInterface = new GUI();

			if (args[0].equalsIgnoreCase("rmi"))
			{
				ClientRMI clientRMI = null;
				try
				{
					clientRMI = new ClientRMI(ip, jsonObject.get("RMI").getAsInt(), viewInterface);

					clientRMI.setSort(clientRMI);
				}
				catch (RemoteException e)
				{
					throw new RuntimeException(e);
				}
			}
			else
			{
				TCPClient tcpClient = new TCPClient(ip, jsonObject.get("TCP").getAsInt(), viewInterface);
				tcpClient.setName("ClientCN");
				tcpClient.setDaemon(true);
				tcpClient.start();
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

	/**
	 * Method that notifies all the clients that this one is disconnected
	 */
	public static void disconnectionHappenedSetter()
	{
		synchronized (lock)
		{
			disconnectionHappened = true;
			lock.notifyAll();
		}

	}

	/**
	 * Method that allow the client to disconnect
	 */
	public static void quit()
	{
		exit = true;
		disconnectionHappenedSetter();
	}
}
