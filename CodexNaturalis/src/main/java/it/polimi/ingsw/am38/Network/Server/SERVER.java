package it.polimi.ingsw.am38.Network.Server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Controller.LobbyManager;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Objects;

/**
 * This is the main class of the server that create both the serverTCP
 * and the serverRMI
 */
public class SERVER implements Serializable
{
	private static final long serialVersionUID = 7906971458142094128L;
	/**
	 * LobbyManager instance
	 */
	private static final LobbyManager LM = LobbyManager.getLobbyManager();

	/**
	 * Launch ServerRMI and ServerTCP
	 *
	 * @param args are the input parameters
	 * @throws FileNotFoundException
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, RemoteException, AlreadyBoundException
	{
		int        portTCP, portRMI;
		Gson       gson       = new Gson();
		JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ServerConfiguration.json"))));
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

		if (args.length == 3)
		{
			portTCP = Integer.parseInt(args[1]);
			portRMI = Integer.parseInt(args[2]);
		}
		else
		{
			portTCP = jsonObject.get("TCP").getAsInt();
			portRMI = jsonObject.get("RMI").getAsInt();
		}

		ServerRMI serverRMI = new ServerRMI(portRMI);
		serverRMI.start();

		ServerTCP serverTCP = new ServerTCP(portTCP);
		serverTCP.start();


		LM.setServerRMI(serverRMI);
		LM.setServerTCP(serverTCP);
	}
}
