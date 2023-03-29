package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Model.Book;
import Model.Member;

public class ReturnBookView {
    private Scanner scanner;
    private List<Book> books;
    private List<Member> members;
    private List<Loan> loans;

    public ReturnBookView(List<Book> books, List<Member> members) {
        scanner = new Scanner(System.in);
        this.books = books;
        this.members = members;
        this.loans = new ArrayList<>();
    }

    public void showReturnBookMenu() {
        System.out.println("--- Return Book Menu ---");
        System.out.println("1. Search for book");
        System.out.println("2. Search for member");
        System.out.println("3. Show all loans");
        System.out.println("4. Go back to main menu");

        int option = getValidOption("Enter an option: ", 1, 4);

        switch (option) {
            case 1:
                searchForBook();
                break;
            case 2:
                searchForMember();
                break;
            case 3:
                showAllLoans();
                break;
            case 4:
                break;
        }
    }

    private void searchForBook() {
        System.out.println("--- Search for book ---");

        int bookIndex = getValidIndex("Enter the index of the book you want to return: ", books.size());
        Book book = books.get(bookIndex);

        int loanIndex = getLoanIndexByBookId(book.getId());
        if (loanIndex == -1) {
            System.out.println("This book is not currently on loan.");
        } else {
            Loan loan = loans.get(loanIndex);
            Member member = getMemberById(loan.getMemberId());

            System.out.println("Book: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publication Date: " + book.getPublicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Loaned to: " + member.getName());
            System.out.println("Loan Date: " + loan.getLoanDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Return Date: " + loan.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            LocalDate currentDate = LocalDate.now();
            int daysOverdue = (int) Math.max(currentDate.toEpochDay() - loan.getReturnDate().toEpochDay(), 0);
            if (daysOverdue == 0) {
                System.out.println("Book returned on time.");
            } else {
                double fine = 3 + daysOverdue * 1;
                System.out.println("Book is " + daysOverdue + " days overdue. You owe a fine of " + String.format("%.2f", fine) + " euros.");
            }

            loans.remove(loanIndex);
        }
    }

    private void searchForMember() {
        System.out.println("--- Search for member ---");
    
        int memberIndex = getValidIndex("Enter the index of the member: ", members.size());
        Member member = members.get(memberIndex);
    
        boolean loanFound = false;
    
        for (Loan loan : loans) {
            if (loan.getMemberId() == member.getId()) {
                loanFound = true;
                Book book = getBookById(loan.getBookId());
    
                System.out.println("Book: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Publication Date: " + book.getPublicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Loan Date: " + loan.getLoanDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Return Date: " + loan.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    
                LocalDate currentDate = LocalDate.now();
                int daysOverdue = (int) Math.max(currentDate.toEpochDay() - loan.getReturnDate().toEpochDay(), 0);
                if (daysOverdue == 0) {
                    System.out.println("Book returned on time.");
                } else {
                    double fine = 3 + daysOverdue * 1;
                    System.out.println("Book is " + daysOverdue + " days overdue. You owe a fine of " + String.format("%.2f", fine) + " euros.");
                }
    
                loans.remove(loan);
                break;
            }
        }
    
        if (!loanFound) {
            System.out.println("No loans found for this member.");
        }
    }
    private void showAllLoans() {
        System.out.println("--- All Loans ---");
    
        for (Loan loan : loans) {
            Book book = getBookById(loan.getBookId());
            Member member = getMemberById(loan.getMemberId());
    
            System.out.println("Book: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publication Date: " + book.getPublicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Loaned to: " + member.getName());
            System.out.println("Loan Date: " + loan.getLoanDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Return Date: " + loan.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    
            LocalDate currentDate = LocalDate.now();
            int daysOverdue = (int) Math.max(currentDate.toEpochDay() - loan.getReturnDate().toEpochDay(), 0);
            if (daysOverdue == 0) {
                System.out.println("Book returned on time.");
            } else {
                double fine = 3 + daysOverdue * 1;
                System.out.println("Book is " + daysOverdue + " days overdue. You owe a fine of " + String.format("%.2f", fine) + " euros.");
            }
        }
    }
    
    private int getLoanIndexByBookId(int bookId) {
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getBookId() == bookId) {
                return i;
            }
        }
        return -1;
    }
    
    private Member getMemberById(int memberId) {
        for (Member member : members) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }
        
    