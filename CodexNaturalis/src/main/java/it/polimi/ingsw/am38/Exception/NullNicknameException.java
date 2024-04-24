package it.polimi.ingsw.am38.Exception;

/**
 * Exception that is thrown when the nickname is null or empty
 */
public class NullNicknameException extends Exception{
    public NullNicknameException(String message){
        super(message);
    }
}
