package library.service;

import library.exception.*;
import library.model.Book;
import library.model.Loan;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of LoanService
 * Follows Single Responsibility Principle (SRP) - only handles loan management
 * Follows Dependency Inversion Principle (DIP) - depends on abstractions (interfaces) not concrete classes
 * Uses collections (HashMap, ArrayList) for efficient loan storage and retrieval
 */
public class LoanServiceImpl implements LoanService {
    private final Map<String, Loan> loans;
    private final BookService bookService;
    private final MemberService memberService;
    private int loanIdCounter;

    /**
     * Constructor injection - follows Dependency Inversion Principle (DIP)
     * Depends on interfaces, not concrete implementations
     */
    public LoanServiceImpl(BookService bookService, MemberService memberService) {
        this.loans = new HashMap<>();
        this.bookService = bookService;
        this.memberService = memberService;
        this.loanIdCounter = 1;
    }

    @Override
    public Loan borrowBook(String memberId, String isbn) 
            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException {
        
        // Validate member exists
        memberService.findMemberById(memberId);
        
        // Validate book exists and is available
        Book book = bookService.findBookByIsbn(isbn);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book with ISBN " + isbn + " is not available");
        }

        // Create loan
        String loanId = "L" + String.format("%04d", loanIdCounter++);
        Loan loan = new Loan(loanId, memberId, isbn, LocalDate.now());
        loans.put(loanId, loan);

        // Mark book as unavailable
        book.setAvailable(false);

        return loan;
    }

    @Override
    public void returnBook(String loanId) throws InvalidOperationException {
        Loan loan = loans.get(loanId);
        if (loan == null) {
            throw new InvalidOperationException("Loan with ID " + loanId + " not found");
        }
        if (loan.isReturned()) {
            throw new InvalidOperationException("Book has already been returned");
        }

        // Mark loan as returned
        loan.setReturnDate(LocalDate.now());

        // Mark book as available
        try {
            Book book = bookService.findBookByIsbn(loan.getIsbn());
            book.setAvailable(true);
        } catch (BookNotFoundException e) {
            // Book was removed from system, ignore
        }
    }

    @Override
    public List<Loan> getLoansByMember(String memberId) {
        return loans.values().stream()
                .filter(loan -> loan.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getActiveLoans() {
        return loans.values().stream()
                .filter(loan -> !loan.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans.values());
    }
}
