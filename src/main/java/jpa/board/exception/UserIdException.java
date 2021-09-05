package jpa.board.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * The Class CUserNotFoundException.
 */
public class UserIdException extends AuthenticationException {
   
   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new c user not found exception.
     *
     * @param msg the msg
     * @param t the t
     */
    public UserIdException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new c user not found exception.
     *
     * @param msg the msg
     */
    public UserIdException(String msg) {
        super(msg);
    }
}