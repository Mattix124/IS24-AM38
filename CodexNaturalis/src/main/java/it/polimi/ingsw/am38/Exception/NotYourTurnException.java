package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when it's not this player turn
 */
public class NotYourTurnException extends Exception{
    public NotYourTurnException(String message){
        super(message);
    }
}
