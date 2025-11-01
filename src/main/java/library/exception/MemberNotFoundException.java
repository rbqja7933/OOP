package library.exception;

/**
 * Exception thrown when a member is not found in the library system
 */
public class MemberNotFoundException extends Exception {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
