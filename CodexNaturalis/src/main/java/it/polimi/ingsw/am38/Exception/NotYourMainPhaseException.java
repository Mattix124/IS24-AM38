package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when it's not this player main phase
 */
public class NotYourMainPhaseException extends Exception{
    public NotYourMainPhaseException(String message){
        super(message);
    }
}
