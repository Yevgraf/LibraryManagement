package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Member extends User {
    private int maxBorrowedBooks;
    private Map<Book, LocalDate> bookLoans;

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.maxBorrowedBooks = 3;
        this.bookLoans = new HashMap<>();
    }

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(bookLoans.keySet());
    }

    public void borrowBook(Book book) {
        if (bookLoans.size() >= maxBorrowedBooks) {
            throw new IllegalStateException("Maximum number of borrowed books reached");
        }
        bookLoans.put(book, LocalDate.now());
    }

    public void returnBook(Book book) {
        if (!bookLoans.containsKey(book)) {
            throw new IllegalArgumentException("Book not loaned");
        }
        bookLoans.remove(book);
    }

    public LocalDate getLoanDate(Book book) {
        if (!bookLoans.containsKey(book)) {
            throw new IllegalArgumentException("Book not loaned");
        }
        return bookLoans.get(book);
    }
}
