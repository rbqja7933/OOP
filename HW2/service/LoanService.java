package service;

import exception.*;
import model.Loan;
import java.util.List;

/**
 * Interface for loan management operations
 * Follows Interface Segregation Principle (ISP) - focused on loan operations only
 */
public interface LoanService {
    /**
     * Borrow a book
     */
    Loan borrowBook(String memberId, String isbn) 
            throws MemberNotFoundException, BookNotFoundException, BookNotAvailableException;

    /**
     * Return a borrowed book
     */
    void returnBook(String loanId) throws InvalidOperationException;

    /**
     * Get all loans for a specific member
     */
    List<Loan> getLoansByMember(String memberId);

    /**
     * Get all active (unreturned) loans
     */
    List<Loan> getActiveLoans();

    /**
     * Get all loans
     */
    List<Loan> getAllLoans();
}
