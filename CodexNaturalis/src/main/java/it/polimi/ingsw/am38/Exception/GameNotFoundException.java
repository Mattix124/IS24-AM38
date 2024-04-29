package it.polimi.ingsw.am38.Exception;

/**
 * Exception thrown when searching for a Game that doesn't exist
 */
public class GameNotFoundException extends Exception{
    public GameNotFoundException (String message){
        super(message);
    }
}
