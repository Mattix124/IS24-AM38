package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when it's not this player draw phase
 */
public class NotYourDrawPhaseException extends Exception{
    public NotYourDrawPhaseException(String message){
        super(message);
    }
}
