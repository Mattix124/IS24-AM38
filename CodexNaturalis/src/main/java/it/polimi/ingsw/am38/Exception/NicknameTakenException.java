package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when a nickname is taken
 */
public class NicknameTakenException extends Exception{
    public NicknameTakenException(String message){
        super(message);
    }
}
