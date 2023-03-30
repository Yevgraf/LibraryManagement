package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import Model.Book;
import Model.Member;

public class ReturnBookView {
    private Scanner scanner;

    public ReturnBookView() {
        scanner = new Scanner(System.in);
    }

    public void getReturnBookInfo(List<Member> members) {
        System.out.println("--- Return a book ---");

        // Display list of members
        System.out.println("Registered members:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName());
        }

        // Select member
        int memberIndex = getValidIndex("Select the member who wants to return a book (enter the corresponding number): ", members.size());
        Member member = members.get(memberIndex);

        // Display list of book loans for the selected member
        List<Book> memberBooks = member.getBooks();
        System.out.println("Books loaned by " + member.getName() + ":");
        for (int i = 0; i < memberBooks.size(); i++) {
            Book book = memberBooks.get(i);
            LocalDate loanDate = member.getLoanDate(book);
            System.out.println((i + 1) + ". " + book.getTitle() + " (loaned on " + loanDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }

        // Select book
        int bookIndex = getValidIndex("Select the book to return (enter the corresponding number): ", memberBooks.size());
        Book book = memberBooks.get(bookIndex);

        // Get return date
        LocalDate returnDate = null;
        while (returnDate == null) {
            System.out.print("Enter the return date (in the format dd/MM/yyyy): ");
            String dateString = scanner.nextLine();
            try {
                returnDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate loanDate = member.getLoanDate(book);
                if (returnDate.isBefore(loanDate) || returnDate.isAfter(LocalDate.now())) {
                    throw new DateTimeParseException("", "", 0);
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Date should be on or after the loan date and before or on today's date. Please try again.");
                returnDate = null;
            }
        }

        // Remove book loan from member's book loans
        member.returnBook(book);

        System.out.println("Book returned successfully.");
    }

    private int getValidIndex(String message, int size) {
        while (true) {
            System.out.print(message);
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                if (index >= 0 && index < size) {
                    return index;
                } else {
                    displayErrorMessage("Invalid index. Please try again.");
                }
            } catch (NumberFormatException e) {
                displayErrorMessage("Invalid input. Please try again.");
            }
        }
    }

    private void displayErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
}
