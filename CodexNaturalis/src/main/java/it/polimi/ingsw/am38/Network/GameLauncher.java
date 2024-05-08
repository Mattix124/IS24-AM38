package it.polimi.ingsw.am38.Network;

import it.polimi.ingsw.am38.Network.Client.CLIENT;
import it.polimi.ingsw.am38.Network.Server.SERVER;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

public class GameLauncher {
    public static void main(String[] args) throws AlreadyBoundException, IOException, NotBoundException {
        if(args[0].equalsIgnoreCase("server")){
            SERVER.main(args);
        }else if(args[0].equalsIgnoreCase("client")){
            String [] spec = new String[3];
            spec[0] = args[1];
            spec[1] = args[2];
            if(args.length==4){
                spec[2] = args[3];
            }
            CLIENT.main(spec);
        }
    }
}
