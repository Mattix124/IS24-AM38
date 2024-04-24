package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when the input given it's not valid (general)
 */
public class InvalidInputException extends Exception{
    public InvalidInputException (String message){
        super(message);
    }
}
