package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when a pawn color is already taken
 */
public class ColorTakenException extends Exception{
    public ColorTakenException (String message){
        super(message);
    }
}
