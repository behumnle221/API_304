package com.bank.exception;

/**
 * Exception levée quand on essaie de créer un compte avec un email déjà existant
 */
public class AccountAlreadyExistsException extends RuntimeException {
    
    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    public AccountAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
