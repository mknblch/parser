package de.mknblch.lfp;

/**
 * Created by mknblch on 05.12.2015.
 */
public class GrammarException extends Exception {

    public GrammarException() {
    }

    public GrammarException(String message) {
        super(message);
    }

    public GrammarException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrammarException(Throwable cause) {
        super(cause);
    }

    public GrammarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
