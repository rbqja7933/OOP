package service;

import exception.BookNotFoundException;
import model.Book;
import java.util.List;

/**
 * Interface for book management operations
 * Follows Interface Segregation Principle (ISP) - focused on book operations only
 */
public interface BookService {
    /**
     * Add a new book to the library
     */
    void addBook(Book book);

    /**
     * Remove a book from the library
     */
    void removeBook(String isbn) throws BookNotFoundException;

    /**
     * Find a book by ISBN
     */
    Book findBookByIsbn(String isbn) throws BookNotFoundException;

    /**
     * Search books by title
     */
    List<Book> searchBooksByTitle(String title);

    /**
     * Search books by author
     */
    List<Book> searchBooksByAuthor(String author);

    /**
     * Get all books in the library
     */
    List<Book> getAllBooks();
}
