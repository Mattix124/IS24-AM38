package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when a deck is empty
 */
public class EmptyDeckException extends Exception{
    public EmptyDeckException(String message){
        super(message);
    }
}
