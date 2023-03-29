package View;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import Model.Book;
import Model.Member;

public class CreateRequestBookView {
    private Scanner scanner;

    public CreateRequestBookView() {
        scanner = new Scanner(System.in);
    }

    public void getRequestBookInfo(List<Book> books, List<Member> members) {
    System.out.println("--- Register book request ---");

    // Display list of books
    System.out.println("Available books:");
    for (int i = 0; i < books.size(); i++) {
        System.out.println((i + 1) + ". " + books.get(i).getTitle());
    }

    // Select book
    int bookIndex = getValidIndex("Select the book (enter the corresponding number): ", books.size());
    Book book = books.get(bookIndex);

    // Display list of members
    System.out.println("Registered members:");
    for (int i = 0; i < members.size(); i++) {
        System.out.println((i + 1) + ". " + members.get(i).getName());
    }

    // Select member
    int memberIndex = getValidIndex("Select the member (enter the corresponding number): ", members.size());
    Member member = members.get(memberIndex);

    LocalDate requestDate = null;
    while (requestDate == null) {
        System.out.print("Enter the request date (in the format dd/MM/yy): ");
        String dateString = scanner.nextLine();
        try {
            requestDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yy"));
            LocalDate publicationDate = book.getPublicationDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            if (requestDate.isBefore(publicationDate) || requestDate.isAfter(currentDate)) {
                throw new DateTimeParseException("", "", 0);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Date should be on or after the book publishing date and before or on today's date. Please try again.");
            requestDate = null;
        }
    }

    // Perform some action with the data, like creating a book loan or reservation
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
