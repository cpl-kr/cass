package de.platen.clapsesy.app.exception;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppException(final Throwable exception) {
        super(exception);
    }
}
