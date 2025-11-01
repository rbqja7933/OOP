# Library System (도서 대출 시스템)

Java로 구현된 객체지향 도서 대출 시스템

## 프로젝트 구조

```
src/main/java/library/
├── Main.java                           # 메인 클래스 및 테스트 케이스
├── model/                              # 도메인 모델
│   ├── Book.java                      # 도서 엔티티
│   ├── Member.java                    # 회원 엔티티
│   └── Loan.java                      # 대출 엔티티
├── service/                            # 서비스 계층
│   ├── BookService.java               # 도서 관리 인터페이스
│   ├── BookServiceImpl.java           # 도서 관리 구현체
│   ├── MemberService.java             # 회원 관리 인터페이스
│   ├── MemberServiceImpl.java         # 회원 관리 구현체
│   ├── LoanService.java               # 대출 관리 인터페이스
│   └── LoanServiceImpl.java           # 대출 관리 구현체
└── exception/                          # 예외 클래스
    ├── BookNotFoundException.java
    ├── MemberNotFoundException.java
    ├── BookNotAvailableException.java
    └── InvalidOperationException.java
```

## 주요 기능

### 1. 도서 관리 (Book Management)
- 도서 등록
- 도서 삭제
- ISBN으로 도서 검색
- 제목으로 도서 검색
- 저자로 도서 검색
- 전체 도서 목록 조회

### 2. 회원 관리 (Member Management)
- 회원 등록
- 회원 삭제
- 회원 ID로 검색
- 전체 회원 목록 조회

### 3. 대출 관리 (Loan Management)
- 도서 대출
- 도서 반납
- 회원별 대출 이력 조회
- 활성 대출 조회 (미반납 도서)
- 전체 대출 이력 조회

## SOLID 원칙 적용

### 1. Single Responsibility Principle (SRP) - 단일 책임 원칙
**적용 위치:**
- `BookServiceImpl`: 오직 도서 관리 기능만 담당
- `MemberServiceImpl`: 오직 회원 관리 기능만 담당
- `LoanServiceImpl`: 오직 대출 관리 기능만 담당

**이유:** 각 클래스가 하나의 책임만 가지므로, 변경 이유가 명확하고 유지보수가 용이합니다.

```java
// BookServiceImpl은 도서 관리만 담당
public class BookServiceImpl implements BookService {
    private final Map<String, Book> books;
    
    public void addBook(Book book) { ... }
    public Book findBookByIsbn(String isbn) { ... }
    // ... 도서 관련 기능만 구현
}
```

### 2. Open/Closed Principle (OCP) - 개방/폐쇄 원칙
**적용 위치:**
- 모든 Service 인터페이스 (BookService, MemberService, LoanService)

**이유:** 인터페이스를 통해 새로운 구현체를 추가할 수 있으며, 기존 코드를 수정하지 않고 기능을 확장할 수 있습니다.

```java
// 인터페이스로 정의하여 확장 가능
public interface BookService {
    void addBook(Book book);
    Book findBookByIsbn(String isbn) throws BookNotFoundException;
    // ...
}

// 다양한 구현체를 추가 가능 (예: DatabaseBookServiceImpl, CacheBookServiceImpl)
public class BookServiceImpl implements BookService { ... }
```

### 3. Liskov Substitution Principle (LSP) - 리스코프 치환 원칙
**적용 위치:**
- 모든 Service 구현체가 인터페이스를 완전히 준수

**이유:** BookService 인터페이스 타입을 사용하는 코드는 BookServiceImpl을 포함한 어떤 구현체로도 대체 가능합니다.

```java
// 인터페이스 타입으로 선언
BookService bookService = new BookServiceImpl();
// 다른 구현체로 쉽게 교체 가능
// BookService bookService = new DatabaseBookServiceImpl();
```

### 4. Interface Segregation Principle (ISP) - 인터페이스 분리 원칙
**적용 위치:**
- BookService, MemberService, LoanService를 각각 분리

**이유:** 하나의 큰 LibraryService 인터페이스 대신, 기능별로 분리된 작은 인터페이스를 사용하여 클라이언트가 필요한 메서드만 의존하도록 합니다.

```java
// 기능별로 인터페이스 분리
public interface BookService { ... }      // 도서 관련 기능만
public interface MemberService { ... }    // 회원 관련 기능만
public interface LoanService { ... }      // 대출 관련 기능만

// 만약 ISP를 위반했다면:
// public interface LibraryService {
//     // 모든 기능이 하나의 인터페이스에... (X)
// }
```

### 5. Dependency Inversion Principle (DIP) - 의존성 역전 원칙
**적용 위치:**
- `LoanServiceImpl`의 생성자 주입

**이유:** 구체적인 클래스가 아닌 추상화(인터페이스)에 의존하여, 결합도를 낮추고 테스트 가능성을 높입니다.

```java
public class LoanServiceImpl implements LoanService {
    private final BookService bookService;      // 인터페이스에 의존
    private final MemberService memberService;  // 인터페이스에 의존
    
    // 생성자 주입으로 의존성 주입
    public LoanServiceImpl(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }
}

// Main.java에서 사용
BookService bookService = new BookServiceImpl();
MemberService memberService = new MemberServiceImpl();
LoanService loanService = new LoanServiceImpl(bookService, memberService);
```

## 예외 처리

시스템은 다양한 예외 상황을 처리합니다:

1. **BookNotFoundException**: 존재하지 않는 도서 접근 시
2. **MemberNotFoundException**: 존재하지 않는 회원 접근 시
3. **BookNotAvailableException**: 이미 대출된 도서를 대출하려 할 때
4. **InvalidOperationException**: 잘못된 작업 수행 시 (예: 이미 반납된 도서 재반납)

## 컬렉션 활용

- **HashMap**: 빠른 검색을 위해 ISBN/회원ID를 키로 사용
- **ArrayList**: 검색 결과 및 목록 조회에 사용
- **Stream API**: 필터링 및 데이터 처리에 활용

## 컴파일 및 실행

```bash
# 컴파일
javac -d out src/main/java/library/*.java src/main/java/library/*/*.java

# 실행
java -cp out library.Main
```

또는 디렉토리에서 직접:

```bash
# src/main/java 디렉토리에서
javac library/*.java library/*/*.java
java library.Main
```

## 테스트 케이스

Main.java에 다음 테스트 케이스가 포함되어 있습니다:

1. 도서 등록 테스트
2. 회원 등록 테스트
3. 도서 검색 테스트 (제목, 저자, ISBN)
4. 도서 대출 테스트
5. 도서 반납 테스트
6. 예외 처리 테스트 (5가지 시나리오)
7. 대출 현황 조회 테스트

## OOP 원칙

- **캡슐화**: 모든 필드는 private으로 선언하고 getter/setter로 접근
- **추상화**: 인터페이스를 통한 구현 세부사항 숨김
- **상속**: Exception 클래스 상속
- **다형성**: 인터페이스 타입으로 다양한 구현체 사용 가능