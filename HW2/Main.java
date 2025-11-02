import exception.*;
import model.*;
import service.*;
import java.util.List;

/**
 * Main class with test cases for the Library System
 * Demonstrates all features of the library system
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("도서 대출 시스템 테스트 (Library System Test)");
        System.out.println("========================================\n");

        // Initialize services (Dependency Injection)
        BookService bookService = new BookServiceImpl();
        MemberService memberService = new MemberServiceImpl();
        LoanService loanService = new LoanServiceImpl(bookService, memberService);

        // Test 1: 도서 등록 (Book Registration)
        System.out.println("=== Test 1: 도서 등록 (Book Registration) ===");
        testBookRegistration(bookService);

        // Test 2: 회원 등록 (Member Registration)
        System.out.println("\n=== Test 2: 회원 등록 (Member Registration) ===");
        testMemberRegistration(memberService);

        // Test 3: 도서 검색 (Book Search)
        System.out.println("\n=== Test 3: 도서 검색 (Book Search) ===");
        testBookSearch(bookService);

        // Test 4: 도서 대출 (Book Borrowing)
        System.out.println("\n=== Test 4: 도서 대출 (Book Borrowing) ===");
        testBookBorrowing(loanService);

        // Test 5: 도서 반납 (Book Return)
        System.out.println("\n=== Test 5: 도서 반납 (Book Return) ===");
        testBookReturn(loanService);

        // Test 6: 예외 처리 (Exception Handling)
        System.out.println("\n=== Test 6: 예외 처리 (Exception Handling) ===");
        testExceptionHandling(bookService, memberService, loanService);

        // Test 7: 대출 현황 조회 (Loan Status Inquiry)
        System.out.println("\n=== Test 7: 대출 현황 조회 (Loan Status Inquiry) ===");
        testLoanInquiry(loanService);

        System.out.println("\n========================================");
        System.out.println("모든 테스트 완료! (All tests completed!)");
        System.out.println("========================================");
    }

    private static void testBookRegistration(BookService bookService) {
        Book book1 = new Book("978-1-234567-89-0", "Effective Java", "Joshua Bloch");
        Book book2 = new Book("978-1-234567-89-1", "Clean Code", "Robert C. Martin");
        Book book3 = new Book("978-1-234567-89-2", "Design Patterns", "Gang of Four");
        Book book4 = new Book("978-1-234567-89-3", "Head First Java", "Kathy Sierra");

        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);
        bookService.addBook(book4);

        System.out.println("등록된 도서: " + bookService.getAllBooks().size() + "권");
        bookService.getAllBooks().forEach(System.out::println);
    }

    private static void testMemberRegistration(MemberService memberService) {
        Member member1 = new Member("M001", "김철수", "kim@example.com");
        Member member2 = new Member("M002", "이영희", "lee@example.com");
        Member member3 = new Member("M003", "박민수", "park@example.com");

        memberService.registerMember(member1);
        memberService.registerMember(member2);
        memberService.registerMember(member3);

        System.out.println("등록된 회원: " + memberService.getAllMembers().size() + "명");
        memberService.getAllMembers().forEach(System.out::println);
    }

    private static void testBookSearch(BookService bookService) {
        System.out.println("제목으로 검색 ('Java'): ");
        List<Book> javaBooks = bookService.searchBooksByTitle("Java");
        javaBooks.forEach(System.out::println);

        System.out.println("\n저자로 검색 ('Martin'): ");
        List<Book> martinBooks = bookService.searchBooksByAuthor("Martin");
        martinBooks.forEach(System.out::println);

        try {
            System.out.println("\nISBN으로 검색 ('978-1-234567-89-0'): ");
            Book book = bookService.findBookByIsbn("978-1-234567-89-0");
            System.out.println(book);
        } catch (BookNotFoundException e) {
            System.out.println("오류: " + e.getMessage());
        }
    }

    private static void testBookBorrowing(LoanService loanService) {
        try {
            System.out.println("회원 M001이 '978-1-234567-89-0' 도서 대출");
            Loan loan1 = loanService.borrowBook("M001", "978-1-234567-89-0");
            System.out.println("대출 성공: " + loan1);

            System.out.println("\n회원 M002가 '978-1-234567-89-1' 도서 대출");
            Loan loan2 = loanService.borrowBook("M002", "978-1-234567-89-1");
            System.out.println("대출 성공: " + loan2);

            System.out.println("\n회원 M003이 '978-1-234567-89-2' 도서 대출");
            Loan loan3 = loanService.borrowBook("M003", "978-1-234567-89-2");
            System.out.println("대출 성공: " + loan3);

        } catch (MemberNotFoundException | BookNotFoundException | BookNotAvailableException e) {
            System.out.println("오류: " + e.getMessage());
        }
    }

    private static void testBookReturn(LoanService loanService) {
        try {
            System.out.println("대출 ID 'L0001' 반납");
            loanService.returnBook("L0001");
            System.out.println("반납 성공!");

            // Verify the loan was returned
            List<Loan> allLoans = loanService.getAllLoans();
            allLoans.stream()
                    .filter(loan -> loan.getLoanId().equals("L0001"))
                    .forEach(System.out::println);

        } catch (InvalidOperationException e) {
            System.out.println("오류: " + e.getMessage());
        }
    }

    private static void testExceptionHandling(BookService bookService, 
                                               MemberService memberService, 
                                               LoanService loanService) {
        // Test 1: 존재하지 않는 도서 검색
        System.out.println("1. 존재하지 않는 도서 검색:");
        try {
            bookService.findBookByIsbn("999-9-999999-99-9");
        } catch (BookNotFoundException e) {
            System.out.println("   예외 처리 성공: " + e.getMessage());
        }

        // Test 2: 존재하지 않는 회원 검색
        System.out.println("\n2. 존재하지 않는 회원 검색:");
        try {
            memberService.findMemberById("M999");
        } catch (MemberNotFoundException e) {
            System.out.println("   예외 처리 성공: " + e.getMessage());
        }

        // Test 3: 이미 대출된 도서 대출 시도
        System.out.println("\n3. 이미 대출된 도서 대출 시도:");
        try {
            loanService.borrowBook("M001", "978-1-234567-89-1");
        } catch (MemberNotFoundException | BookNotFoundException e) {
            System.out.println("   오류: " + e.getMessage());
        } catch (BookNotAvailableException e) {
            System.out.println("   예외 처리 성공: " + e.getMessage());
        }

        // Test 4: 존재하지 않는 대출 반납 시도
        System.out.println("\n4. 존재하지 않는 대출 반납 시도:");
        try {
            loanService.returnBook("L9999");
        } catch (InvalidOperationException e) {
            System.out.println("   예외 처리 성공: " + e.getMessage());
        }

        // Test 5: 이미 반납된 도서 재반납 시도
        System.out.println("\n5. 이미 반납된 도서 재반납 시도:");
        try {
            loanService.returnBook("L0001");
        } catch (InvalidOperationException e) {
            System.out.println("   예외 처리 성공: " + e.getMessage());
        }
    }

    private static void testLoanInquiry(LoanService loanService) {
        System.out.println("전체 대출 현황:");
        List<Loan> allLoans = loanService.getAllLoans();
        System.out.println("총 대출 건수: " + allLoans.size());
        allLoans.forEach(System.out::println);

        System.out.println("\n활성 대출 (미반납):");
        List<Loan> activeLoans = loanService.getActiveLoans();
        System.out.println("미반납 도서: " + activeLoans.size() + "건");
        activeLoans.forEach(System.out::println);

        System.out.println("\n회원 M001의 대출 이력:");
        List<Loan> memberLoans = loanService.getLoansByMember("M001");
        memberLoans.forEach(System.out::println);
    }
}
