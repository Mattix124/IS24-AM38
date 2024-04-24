package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when the player number is not between 2 and 4
 */
public class NumOfPlayersException extends Exception{
    public NumOfPlayersException(String message){
        super(message);
    }
}

