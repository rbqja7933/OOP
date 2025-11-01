package library.exception;

/**
 * Exception thrown when an invalid operation is attempted
 */
public class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}
