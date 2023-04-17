package de.gothaer.collections;

public class StapelException extends Exception {
    public StapelException() {
    }

    public StapelException(String message) {
        super(message);
    }

    public StapelException(String message, Throwable cause) {
        super(message, cause);
    }

    public StapelException(Throwable cause) {
        super(cause);
    }

    public StapelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
