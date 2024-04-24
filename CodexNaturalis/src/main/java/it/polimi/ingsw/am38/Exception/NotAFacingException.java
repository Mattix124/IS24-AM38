package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when "face" string is not "face up" or "face down"
 */
public class NotAFacingException extends Exception{
    public NotAFacingException(String message){
        super(message);
    }
}
