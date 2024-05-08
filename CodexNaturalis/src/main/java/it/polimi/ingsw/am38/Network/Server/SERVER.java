package it.polimi.ingsw.am38.Network.Server;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Controller.LobbyManager;

public class SERVER implements Serializable {

    private static final long serialVersionUID = 3768243144909833291L;

    private static LobbyManager LM;

    public static void main(String[] args) throws FileNotFoundException, RemoteException, AlreadyBoundException {
        int portTCP, portRMI;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ServerConfiguration.json"))));
        JsonObject jsonObject = gson.fromJson(jsonReader,JsonObject.class);

        if(args.length == 3){
            portTCP = Integer.parseInt(args[1]);
            portRMI = Integer.parseInt(args[2]);
        }else{
            portTCP = jsonObject.get("TCP").getAsInt();
            portRMI = jsonObject.get("RMI").getAsInt();
        }

        ServerRMI serverRMI = new ServerRMI(portRMI);
        serverRMI.start();

        //ServerTCP serverTCP = new ServerTCP(portTCP);
        //Thread TCPThread = new Thread(serverTCP);

        LM = new LobbyManager();
        LM.setServerRMI(serverRMI);
        //LM.setServerTCP(serverTCP);
        serverRMI.setLobbyManager(LM);
    }
}
