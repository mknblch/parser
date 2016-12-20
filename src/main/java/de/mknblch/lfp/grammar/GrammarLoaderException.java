package de.mknblch.lfp.grammar;

/**
 * Created by mknblch on 05.12.2015.
 */
public class GrammarLoaderException extends Exception {

    GrammarLoaderException() {
    }

    GrammarLoaderException(String message) {
        super(message);
    }

    GrammarLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    GrammarLoaderException(Throwable cause) {
        super(cause);
    }

    GrammarLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
