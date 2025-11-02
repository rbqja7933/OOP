package model;

import java.time.LocalDate;

/**
 * Represents a book loan transaction
 * Tracks borrowing information
 */
public class Loan {
    private final String loanId;
    private final String memberId;
    private final String isbn;
    private final LocalDate borrowDate;
    private LocalDate returnDate;

    public Loan(String loanId, String memberId, String isbn, LocalDate borrowDate) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = null;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return String.format("Loan[ID=%s, MemberID=%s, ISBN=%s, BorrowDate=%s, ReturnDate=%s]",
                loanId, memberId, isbn, borrowDate, returnDate != null ? returnDate : "Not returned");
    }
}
