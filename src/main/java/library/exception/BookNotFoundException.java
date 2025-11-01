package library.exception;

/**
 * Exception thrown when a book is not found in the library system
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
