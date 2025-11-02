package exception;

/**
 * Exception thrown when a book is not available for borrowing
 */
public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
