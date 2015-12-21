package de.mknblch.lfp.lexer;

/**
 * Created by mknblch on 05.12.2015.
 */
public class SyntaxException extends Exception {

    SyntaxException() {
    }

    SyntaxException(String message) {
        super(message);
    }

    SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    SyntaxException(Throwable cause) {
        super(cause);
    }

    SyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
