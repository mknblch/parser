package de.mknblch.lfp.grammar;

/**
 * Created by mknblch on 05.12.2015.
 */
public class GrammarReaderException extends Exception {

    GrammarReaderException() {
    }

    GrammarReaderException(String message) {
        super(message);
    }

    GrammarReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    GrammarReaderException(Throwable cause) {
        super(cause);
    }

    GrammarReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
