package library.service;

import library.exception.BookNotFoundException;
import library.model.Book;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of BookService
 * Follows Single Responsibility Principle (SRP) - only handles book management
 * Uses collections (HashMap) for efficient book storage and retrieval
 */
public class BookServiceImpl implements BookService {
    private final Map<String, Book> books;

    public BookServiceImpl() {
        this.books = new HashMap<>();
    }

    @Override
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        books.put(book.getIsbn(), book);
    }

    @Override
    public void removeBook(String isbn) throws BookNotFoundException {
        if (!books.containsKey(isbn)) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        }
        books.remove(isbn);
    }

    @Override
    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        }
        return book;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
}
