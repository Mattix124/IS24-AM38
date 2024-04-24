package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when a gameID is not valid
 */
public class GameNotFoundException extends Exception{
    public GameNotFoundException(String message){
        super(message);
    }
}
